package com.hvb.loyalty.service;

import org.springframework.stereotype.Component;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import javax.imageio.ImageIO;

/**
 * Genera la imagen "hero" (banner) del pase de Google Wallet.
 * El campo nativo "Puntos" de Google ahora se usa para "Recompensas obtenidas",
 * así que esta imagen se enfoca 100% en: Titular + badge (compacto arriba)
 * y las tazas de progreso (grandes, centradas, elemento principal).
 */
@Component
public class HeroImageGenerator {

    // Google recomienda 1032x336 (ratio ~3:1) para heroImage
    private static final int WIDTH = 1032;
    private static final int HEIGHT = 336;
    private static final int TOTAL_TAZAS = 10;

    private static final Color DORADO = new Color(0xD4, 0xA8, 0x43);
    private static final Color CREMA = new Color(0xF5, 0xF0, 0xE8);
    private static final Color LABEL_MUTED = new Color(0x8F, 0xA8, 0x8F);
    private static final Color TAZA_VACIA = new Color(0x4A, 0x55, 0x42);
    private static final Color ICONO_LLENO = new Color(0x1E, 0x2B, 0x1F);

    /**
     * @param puntosActuales puntos acumulados en el ciclo actual (0 a 10)
     * @param nombreCliente  nombre del titular
     * @param estatus        texto del badge (ej. "Frecuente", "Nuevo")
     */
    public byte[] generarImagenProgreso(int puntosActuales, String nombreCliente, String estatus) throws Exception {
        int llenas = Math.max(0, Math.min(puntosActuales, TOTAL_TAZAS));

        BufferedImage img = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = img.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        // Fondo transparente: se integra al verde nativo de la tarjeta

        int margenX = 48;

        // --- Fila compacta superior: TITULAR + badge de estatus ---
        g.setColor(LABEL_MUTED);
        g.setFont(new Font("SansSerif", Font.PLAIN, 18));
        g.drawString("TITULAR", margenX, 34);

        g.setColor(CREMA);
        g.setFont(new Font("SansSerif", Font.BOLD, 38));
        g.drawString(nombreCliente != null ? nombreCliente : "", margenX, 72);

        String textoBadge = "● " + (estatus != null ? estatus : "Nuevo");
        g.setFont(new Font("SansSerif", Font.BOLD, 20));
        FontMetrics fmBadge = g.getFontMetrics();
        int badgeAncho = fmBadge.stringWidth(textoBadge) + 36;
        int badgeAlto = 40;
        int badgeX = WIDTH - margenX - badgeAncho;
        int badgeY = 20;
        g.setColor(DORADO);
        g.setStroke(new BasicStroke(2.5f));
        g.draw(new RoundRectangle2D.Double(badgeX, badgeY, badgeAncho, badgeAlto, badgeAlto, badgeAlto));
        g.drawString(textoBadge, badgeX + 18, badgeY + 27);

        // --- Línea divisoria ---
        g.setColor(new Color(255, 255, 255, 25));
        g.drawLine(margenX, 100, WIDTH - margenX, 100);

        // --- Texto de progreso, centrado ---
        g.setColor(LABEL_MUTED);
        g.setFont(new Font("SansSerif", Font.PLAIN, 28));
        String textoPuntos = "PUNTOS - " + llenas + " de " + TOTAL_TAZAS + " para tu próxima recompensa";
        FontMetrics fmPuntos = g.getFontMetrics();
        int textoX = (WIDTH - fmPuntos.stringWidth(textoPuntos)) / 2;
        g.drawString(textoPuntos, textoX, 144);

        // --- Tazas: grandes, centradas, elemento principal ---
        int diametro = 84;
        int espacio = 20;
        int totalAncho = TOTAL_TAZAS * diametro + (TOTAL_TAZAS - 1) * espacio;
        int xInicio = (WIDTH - totalAncho) / 2;
        int y = 175;

        for (int i = 0; i < TOTAL_TAZAS; i++) {
            int x = xInicio + i * (diametro + espacio);
            dibujarTaza(g, x, y, diametro, i < llenas);
        }

        g.dispose();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(img, "png", baos);
        return baos.toByteArray();
    }

    private void dibujarTaza(Graphics2D g, int x, int y, int diametro, boolean llena) {
        Ellipse2D circulo = new Ellipse2D.Double(x, y, diametro, diametro);

        g.setColor(llena ? DORADO : TAZA_VACIA);
        g.fill(circulo);

        g.setColor(CREMA);
        g.setStroke(new BasicStroke(3f));
        g.draw(circulo);

        int cuerpoAncho = (int) (diametro * 0.42);
        int cuerpoAlto = (int) (diametro * 0.32);
        int cuerpoX = x + (diametro - cuerpoAncho) / 2 - 4;
        int cuerpoY = y + (int) (diametro * 0.34);

        g.setColor(llena ? ICONO_LLENO : CREMA);
        g.fill(new RoundRectangle2D.Double(cuerpoX, cuerpoY, cuerpoAncho, cuerpoAlto, 6, 6));

        int asaDiametro = (int) (cuerpoAlto * 0.85);
        int asaX = cuerpoX + cuerpoAncho - 5;
        int asaY = cuerpoY + (cuerpoAlto - asaDiametro) / 2;
        g.setStroke(new BasicStroke(3f));
        g.setColor(llena ? ICONO_LLENO : CREMA);
        g.drawArc(asaX, asaY, asaDiametro, asaDiametro, -90, 180);
    }
}