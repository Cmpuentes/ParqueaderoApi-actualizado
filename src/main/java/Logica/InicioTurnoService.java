/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Logica;

import Datos.InicioTurnoResponseDTO;
import Datos.TotalesMediosPagoAbonosDTO;
import Datos.TotalesMediosPagoDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 *
 * @author Carlos Mario
 */
public class InicioTurnoService {
    
    //MÉTODO QUE REALIZA CONSULTA Y QUE SE USA EN JFINTURNOP
    public InicioTurnoResponseDTO realizarConsulta(int inicioturno) throws Exception {
        String baseUrl = ConfiguradorApi.getInstance().getApiBaseUrl();
        URL url = new URL(baseUrl + "/turno/consulta/" + inicioturno);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Accept", "application/json");

        if (conn.getResponseCode() != 200) {
            throw new RuntimeException("Error HTTP : " + conn.getResponseCode());
        }

        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        ObjectMapper mapper = new ObjectMapper();
        InicioTurnoResponseDTO turno = mapper.readValue(br, InicioTurnoResponseDTO.class);

        conn.disconnect();
        return turno;
    }
    
    //FUNCIÓN QUE TRAE LOS TOTALES DE LOS MEDIOS DE PAGO, USADA EN JFINTURNOP
    public TotalesMediosPagoDTO totalmedio_pagos(int numeroTurno) throws Exception {
        String baseUrl = ConfiguradorApi.getInstance().getApiBaseUrl();
        URL url = new URL(baseUrl + "/api/salidas/escritorio/totales-medio-pago/" + numeroTurno);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Accept", "application/json");

        if (conn.getResponseCode() != 200) {
            throw new RuntimeException("Error HTTP : " + conn.getResponseCode());
        }

        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));

        ObjectMapper mapper = new ObjectMapper();
        TotalesMediosPagoDTO totales = mapper.readValue(br, TotalesMediosPagoDTO.class);

        conn.disconnect();
        return totales;
    }
    
    //FUNCIÓN QUE SE TRAE LOS TOTALES DE LOS MEDIOS DE PAGO DE ABONOS
    public TotalesMediosPagoAbonosDTO totalmedio_pagos_abonos(int numeroTurno) throws Exception {
        String baseUrl = ConfiguradorApi.getInstance().getApiBaseUrl();
        URL url = new URL(baseUrl + "/api/escritorio/abonos/totales/" + numeroTurno);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Accept", "application/json");

        if (conn.getResponseCode() != 200) {
            throw new RuntimeException("Error HTTP : " + conn.getResponseCode());
        }

        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));

        ObjectMapper mapper = new ObjectMapper();
        TotalesMediosPagoAbonosDTO totales = mapper.readValue(br, TotalesMediosPagoAbonosDTO.class);

        conn.disconnect();

        return totales;
    }
}
