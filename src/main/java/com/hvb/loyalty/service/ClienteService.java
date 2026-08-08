package com.hvb.loyalty.service;

import com.hvb.loyalty.dto.ClienteRequestDTO;
import com.hvb.loyalty.dto.ClienteResponseDTO;
import com.hvb.loyalty.entity.Cliente;
import com.hvb.loyalty.repository.ClienteRepository;
import com.hvb.loyalty.utils.ClienteUtil;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClienteService {

    private final ClienteRepository clienteRepository;
    private final TarjetaService tarjetaService;
    private final com.hvb.loyalty.repository.TarjetaRepository tarjetaRepository;
    private final EmailService emailService;

    public ClienteService(ClienteRepository clienteRepository,
                          TarjetaService tarjetaService,
                          com.hvb.loyalty.repository.TarjetaRepository tarjetaRepository,
                          EmailService emailService) {
        this.clienteRepository = clienteRepository;
        this.tarjetaService = tarjetaService;
        this.tarjetaRepository = tarjetaRepository;
        this.emailService = emailService;
    }

    public List<ClienteResponseDTO> listar() {
        return clienteRepository.findAll().stream()
                .map(this::aResponse)
                .collect(Collectors.toList());
    }

    public ClienteResponseDTO obtenerPorId(Long id) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
        return aResponse(cliente);
    }

    public ClienteResponseDTO crear(ClienteRequestDTO dto) {
        if (clienteRepository.existsByTelefono(dto.getTelefono())) {
            throw new RuntimeException("Ya existe un cliente con ese teléfono");
        }
        Cliente cliente = new Cliente();
        ClienteUtil.actualizarDatos(cliente, dto);
        Cliente guardado = clienteRepository.save(cliente);

        // Crea automáticamente la tarjeta del cliente
        tarjetaService.crearParaCliente(guardado);

        // Envía correo de bienvenida si el cliente proporcionó correo
        if (guardado.getCorreo() != null && !guardado.getCorreo().isBlank()) {
            emailService.enviarBienvenida(guardado.getCorreo(), guardado.getNombre());
        }

        return aResponse(guardado);
    }

    public ClienteResponseDTO actualizar(Long id, ClienteRequestDTO dto) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));

        if (!cliente.getTelefono().equals(dto.getTelefono())
                && clienteRepository.existsByTelefono(dto.getTelefono())) {
            throw new RuntimeException("Ya existe un cliente con ese teléfono");
        }

        ClienteUtil.actualizarDatos(cliente, dto);
        return aResponse(clienteRepository.save(cliente));
    }

    public void eliminar(Long id) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
        clienteRepository.delete(cliente);
    }

    private ClienteResponseDTO aResponse(Cliente c) {
        ClienteResponseDTO dto = new ClienteResponseDTO();
        dto.setId(c.getId());
        dto.setNombre(c.getNombre());
        dto.setApellidos(c.getApellidos());
        dto.setTelefono(c.getTelefono());
        dto.setCorreo(c.getCorreo());
        dto.setFechaRegistro(c.getFechaRegistro());
        dto.setActivo(c.getActivo());
        dto.setNivel(c.getNivel() != null ? c.getNivel().getNombre() : "Nuevo");

        // Datos de su tarjeta (puntos y QR)
        tarjetaRepository.findAll().stream()
                .filter(t -> t.getCliente() != null && t.getCliente().getId().equals(c.getId()))
                .findFirst()
                .ifPresent(t -> {
                    dto.setPuntos(t.getPuntosAcumulados());
                    dto.setCodigoQr(t.getCodigoQr());
                });

        return dto;
    }
}