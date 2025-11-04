package Logica;

import Datos.ApiResponse;
import Datos.FinalizarTurnoResponse;
import DatosP.Dinicioturnop;
import Datos.SalidaTurnoRequest;
import Datos.TotalesMediosPagoDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

/**
 *
 * @author Carlos Mario
 */
public class ApiFinTurnoService {

    //OBTENER EMPLEADO ACTIVO
//    public String obtenerEmpleadoActivo(int idturno) {
//        try {
//            System.out.println("Buscando empleado para turno con id: " + idturno);
//
//            //URL url = new URL("/empleado-activo" + idturno);
//            String baseUrl = ConfiguradorApi.getInstance().getApiBaseUrl();
//            URL url = new URL(baseUrl + "/api/turno/empleado-activo/" + idturno);
//            
//            //_____
//            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//            conn.setRequestMethod("GET");
//            conn.setRequestProperty("Accept", "application/json");
//
//            if (conn.getResponseCode() == 404) {
//                System.out.println("No se encontró empleado para este turno activo");
//                return null;
//            }
//
//            if (conn.getResponseCode() != 200) {
//                throw new RuntimeException("Error HTTP: " + conn.getResponseCode());
//            }
//
//            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
//            StringBuilder json = new StringBuilder();
//            String line;
//            while ((line = br.readLine()) != null) {
//                json.append(line);
//            }
//            conn.disconnect();
//            
//            System.out.println("Respuesta cruda: " + json);
//
//            // Adaptado a nuevo formato JSON
//            Map<String, String> map = new Gson().fromJson(json.toString(), Map.class);
//            return map.get("empleado");
//
//        } catch (Exception e) {
//            System.err.println("Error obteniendo empleado activo: " + e.getMessage());
//            return null;
//        }
//    }
//    
//    public TotalesMediosPagoDTO obtenerTotalesMediosPago(int numeroTurno) {
//    try {
//        //URL url = new URL("http://localhost:8080/api/salidas/totales-medios-pago/" + numeroTurno);
//        
//        String baseUrl = ConfiguradorApi.getInstance().getApiBaseUrl();
//        URL url = new URL(baseUrl + "/api/salidas/totales-medios-pago/"+numeroTurno);
//        
//        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//        conn.setRequestMethod("GET");
//        conn.setRequestProperty("Accept", "application/json");
//
//        if (conn.getResponseCode() != 200) {
//            throw new RuntimeException("Error HTTP: " + conn.getResponseCode());
//        }
//
//        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
//        StringBuilder json = new StringBuilder();
//        String line;
//        while ((line = br.readLine()) != null) {
//            json.append(line);
//        }
//        conn.disconnect();
//
//        Gson gson = new Gson();
//        return gson.fromJson(json.toString(), TotalesMediosPagoDTO.class);
//
//    } catch (Exception e) {
//        System.err.println("Error obteniendo totales medios de pago: " + e.getMessage());
//        return null;
//    }
//}
//    public boolean finalizarTurno(Dinicioturnop turnoDTO) {
//        try {
//            int numeroTurno = turnoDTO.getNumeroturno();
//            //URL url = new URL("http://localhost:8080/api/turno/finalizar/" + numeroTurno);
//            
//            String baseUrl = ConfiguradorApi.getInstance().getApiBaseUrl();
//            URL url = new URL(baseUrl + "/api/turno/finalizar/"+numeroTurno);
//            
//            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//            conn.setRequestMethod("PUT");
//            conn.setRequestProperty("Accept", "application/json");
//            conn.setDoOutput(true); // necesario para PUT aunque no enviemos body
//
//            // Si necesitas enviar datos en el body (aquí no, pero se deja preparado)
//            try (OutputStream os = conn.getOutputStream()) {
//                os.write(new byte[0]);
//            }
//
//            int responseCode = conn.getResponseCode();
//            conn.disconnect();
//
//            return responseCode == 200; // 200 = OK
//
//        } catch (Exception e) {
//            System.err.println("Error finalizando turno: " + e.getMessage());
//            return false;
//        }
//    }
    public boolean registrarSalidaTurno(SalidaTurnoRequest dto) {
        Gson gson = new Gson();
        String jsonInput = gson.toJson(dto);

        try {
            //URL url = new URL("http://localhost:8080/salida_turno/registro");

            String baseUrl = ConfiguradorApi.getInstance().getApiBaseUrl();
            URL url = new URL(baseUrl + "/salida_turno/insertar");

            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json");
            con.setDoOutput(true);

            // Enviar JSON
            try (OutputStream os = con.getOutputStream()) {
                byte[] input = jsonInput.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            // Leer respuesta
            int status = con.getResponseCode();
            InputStream is = (status < 400) ? con.getInputStream() : con.getErrorStream();
            try (BufferedReader br = new BufferedReader(new InputStreamReader(is, "utf-8"))) {
                StringBuilder response = new StringBuilder();
                String responseLine;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }

                // Aquí podrías parsear la respuesta a ApiResponse si quieres
                System.out.println("Respuesta del servidor: " + response);

                // Si quieres evaluar el campo success del JSON:
                ApiResponse apiResponse = gson.fromJson(response.toString(), ApiResponse.class);
                return apiResponse.isSuccess();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    //FUNCIÓN PARA INSERTAR EN LA TABLA SALIDATURNO LLAMADA DESDE JFINTURNOP
    public boolean insertar(SalidaTurnoRequest dto) throws Exception {
        String baseUrl = ConfiguradorApi.getInstance().getApiBaseUrl();
        URL url = new URL(baseUrl + "/salida_turno/escritorio/insertar");

        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setDoOutput(true);

        ObjectMapper mapper = new ObjectMapper();
        String jsonInput = mapper.writeValueAsString(dto);

        try (OutputStream os = conn.getOutputStream()) {
            byte[] input = jsonInput.getBytes("utf-8");
            os.write(input, 0, input.length);
        }

        if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
            throw new RuntimeException("Error HTTP : " + conn.getResponseCode());
        }

        conn.disconnect();
        return true;
    }

    //FUNCIÓN PARA FINALIZAR TURNO LLAMADA EN JFINTURNO
    public boolean finalizarturno(int numeroTurno) {
        String baseUrl = ConfiguradorApi.getInstance().getApiBaseUrl();
        String endpoint = baseUrl + "/api/escritorio/inicioturno/finalizar/" + numeroTurno;

        try {
            URL url = new URL(endpoint);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("PUT");
            conn.setRequestProperty("Accept", "application/json");

            int responseCode = conn.getResponseCode();

            if (responseCode == 200) {
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
                    Gson gson = new Gson();
                    FinalizarTurnoResponse response = gson.fromJson(reader, FinalizarTurnoResponse.class);

                    System.out.println("✅ " + response.getMensaje());
                    return response.isTurnoActivo(); // false = turno cerrado
                }
            } else {
                System.err.println("⚠️ Error: Código HTTP de finalizar turno " + responseCode);
                return true; // se mantiene activo si hubo error
            }
        } catch (Exception e) {
            e.printStackTrace();
            return true; // si ocurre excepción, no desactivamos botones
        }
    }

}
