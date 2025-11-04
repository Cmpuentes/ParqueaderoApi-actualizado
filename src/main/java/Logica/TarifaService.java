/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Logica;

import Datos.TarifasDTO;
import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 *
 * @author Carlos Mario
 */
public class TarifaService {

    //FUNCIÓN PARA OBTENER LAS TARIFAS DE LOS LOS TIÓS DE SERVICIO
    public static TarifasDTO obtenerTarifa(String tipoVehiculo, String tipoServicio) {
        try {
            // Usamos la URL base desde la configuración centralizada
            String baseUrl = ConfiguradorApi.getInstance().getApiBaseUrl();
            String endpoint = "/api/tarifas/escritorio"; // Ruta específica de este servicio

            // Construimos la URL con parámetros
            String urlString = String.format("%s%s?tipovehiculo=%s&tiposervicio=%s",
                    baseUrl,
                    endpoint,
                    URLEncoder.encode(tipoVehiculo, StandardCharsets.UTF_8),
                    URLEncoder.encode(tipoServicio, StandardCharsets.UTF_8)
            );

            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            if (conn.getResponseCode() == 200) {
                InputStreamReader reader = new InputStreamReader(conn.getInputStream());
                return new Gson().fromJson(reader, TarifasDTO.class);
            } else {
                System.err.println("Error al obtener tarifa: " + conn.getResponseCode());
            }

        } catch (IOException e) {
            System.err.println("Excepción al obtener tarifa: " + e.getMessage());
            e.printStackTrace();
        }

        return null;
    }

}
