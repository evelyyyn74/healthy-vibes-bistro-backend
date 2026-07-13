package com.hvb.loyalty.service;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.json.gson.GsonFactory;
import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.api.services.walletobjects.Walletobjects;
import com.google.api.services.walletobjects.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.Arrays;

import io.jsonwebtoken.Jwts;

import java.security.interfaces.RSAPrivateKey;
import java.util.HashMap;
import java.util.Map;

@Service
public class GoogleWalletService {

    @Value("${google.wallet.issuer-id}")
    private String issuerId;

    @Autowired
    private HeroImageGenerator heroImageGenerator;

    @Autowired
    private SupabaseStorageService supabaseStorageService;

    private static final String CLASS_SUFFIX = "hvb_loyalty_class";

    private Walletobjects service;
    private ServiceAccountCredentials credentials;

    // Inicializa la conexión con Google usando el JSON de credenciales
    private void init() throws Exception {
        if (service != null) return; // ya inicializado

        InputStream stream = new ClassPathResource("wallet-credentials.json").getInputStream();
        credentials = (ServiceAccountCredentials) ServiceAccountCredentials.fromStream(stream)
                .createScoped(Arrays.asList("https://www.googleapis.com/auth/wallet_object.issuer"));

        service = new Walletobjects.Builder(
                GoogleNetHttpTransport.newTrustedTransport(),
                GsonFactory.getDefaultInstance(),
                request -> {
                    credentials.refreshIfExpired();
                    request.getHeaders().setAuthorization("Bearer " + credentials.getAccessToken().getTokenValue());
                })
                .setApplicationName("HVB Loyalty")
                .build();
    }

    // Crea la plantilla (Class) si no existe. Se llama una vez.
    public void crearClaseSiNoExiste() throws Exception {
        init();
        String classId = issuerId + "." + CLASS_SUFFIX;

        try {
            service.loyaltyclass().get(classId).execute();
            System.out.println("[GoogleWallet] La clase " + classId + " ya existe.");
            // Ya existe, no hacemos nada
        } catch (GoogleJsonResponseException e) {
            if (e.getStatusCode() != 404) {
                // No es "no existe": es permisos, auth, cuota, etc. NO lo tragamos.
                System.err.println("[GoogleWallet] Error real al consultar la clase (" + e.getStatusCode() + "): " + e.getDetails());
                throw e;
            }

            // 404 real: no existe, la creamos
            System.out.println("[GoogleWallet] La clase no existe, creando: " + classId);
            LoyaltyClass loyaltyClass = new LoyaltyClass()
                    .setId(classId)
                    .setIssuerName("Healthy Vibes Bistro")
                    .setProgramName("Programa de Lealtad")
                    .setReviewStatus("UNDER_REVIEW")
                    .setHexBackgroundColor("#1E2B1F")
                    .setProgramLogo(new Image()
                            .setSourceUri(new ImageUri()
                                    .setUri("https://i.imgur.com/8G3ReJt.png"))
                            .setContentDescription(new LocalizedString()
                                    .setDefaultValue(new TranslatedString()
                                            .setLanguage("es")
                                            .setValue("Logo Healthy Vibes Bistro"))));

            try {
                service.loyaltyclass().insert(loyaltyClass).execute();
                System.out.println("[GoogleWallet] Clase creada exitosamente: " + classId);
            } catch (GoogleJsonResponseException insertEx) {
                System.err.println("[GoogleWallet] Error al CREAR la clase (" + insertEx.getStatusCode() + "): " + insertEx.getDetails());
                throw insertEx;
            }
        }
    }

