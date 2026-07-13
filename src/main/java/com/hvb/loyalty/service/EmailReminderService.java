package com.hvb.loyalty.service;

import tools.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.util.Map;

@Service
public class EmailReminderService {

    @Value("${resend.api-key}")
    private String apiKey;

    @Value("${resend.from-email:onboarding@resend.dev}")
    private String fromEmail;

    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Manda el correo de recordatorio para agregar la tarjeta a Google Wallet.
     */
    public void enviarRecordatorioWallet(String correoDestino, String nombreCliente, String linkTarjeta) throws Exception {
        String asunto = "No olvides tu tarjeta de Healthy Vibes Bistro 🌿";
        String html = """
                <div style="font-family: sans-serif; max-width: 480px; margin: 0 auto;">
                    <h2 style="color:#1E2B1F;">¡Hola %s!</h2>
                    <p style="color:#444;">Vimos que aún no agregas tu tarjeta de lealtad a Google Wallet.
                    Solo toma un segundo y así siempre la traes contigo para acumular puntos en cada visita.</p>
                    <p style="text-align:center; margin: 28px 0;">
                        <a href="%s" style="background:#D4A843; color:#1E2B1F; padding:12px 24px;
                        border-radius:10px; text-decoration:none; font-weight:bold;">
                            Ver mi tarjeta y agregarla
                        </a>
                    </p>
                    <p style="color:#888; font-size:12px;">Healthy Vibes Bistro</p>
                </div>
                """.formatted(nombreCliente, linkTarjeta);

        Map<String, Object> payload = Map.of(
                "from", fromEmail,
                "to", correoDestino,
                "subject", asunto,
                "html", html
        );
        String jsonBody = objectMapper.writeValueAsString(payload);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.resend.com/emails"))
                .header("Authorization", "Bearer " + apiKey)
                .header("Content-Type", "application/json")
                .POST(BodyPublishers.ofString(jsonBody))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new RuntimeException("Error al enviar correo con Resend (" + response.statusCode() + "): " + response.body());
        }
    }
}