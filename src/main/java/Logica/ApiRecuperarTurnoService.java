/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Logica;

import Datos.TurnoActivoDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 *
 * @author Carlos Mario
 */
public class ApiRecuperarTurnoService {

//    public static TurnoActivoDTO numeroturnop() throws Exception {
//        String baseUrl = ConfiguradorApi.getInstance().getApiBaseUrl();
//        URL url = new URL(baseUrl + "/api/inicioturno/activo-p");
//        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//        conn.setRequestMethod("GET");
//        conn.setRequestProperty("Accept", "application/json");
//
//        if (conn.getResponseCode() != 200) {
//            throw new RuntimeException("Error HTTP : " + conn.getResponseCode());
//        }
//
//        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
//
//        ObjectMapper mapper = new ObjectMapper();
//        TurnoActivoDTO turnoActivo = mapper.readValue(br, TurnoActivoDTO.class);
//
//        conn.disconnect();
//        return turnoActivo;
//    }

}
