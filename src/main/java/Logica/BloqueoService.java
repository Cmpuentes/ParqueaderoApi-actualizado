/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Logica;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 *
 * @author Carlos Mario
 */
public class BloqueoService {
    
    public boolean verificarBloqueo(String codigo) {
        try {
            
            String baseUrl = ConfiguradorApi.getInstance().getApiBaseUrl();
            URL url = new URL(baseUrl + "/api/bloqueos/escritorio/verificar/"+ codigo);
            
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");

            if (con.getResponseCode() == 200) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String respuesta = reader.readLine();
                return Boolean.parseBoolean(respuesta);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
