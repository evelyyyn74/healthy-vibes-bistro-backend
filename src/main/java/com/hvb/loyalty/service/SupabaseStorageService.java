package com.hvb.loyalty.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;

@Service
public class SupabaseStorageService {

    @Value("${supabase.url}")
    private String supabaseUrl;

    @Value("${supabase.service-role-key}")
    private String serviceRoleKey;

    @Value("${supabase.storage.bucket}")
    private String bucket;

    private final HttpClient httpClient = HttpClient.newHttpClient();

    /**
     * Sube (o sobrescribe) un archivo en Supabase Storage y regresa su URL pública.
     * El bucket debe estar configurado como público en el dashboard de Supabase.
     *
     * @param path        ruta dentro del bucket, ej. "hero/HVBF9F19884.png"
     * @param contentBytes bytes del archivo
     * @param contentType  ej. "image/png"
     */
    public String subirArchivo(String path, byte[] contentBytes, String contentType) throws Exception {
        String uploadUrl = supabaseUrl + "/storage/v1/object/" + bucket + "/" + path;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(uploadUrl))
                .header("apikey", serviceRoleKey)
                .header("Authorization", "Bearer " + serviceRoleKey)
                .header("Content-Type", contentType)
                .header("x-upsert", "true")
                .PUT(BodyPublishers.ofByteArray(contentBytes))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200 && response.statusCode() != 201) {
            throw new RuntimeException("Error al subir a Supabase Storage (" + response.statusCode() + "): " + response.body());
        }

        return supabaseUrl + "/storage/v1/object/public/" + bucket + "/" + path;
    }
}