/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Logica;

import Datos.EmpleadoActivoDTO;
import Datos.InicioTurnoDTO;
import Datos.InicioTurnoResponseDTO;
import Datos.NumeroTurnoResponse;
import Datos.TotalesMediosPagoAbonosDTO;
import Datos.TotalesMediosPagoDTO;
import DatosP.Dinicioturnop;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import javax.swing.JOptionPane;

/**
 *
 * @author Carlos Mario
 */
public class ApiInicioTurnoService {

    //OBTENER NUMERO TURNO ACTIVO
    public int obtenerNumeroTurnoActivo() {//EN VEREMOS
        int numeroTurno = 0;

        try {
            // Usamos la URL base desde la configuración centralizada
            String baseUrl = ConfiguradorApi.getInstance().getApiBaseUrl();
            URL url = new URL(baseUrl + "/api/turno/activo"); // Solo agregamos la ruta específica

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            int code = connection.getResponseCode();
            if (code == 200) {
                try (BufferedReader reader = new BufferedReader(
                        new InputStreamReader(connection.getInputStream()))) {
                    String response = reader.readLine();
                    if (response != null && !response.trim().isEmpty()) {
                        numeroTurno = Integer.parseInt(response);
                    }
                }
            } else {
                System.out.println("Error al obtener número de turno activo. Código: " + code);
            }

        } catch (Exception e) {
            System.out.println("Excepción al obtener número de turno activo: " + e.getMessage());
        }

        return numeroTurno;
    }

    //GENERA EL NÚMERO DE TURNO EN EL INICIO DE TURNO
    public int generarnumeroApi() throws Exception {
        // URL base desde config.properties
        String baseUrl = ConfiguradorApi.getInstance().getApiBaseUrl();
        URL url = new URL(baseUrl + "/api/inicioturno/generar-numero");

        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Accept", "application/json");

        if (conn.getResponseCode() != 200) {
            throw new RuntimeException("Error en la API. Código: " + conn.getResponseCode());
        }

        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line);
        }
        br.close();
        conn.disconnect();

        // Parsear JSON con Gson
        Gson gson = new Gson();
        NumeroTurnoResponse response = gson.fromJson(sb.toString(), NumeroTurnoResponse.class);
        return response.getNumeroTurno();
    }

    //REGISTRA EL INICIO DE TURNO
    public boolean insertar(InicioTurnoDTO dts) {
        try {
            // URL base desde config.properties
            String baseUrl = ConfiguradorApi.getInstance().getApiBaseUrl();
            URL url = new URL(baseUrl + "/api/inicioturno/registrar");

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json; utf-8");
            conn.setRequestProperty("Accept", "application/json");
            conn.setDoOutput(true);

            // Convertir el objeto a JSON
            Gson gson = new Gson();
            String jsonInput = gson.toJson(dts);

            // Enviar el JSON
            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = jsonInput.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            // Verificar código de respuesta
            int code = conn.getResponseCode();
            if (code == HttpURLConnection.HTTP_OK) {
                System.out.println("Inicio de turno registrado con éxito");
                return true;
            } else {
                System.out.println("Error en API, código: " + code);
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean hayTurnoActivo() {
        try {
            String baseUrl = ConfiguradorApi.getInstance().getApiBaseUrl();
            URL url = new URL(baseUrl + "/api/inicioturno/turno/activo");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");

            if (conn.getResponseCode() != 200) {
                throw new RuntimeException("Error HTTP: " + conn.getResponseCode());
            }

            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String result = br.readLine(); // será "true" o "false"
            conn.disconnect();

            return Boolean.parseBoolean(result);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al verificar el estado del turno: " + e.getMessage());
            return false;
        }
    }

    //MÉTODO QUE REALIZA CONSULTA Y QUE SE USA EN JFINTURNOP
    public InicioTurnoResponseDTO realizarConsulta(int inicioturno) throws Exception {
        String baseUrl = ConfiguradorApi.getInstance().getApiBaseUrl();
        URL url = new URL(baseUrl + "/api/inicioturno/consulta/" + inicioturno);
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

    public int numeroturno() throws Exception {
        String baseUrl = ConfiguradorApi.getInstance().getApiBaseUrl();
        URL url = new URL(baseUrl + "/api/inicioturno/ultimo-turno-activo");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Accept", "application/json");

        if (conn.getResponseCode() != 200) {
            throw new RuntimeException("Error HTTP : " + conn.getResponseCode());
        }

        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));

        ObjectMapper mapper = new ObjectMapper();
        NumeroTurnoResponse dto = mapper.readValue(br, NumeroTurnoResponse.class);

        conn.disconnect();
        return dto.getNumeroTurno();
    }

//FUNCIÓN QUE TRAE LOS TOTALES DE LOS MEDIOS DE PAGO, USADA EN JFINTURNOP
    public TotalesMediosPagoDTO totalmedio_pagos(int numeroTurno) throws Exception {
        String baseUrl = ConfiguradorApi.getInstance().getApiBaseUrl();
        URL url = new URL(baseUrl + "/api/salidas/totales-medio-pago/" + numeroTurno);
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
        URL url = new URL(baseUrl + "/api/abonos/totales/" + numeroTurno);
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

    //FUNCIÓN CONSULTA EMPLEADO USADA EN JFINTURNOP
    public String consultaEmpleado(Long idTurno) throws Exception {
    String baseUrl = ConfiguradorApi.getInstance().getApiBaseUrl();
    URL url = new URL(baseUrl + "/api/inicioturno/empleado-activo/" + idTurno);

    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
    conn.setRequestMethod("GET");
    conn.setRequestProperty("Accept", "application/json");

    if (conn.getResponseCode() != 200) {
        throw new RuntimeException("Error HTTP : " + conn.getResponseCode());
    }

    BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));

    ObjectMapper mapper = new ObjectMapper();
    EmpleadoActivoDTO empleadoDTO = mapper.readValue(br, EmpleadoActivoDTO.class);

    conn.disconnect();

    return empleadoDTO.getEmpleado();
}


}
