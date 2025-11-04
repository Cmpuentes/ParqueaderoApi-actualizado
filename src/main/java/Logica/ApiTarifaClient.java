
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
public class ApiTarifaClient {

//    private static final String BASE_URL = "http://localhost:8080/api/tarifas";
//
//    public static TarifasDTO obtenerTarifa(String tipoVehiculo, String tipoServicio) {
//        try {
//            String query = String.format("%s?tipovehiculo=%s&tiposervicio=%s",
//                    BASE_URL,
//                    URLEncoder.encode(tipoVehiculo, StandardCharsets.UTF_8),
//                    URLEncoder.encode(tipoServicio, StandardCharsets.UTF_8)
//            );
//
//            URL url = new URL(query);
//            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//            conn.setRequestMethod("GET");
//
//            if (conn.getResponseCode() == 200) {
//                InputStreamReader reader = new InputStreamReader(conn.getInputStream());
//                return new Gson().fromJson(reader, TarifasDTO.class);
//            } else {
//                System.err.println("Error al obtener tarifa: " + conn.getResponseCode());
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        return null;
//    }
    
    public static TarifasDTO obtenerTarifa(String tipoVehiculo, String tipoServicio) {
    try {
        // Usamos la URL base desde la configuración centralizada
        String baseUrl = ConfiguradorApi.getInstance().getApiBaseUrl();
        String endpoint = "/api/tarifas"; // Ruta específica de este servicio

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

//    public static Integer obtenerTarifaPrepago(String placa, String tipoVehiculo, String tipoServicio) {
//        try {
//            String endpoint = "http://localhost:8080/api/clientes/tarifa-prepago?placa=" + URLEncoder.encode(placa, "UTF-8")
//                    + "&tipoVehiculo=" + URLEncoder.encode(tipoVehiculo, "UTF-8")
//                    + "&tipoServicio=" + URLEncoder.encode(tipoServicio, "UTF-8");
//
//            HttpURLConnection con = (HttpURLConnection) new URL(endpoint).openConnection();
//            con.setRequestMethod("GET");
//
//            if (con.getResponseCode() == 200) {
//                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
//                String linea = in.readLine();
//                in.close();
//                return Integer.parseInt(linea); // Se espera que el back devuelva solo el número (ej: 3000)
//            } else {
//                System.err.println("Error al consultar tarifa prepago: " + con.getResponseCode());
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
    
    public static Integer obtenerTarifaPrepago(String placa, String tipoVehiculo, String tipoServicio) {
    try {
        // Usamos la URL base desde la configuración centralizada
        String baseUrl = ConfiguradorApi.getInstance().getApiBaseUrl();
        String ruta = "/api/clientes/tarifa-prepago";

        // Construimos la URL con parámetros
        String urlString = String.format("%s%s?placa=%s&tipoVehiculo=%s&tipoServicio=%s",
                baseUrl,
                ruta,
                URLEncoder.encode(placa, StandardCharsets.UTF_8),
                URLEncoder.encode(tipoVehiculo, StandardCharsets.UTF_8),
                URLEncoder.encode(tipoServicio, StandardCharsets.UTF_8)
        );

        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");

        if (conn.getResponseCode() == 200) {
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
                String linea = reader.readLine();
                return Integer.parseInt(linea); // Se espera un número (ej: 3000)
            }
        } else {
            System.err.println("Error al consultar tarifa prepago: " + conn.getResponseCode());
        }

    } catch (Exception e) {
        System.err.println("Excepción al consultar tarifa prepago: " + e.getMessage());
        e.printStackTrace();
    }

    return null;
}

}
