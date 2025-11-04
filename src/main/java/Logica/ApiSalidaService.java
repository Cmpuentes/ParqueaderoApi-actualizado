package Logica;

import DatosP.Dinicioturnop;
import DatosP.Dsalidap;
import Datos.FinTurnoDTO;
import Datos.TotalesSalidaDTO;
import ReporteP.SalidaTurnoReporteDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.swing.JOptionPane;

/**
 *
 * @author Carlos Mario
 */
public class ApiSalidaService {

    // Registrar salida
    public boolean registrarSalida(Dsalidap salida) {
        try {
            //URL url = new URL(BASE_URL + "/registrar");

            String baseUrl = ConfiguradorApi.getInstance().getApiBaseUrl();
            URL url = new URL(baseUrl + "/api/salidas/registrar");

            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json");
            con.setDoOutput(true);

            // Convertir salida a JSON
            Gson gson = new Gson();
            String json = gson.toJson(salida);

            // Enviar JSON
            try (OutputStream os = con.getOutputStream()) {
                byte[] input = json.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            int responseCode = con.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK || responseCode == HttpURLConnection.HTTP_CREATED) {
                return true;
            } else {
                InputStream errorStream = con.getErrorStream();
                if (errorStream != null) {
                    String errorMsg = new BufferedReader(new InputStreamReader(errorStream))
                            .lines().collect(Collectors.joining("\n"));
                    JOptionPane.showMessageDialog(null, "Error al registrar salida: " + errorMsg);
                } else {
                    JOptionPane.showMessageDialog(null, "Error al registrar salida. Código: " + responseCode);
                }
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error de conexión al registrar salida: " + e.getMessage());
        }

        return false;
    }

    public List<Dsalidap> obtenerListaSalidas() {
        try {

            String baseUrl = ConfiguradorApi.getInstance().getApiBaseUrl();
            URL url = new URL(baseUrl + "/api/salidas");

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");

            if (conn.getResponseCode() == 200) {
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder json = new StringBuilder();
                String line;
                while ((line = in.readLine()) != null) {
                    json.append(line);
                }
                in.close();

                Gson gson = new Gson();
                Type listType = new TypeToken<List<Dsalidap>>() {
                }.getType();
                return gson.fromJson(json.toString(), listType);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public boolean editarSalida(int idSalida, String efectivoslda, String tarjetaslda, String transferenciaslda, String totalslda) {
        try {

            int efectivo = efectivoslda.isEmpty() ? 0 : Integer.parseInt(efectivoslda);
            int tarjeta = tarjetaslda.isEmpty() ? 0 : Integer.parseInt(tarjetaslda);
            int transferencia = transferenciaslda.isEmpty() ? 0 : Integer.parseInt(transferenciaslda);
            int total = totalslda.isEmpty() ? 0 : Integer.parseInt(totalslda);

            // Construimos el JSON manualmente o usando una librería si prefieres
            String jsonInputString = String.format(
                    "{\"efectivo\": %s, \"tarjeta\": %s, \"transferencia\": %s, \"total\": %s}",
                    efectivo, tarjeta, transferencia, total
            );

            String baseUrl = ConfiguradorApi.getInstance().getApiBaseUrl();
            URL url = new URL(baseUrl + "/api/salidas/" + idSalida);

            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("PUT");
            con.setRequestProperty("Content-Type", "application/json");
            con.setDoOutput(true);

            // Escribimos el body
            try (OutputStream os = con.getOutputStream()) {
                byte[] input = jsonInputString.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            int code = con.getResponseCode();
            System.out.println("idsalida: " + idSalida);
            System.out.println("Código de respuesta: " + code);

            return (code == 200 || code == 204); // 200 OK o 204 No Content (si no devuelves body)

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static TotalesSalidaDTO obtenerTotalesSalida() throws Exception {
        String baseUrl = ConfiguradorApi.getInstance().getApiBaseUrl();
        URL url = new URL(baseUrl + "/api/salidas/totales-calculados");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Accept", "application/json");

        if (conn.getResponseCode() != 200) {
            throw new RuntimeException("Error HTTP : " + conn.getResponseCode());
        }

        BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

        ObjectMapper mapper = new ObjectMapper();
        TotalesSalidaDTO totales = mapper.readValue(br, TotalesSalidaDTO.class);

        conn.disconnect();
        return totales;
    }

    public Dinicioturnop obtenerUltimoTurnoActivo() {
        try {

            String baseUrl = ConfiguradorApi.getInstance().getApiBaseUrl();
            URL url = new URL(baseUrl + "/api/turno/numero/activo");

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");

            if (conn.getResponseCode() == 404) {
                System.out.println("No hay turno activo en este momento");
                return null;
            }

            if (conn.getResponseCode() != 200) {
                throw new RuntimeException("Error HTTP: " + conn.getResponseCode());
            }

            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
            StringBuilder json = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                json.append(line);
            }

            conn.disconnect();

            com.google.gson.Gson gson = new com.google.gson.Gson();
            return gson.fromJson(json.toString(), Dinicioturnop.class);

        } catch (Exception e) {
            System.err.println("Error obteniendo turno activo: " + e.getMessage());
            return null;
        }
    }

    public FinTurnoDTO obtenerTurnoPorNumero(int numeroTurno) {
        try {

            String baseUrl = ConfiguradorApi.getInstance().getApiBaseUrl();
            URL url = new URL(baseUrl + "/api/turno/numero/" + numeroTurno);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");

            if (conn.getResponseCode() == 404) {
                System.out.println("No se encontró un turno con ese número");
                return null;
            }

            if (conn.getResponseCode() != 200) {
                throw new RuntimeException("Error HTTP: " + conn.getResponseCode());
            }

            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
            StringBuilder json = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                json.append(line);
            }
            conn.disconnect();

            com.google.gson.Gson gson = new com.google.gson.Gson();
            return gson.fromJson(json.toString(), FinTurnoDTO.class);

        } catch (Exception e) {
            System.err.println("Error obteniendo turno por número: " + e.getMessage());
            return null;
        }
    }

    public SalidaTurnoReporteDTO obtenerCierreDesdeAPI() throws Exception {
//    URL url = new URL("http://192.168.56.1:3000/salida_turno/escritorio/cierre-turno/ultimo");
        String baseUrl = ConfiguradorApi.getInstance().getApiBaseUrl();
        URL url = new URL(baseUrl + "/salida_turno/escritorio/cierre-turno/ultimo");

        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Accept", "application/json");

        if (conn.getResponseCode() != 200) {
            throw new RuntimeException("Error en API: " + conn.getResponseCode());
        }

        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        StringBuilder sb = new StringBuilder();
        String linea;
        while ((linea = br.readLine()) != null) {
            sb.append(linea);
        }

        conn.disconnect();

        // Convertir JSON → DTO usando Gson
        Gson gson = new Gson();
        return gson.fromJson(sb.toString(), SalidaTurnoReporteDTO.class);
    }


        public List<SalidaTurnoReporteDTO> getCopiaCierreTurno(String Nturno) throws IOException {

            String baseUrl = ConfiguradorApi.getInstance().getApiBaseUrl();
            URL url = new URL(baseUrl + "/salida_turno/escritorio/copia-cierre/" + Nturno);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");

            if (conn.getResponseCode() != 200) {
                return new ArrayList<>();
            }

            BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
            String output;
            StringBuilder jsonResponse = new StringBuilder();

            while ((output = br.readLine()) != null) {
                jsonResponse.append(output);
            }

            conn.disconnect();

            // Convertir JSON a lista de DTOs usando Gson
            Gson gson = new Gson();
            Type listType = new TypeToken<List<SalidaTurnoReporteDTO>>() {
            }.getType();

            return gson.fromJson(jsonResponse.toString(), listType);
        }
    

}
