package com.hvb.loyalty.service;

import com.hvb.loyalty.entity.Tarjeta;
import com.hvb.loyalty.repository.TarjetaRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class RecordatorioWalletScheduler {

    private static final int HORAS_ESPERA = 3;
    private static final int HORA_MIN_ENVIO = 9;  // 9am
    private static final int HORA_MAX_ENVIO = 21;  // 9pm

    private final TarjetaRepository tarjetaRepository;
    private final EmailReminderService emailReminderService;

    @Value("${app.frontend-url:http://localhost:5173}")
    private String frontendUrl;

    public RecordatorioWalletScheduler(TarjetaRepository tarjetaRepository, EmailReminderService emailReminderService) {
        this.tarjetaRepository = tarjetaRepository;
        this.emailReminderService = emailReminderService;
    }

    // Corre cada 15 minutos
    @Scheduled(fixedRate = 15 * 60 * 1000)
    public void revisarPendientes() {
        LocalDateTime ahora = LocalDateTime.now();

        // Fuera de horario permitido (antes de 9am o después de 9pm): no manda nada, espera al siguiente ciclo
        if (ahora.getHour() < HORA_MIN_ENVIO || ahora.getHour() >= HORA_MAX_ENVIO) {
            return;
        }

        List<Tarjeta> pendientes = tarjetaRepository
                .findByWalletAgregadoFalseAndRecordatorioEnviadoFalseAndWalletPospuestoEnIsNotNull();

        for (Tarjeta tarjeta : pendientes) {
            boolean yaPasaronLasHoras = tarjeta.getWalletPospuestoEn()
                    .plusHours(HORAS_ESPERA)
                    .isBefore(ahora);

            if (!yaPasaronLasHoras) continue;

            String correo = tarjeta.getCliente() != null ? tarjeta.getCliente().getCorreo() : null;
            if (correo == null || correo.isBlank()) {
                // No tiene correo: no se le puede recordar por este canal.
                // Se marca como enviado para no revisarlo de nuevo en cada ciclo.
                tarjeta.setRecordatorioEnviado(true);
                tarjetaRepository.save(tarjeta);
                continue;
            }

            try {
                String nombre = tarjeta.getCliente().getNombre();
                String link = frontendUrl + "/tarjeta/" + tarjeta.getCodigoQr();
                emailReminderService.enviarRecordatorioWallet(correo, nombre, link);
                tarjeta.setRecordatorioEnviado(true);
                tarjetaRepository.save(tarjeta);
                System.out.println("[Recordatorio] Enviado a " + correo + " (tarjeta " + tarjeta.getCodigoQr() + ")");
            } catch (Exception e) {
                System.err.println("[Recordatorio] Error enviando a " + correo + ": " + e.getMessage());
                // No se marca como enviado: se reintenta en el siguiente ciclo
            }
        }
    }
}