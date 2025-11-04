
package Logica;

import Datos.ConteoActivoDTO;
import DatosP.Dingresop;
import Datos.SalidaConsultaDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

/**
 *
 * @author Carlos Mario
 */
public class ApiIngresoService {

    //Función para GUARDAR o registrar datos de ingreso
    public String guardarIngreso(Dingresop ingreso) {
        try {
            Gson gson = new Gson();
            String json = gson.toJson(ingreso);

            //URL url = new URL(API_URL);
            
            String baseUrl = ConfiguradorApi.getInstance().getApiBaseUrl();
            URL url = new URL(baseUrl + "/api/ingresos");
            
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json; utf-8");
            con.setRequestProperty("Accept", "application/json");
            con.setDoOutput(true);

            try (OutputStream os = con.getOutputStream()) {
                byte[] input = json.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            int responseCode = con.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK || responseCode == HttpURLConnection.HTTP_CREATED) {
                try (BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), "utf-8"))) {
                    StringBuilder response = new StringBuilder();
                    String responseLine;
                    while ((responseLine = br.readLine()) != null) {
                        response.append(responseLine.trim());
                    }
                    return response.toString(); // Devuelve la respuesta del servidor
                }
            } else {
                return "Error al guardar ingreso: " + responseCode;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return "Error de conexión o servidor: " + e.getMessage();
        }
    }

    //Función para ACTUALIZR o editar registros de ingreso
    public boolean actualizarIngresoEnAPI(Dingresop ingreso) {
        try {
            //URL url = new URL("http://localhost:8080/api/ingresos/" + ingreso.getIdingreso());
            
            String baseUrl = ConfiguradorApi.getInstance().getApiBaseUrl();
            URL url = new URL(baseUrl + "/api/ingresos/"+ingreso.getIdingreso());
            
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("PUT");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            Gson gson = new Gson();
            String json = gson.toJson(ingreso);

            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = json.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            int responseCode = conn.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                return true;
            } else {
                System.err.println("Error al actualizar ingreso. Código: " + responseCode);
                return false;
            }

        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error al conectar con la API: " + e.getMessage());
            return false;
        }
    }

    public static List<String> obtenerPlacasActivas() {
        List<String> placas = new ArrayList<>();
        try {
            //URL url = new URL("http://localhost:8080/api/ingresos/placas-activas"); // Ajusta si usas otro puerto o ruta
            
            String baseUrl = ConfiguradorApi.getInstance().getApiBaseUrl();
            URL url = new URL(baseUrl + "/api/ingresos/placas-activas");
            
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");

            if (con.getResponseCode() == 200) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();

                // Convertir JSON a lista de Strings con Gson
                Gson gson = new Gson();
                Type listType = new TypeToken<List<String>>() {
                }.getType();
                placas = gson.fromJson(response.toString(), listType);
            } else {
                System.err.println("Error al obtener placas activas: " + con.getResponseCode());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return placas;
    }

    public static SalidaConsultaDTO consultarIngresoPorPlaca(String placa) {
        SalidaConsultaDTO dto = null;
        try {
            //URL url = new URL("http://localhost:8080/api/ingresos/detalle/" + placa);
            
            String baseUrl = ConfiguradorApi.getInstance().getApiBaseUrl();
            URL url = new URL(baseUrl + "/api/ingresos/detalle/"+placa);
            
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");

            if (con.getResponseCode() == 200) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();

                Gson gson = new Gson();
                dto = gson.fromJson(response.toString(), SalidaConsultaDTO.class);

            } else {
                JOptionPane.showMessageDialog(null, "Placa no encontrada o error del servidor.");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al consultar placa: " + e.getMessage());
        }
        return dto;
    }
    
    public boolean eliminarIngreso(Dingresop dts) {
        try {
            //URL url = new URL("http://localhost:8080/api/ingresos/" + dts.getIdingreso());
            
            String baseUrl = ConfiguradorApi.getInstance().getApiBaseUrl();
            URL url = new URL(baseUrl + "/api/ingresos/"+ dts.getIdingreso());
            
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("DELETE");
            con.setRequestProperty("Accept", "application/json");

            int responseCode = con.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK || responseCode == HttpURLConnection.HTTP_NO_CONTENT) {
                return true;
            } else if (responseCode == HttpURLConnection.HTTP_NOT_FOUND) {
                JOptionPane.showMessageDialog(null, "Ingreso no encontrado con ID: " + dts.getIdingreso());
            } else {
                JOptionPane.showMessageDialog(null, "Error al eliminar ingreso. Código: " + responseCode);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error de conexión al eliminar ingreso: " + e.getMessage());
        }

        return false;
    }
    
    public int contarVehiculosActivos() {
        try {
            //URL url = new URL(API_URL + "/activos/count");
            
            String baseUrl = ConfiguradorApi.getInstance().getApiBaseUrl();
            URL url = new URL(baseUrl + "/activos/count");
            
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");

            if (conn.getResponseCode() != 200) {
                throw new RuntimeException("Error HTTP: " + conn.getResponseCode());
            }

            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
            String response = br.readLine(); // El endpoint devuelve un número simple
            conn.disconnect();

            return Integer.parseInt(response);

        } catch (Exception e) {
            System.err.println("Error al contar vehículos activos: " + e.getMessage());
            return 0; // Si hay error, devolver 0 por seguridad
        }
    }
    
    //Metodo para contar vehiculos activos en parquedaro llamado en Jfinturnop
    public int contarEstadoActivo() throws Exception {
    String baseUrl = ConfiguradorApi.getInstance().getApiBaseUrl();
    URL url = new URL(baseUrl + "/api/ingresos/contar-activos");
    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
    conn.setRequestMethod("GET");
    conn.setRequestProperty("Accept", "application/json");

    if (conn.getResponseCode() != 200) {
        throw new RuntimeException("Error HTTP : " + conn.getResponseCode());
    }

    BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));

    ObjectMapper mapper = new ObjectMapper();
    ConteoActivoDTO conteo = mapper.readValue(br, ConteoActivoDTO.class);

    conn.disconnect();

    return (int) conteo.getTotalActivos(); // devolver el total directamente
}

}
