package com.hvb.loyalty.service;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void enviarBienvenida(String destinatario, String nombre) {
        try {
            MimeMessage msg = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(msg, true, "UTF-8");
            helper.setFrom("healthybivesbistro039@gmail.com", "Healthy Vibes Bistro");
            helper.setTo(destinatario);
            helper.setSubject("¡Bienvenido al programa de lealtad, " + nombre + "!");
            helper.setText(construirBienvenida(nombre), true);
            mailSender.send(msg);
        } catch (Exception e) {
            System.err.println("Error al enviar correo de bienvenida: " + e.getMessage());
        }
    }

    public void enviarConfirmacionCanje(String destinatario, String nombre, String premioNombre, int puntosDescontados, int puntosRestantes) {
        try {
            MimeMessage msg = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(msg, true, "UTF-8");
            helper.setFrom("healthybivesbistro039@gmail.com", "Healthy Vibes Bistro");
            helper.setTo(destinatario);
            helper.setSubject("¡Recompensa canjeada con éxito!");
            helper.setText(construirCanje(nombre, premioNombre, puntosDescontados, puntosRestantes), true);
            mailSender.send(msg);
        } catch (Exception e) {
            System.err.println("Error al enviar correo de canje: " + e.getMessage());
        }
    }

    private String construirBienvenida(String nombre) {
        return """
            <div style="font-family:Arial,sans-serif;max-width:520px;margin:auto;background:#f8f4ec;border-radius:12px;overflow:hidden;">
              <div style="background:#2C3B2D;padding:28px 32px;text-align:center;">
                <h1 style="color:#C4943A;margin:0;font-size:22px;">Healthy Vibes Bistro</h1>
                <p style="color:#8FA88F;margin:6px 0 0;font-size:13px;">Programa de Lealtad</p>
              </div>
              <div style="padding:32px;">
                <h2 style="color:#2C3B2D;font-size:20px;">¡Hola, %s!</h2>
                <p style="color:#4a4a4a;line-height:1.7;">
                  Tu tarjeta de lealtad ha sido creada exitosamente. A partir de ahora acumularás puntos en cada visita a Healthy Vibes Bistro y podrás canjearlos por increíbles recompensas.
                </p>
                <div style="background:#2C3B2D;border-radius:10px;padding:20px;text-align:center;margin:24px 0;">
                  <p style="color:#C4943A;font-size:28px;font-weight:800;margin:0;">0 pts</p>
                  <p style="color:#8FA88F;font-size:13px;margin:4px 0 0;">Puntos actuales</p>
                </div>
                <p style="color:#4a4a4a;line-height:1.7;">
                  Presenta tu código QR en cada visita para que el mesero registre tu compra y sumes puntos automáticamente.
                </p>
              </div>
              <div style="background:#2C3B2D;padding:16px;text-align:center;">
                <p style="color:#8FA88F;font-size:12px;margin:0;">© 2025 Healthy Vibes Bistro · Todos los derechos reservados</p>
              </div>
            </div>
            """.formatted(nombre);
    }

    private String construirCanje(String nombre, String premio, int puntosDesc, int puntosRest) {
        return """
            <div style="font-family:Arial,sans-serif;max-width:520px;margin:auto;background:#f8f4ec;border-radius:12px;overflow:hidden;">
              <div style="background:#2C3B2D;padding:28px 32px;text-align:center;">
                <h1 style="color:#C4943A;margin:0;font-size:22px;">Healthy Vibes Bistro</h1>
                <p style="color:#8FA88F;margin:6px 0 0;font-size:13px;">Programa de Lealtad</p>
              </div>
              <div style="padding:32px;">
                <h2 style="color:#2C3B2D;font-size:20px;">¡Felicidades, %s!</h2>
                <p style="color:#4a4a4a;line-height:1.7;">
                  Has canjeado exitosamente una recompensa en Healthy Vibes Bistro. A continuación el resumen de tu canje:
                </p>
                <div style="background:#2C3B2D;border-radius:10px;padding:20px;margin:24px 0;">
                  <p style="color:#C4943A;font-size:16px;font-weight:700;margin:0 0 12px;">%s</p>
                  <p style="color:#8FA88F;font-size:13px;margin:4px 0;">Puntos descontados: <span style="color:#F5F0E8;font-weight:600;">%d pts</span></p>
                  <p style="color:#8FA88F;font-size:13px;margin:4px 0;">Puntos restantes: <span style="color:#F5F0E8;font-weight:600;">%d pts</span></p>
                </div>
                <p style="color:#4a4a4a;line-height:1.7;">
                  ¡Sigue visitándonos para acumular más puntos y seguir disfrutando de tus beneficios!
                </p>
              </div>
              <div style="background:#2C3B2D;padding:16px;text-align:center;">
                <p style="color:#8FA88F;font-size:12px;margin:0;">© 2025 Healthy Vibes Bistro · Todos los derechos reservados</p>
              </div>
            </div>
            """.formatted(nombre, premio, puntosDesc, puntosRest);
    }
}