    // Genera el link "Add to Google Wallet" para un cliente
    public String generarLinkParaCliente(String codigoQr, String nombreCliente, int puntos,
                                         String nivelNombre, int recompensasObtenidas) throws Exception {
        init();
        crearClaseSiNoExiste();

        String classId = issuerId + "." + CLASS_SUFFIX;
        // El objeto debe tener un id único por cliente (usamos su código QR, sin caracteres raros)
        String objectId = issuerId + "." + codigoQr.replaceAll("[^a-zA-Z0-9]", "");

        // Genera la imagen (titular, badge y tazas) y la sube a Supabase Storage
        String heroImageUrl = subirImagenProgreso(objectId, puntos, nombreCliente, nivelNombre);

        LoyaltyObject loyaltyObject = new LoyaltyObject()
                .setId(objectId)
                .setClassId(classId)
                .setState("ACTIVE")
                .setAccountName(nombreCliente)
                .setAccountId(codigoQr)
                .setLoyaltyPoints(new LoyaltyPoints()
                        .setLabel("Recompensas obtenidas")
                        .setBalance(new LoyaltyPointsBalance().setInt(recompensasObtenidas)))
                .setBarcode(new Barcode()
                        .setType("QR_CODE")
                        .setValue(codigoQr))
                .setHeroImage(new Image()
                        .setSourceUri(new ImageUri().setUri(heroImageUrl))
                        .setContentDescription(new LocalizedString()
                                .setDefaultValue(new TranslatedString()
                                        .setLanguage("es")
                                        .setValue("Progreso de puntos"))));

        Map<String, Object> walletObjects = new HashMap<>();
        walletObjects.put("loyaltyObjects", Arrays.asList(loyaltyObject));

        long ahora = System.currentTimeMillis() / 1000;

        String jwt = Jwts.builder()
                .issuer(credentials.getClientEmail())
                .audience().single("google")
                .claim("typ", "savetowallet")
                .claim("iat", ahora)
                .claim("payload", walletObjects)
                .signWith((RSAPrivateKey) credentials.getPrivateKey())
                .compact();

        System.out.println("[GoogleWallet] classId usado: " + classId);
        System.out.println("[GoogleWallet] objectId usado: " + objectId);
        System.out.println("[GoogleWallet] issuer (service account email): " + credentials.getClientEmail());

        return "https://pay.google.com/gp/v/save/" + jwt;
    }

    /**
     * Genera la imagen (titular, badge de estatus y tazas) y la sube a Supabase Storage.
     * Agrega cache-busting al URL para forzar a Google a refrescar la imagen cada vez
     * que cambian los datos.
     */
    private String subirImagenProgreso(String objectId, int puntos, String nombreCliente, String estatus) throws Exception {
        byte[] imagenBytes = heroImageGenerator.generarImagenProgreso(puntos, nombreCliente, estatus);
        String path = "hero/" + objectId + ".png";
        String urlBase = supabaseStorageService.subirArchivo(path, imagenBytes, "image/png");
        return urlBase + "?v=" + System.currentTimeMillis();
    }

    /**
     * Actualiza los puntos (y recompensas) de un pase YA GUARDADO por el cliente en su
     * Google Wallet. Regenera la imagen de tazas, la resube, y empuja el cambio a Google
     * para que el pase se actualice automáticamente en el celular del cliente (sin que
     * tenga que volver a guardarlo). Se debe llamar cada vez que el negocio suma o resta
     * puntos/recompensas de un cliente (ej. al escanear su QR en una compra).
     */
    public void actualizarPuntosWallet(String codigoQr, int nuevosPuntos, int nuevasRecompensas,
                                       String nombreCliente, String estatus) throws Exception {
        init();

        String objectId = issuerId + "." + codigoQr.replaceAll("[^a-zA-Z0-9]", "");

        LoyaltyObject existente = service.loyaltyobject().get(objectId).execute();

        String heroImageUrl = subirImagenProgreso(objectId, nuevosPuntos, nombreCliente, estatus);

        existente.setLoyaltyPoints(new LoyaltyPoints()
                .setLabel("Recompensas obtenidas")
                .setBalance(new LoyaltyPointsBalance().setInt(nuevasRecompensas)));

        existente.setHeroImage(new Image()
                .setSourceUri(new ImageUri().setUri(heroImageUrl))
                .setContentDescription(new LocalizedString()
                        .setDefaultValue(new TranslatedString()
                                .setLanguage("es")
                                .setValue("Progreso de puntos"))));

        service.loyaltyobject().patch(objectId, existente).execute();

        System.out.println("[GoogleWallet] Pase actualizado para " + objectId + " con " + nuevosPuntos + " puntos.");
    }
}