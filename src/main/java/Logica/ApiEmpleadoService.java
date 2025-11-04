package Logica;

import Datos.EmpleadoAdminDTO;
import Datos.EmpleadoAdministrativoDTO;
import Datos.EmpleadoLoginRequestDTO;
import Datos.EmpleadoResponseDTO;
import Datos.ErrorResponseDTO;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Carlos Mario
 */
public class ApiEmpleadoService {

    public Integer totalregistros = 0;

    //FUNCIÓN LOGIN PARA EL EMPLEADO
    public EmpleadoResponseDTO loginEmpleado(String login, String password) throws Exception {
        //URL url = new URL(BASE_URL + "/login");

        String baseUrl = ConfiguradorApi.getInstance().getApiBaseUrl();
        URL url = new URL(baseUrl + "/api/empleados/login");

        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json; utf-8");
        conn.setDoOutput(true);

        // Construir JSON de petición
        Gson gson = new Gson();
        EmpleadoLoginRequestDTO request = new EmpleadoLoginRequestDTO(login, password);
        String jsonInput = gson.toJson(request);

        try (OutputStream os = conn.getOutputStream()) {
            byte[] input = jsonInput.getBytes("utf-8");
            os.write(input, 0, input.length);
        }

        int statusCode = conn.getResponseCode();

        // Si login correcto
        if (statusCode == HttpURLConnection.HTTP_OK) {
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                response.append(line);
            }
            br.close();
            return gson.fromJson(response.toString(), EmpleadoResponseDTO.class);
        } // Si error (401)
        else if (statusCode == HttpURLConnection.HTTP_UNAUTHORIZED) {
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getErrorStream(), "utf-8"));
            StringBuilder errorResponse = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                errorResponse.append(line);
            }
            br.close();
            ErrorResponseDTO error = gson.fromJson(errorResponse.toString(), ErrorResponseDTO.class);
            throw new Exception(error.getMensaje());
        } else {
            throw new Exception("Error en el servidor inicio turno. Código: " + statusCode);
        }
    }

    //FUNCIÓN PARA EL LOGIN DEL ADMINISTRATIVO
//    public EmpleadoResponseDTO login(String login, String password) {
//        try {
//            String baseUrl = ConfiguradorApi.getInstance().getApiBaseUrl();
//            URL url = new URL(baseUrl + "/api/administrativos/login");
//            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//
//            conn.setRequestMethod("POST");
//            conn.setRequestProperty("Content-Type", "application/json; utf-8");
//            conn.setRequestProperty("Accept", "application/json");
//            conn.setDoOutput(true);
//
//            // Crear el JSON con Gson
//            Gson gson = new Gson();
//            EmpleadoLoginRequestDTO request = new EmpleadoLoginRequestDTO(login, password);
//            String jsonInput = gson.toJson(request);
//
//            try (OutputStream os = conn.getOutputStream()) {
//                byte[] input = jsonInput.getBytes(StandardCharsets.UTF_8);
//                os.write(input, 0, input.length);
//            }
//
//            int code = conn.getResponseCode();
//            System.out.println("Ejecutando login, response code: " + code);
//
//            switch (code) {
//                case HttpURLConnection.HTTP_OK -> {
//                    // ✅ Login exitoso
//                    try (BufferedReader br = new BufferedReader(
//                            new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
//                        StringBuilder response = new StringBuilder();
//                        String line;
//                        while ((line = br.readLine()) != null) {
//                            response.append(line.trim());
//                        }
//                        System.out.println("Respuesta exitosa del servidor: " + response);
//                        return gson.fromJson(response.toString(), EmpleadoResponseDTO.class);
//                    }
//                }
//                case HttpURLConnection.HTTP_UNAUTHORIZED -> {
//                    // ❌ Credenciales inválidas
//                    try (BufferedReader br = new BufferedReader(
//                            new InputStreamReader(conn.getErrorStream(), StandardCharsets.UTF_8))) {
//                        StringBuilder errorResponse = new StringBuilder();
//                        String line;
//                        while ((line = br.readLine()) != null) {
//                            errorResponse.append(line.trim());
//                        }
//                        System.out.println("Respuesta de error del servidor: " + errorResponse);
//                        // Aquí puedes parsear a ErrorResponseDTO si quieres:
//                        // ErrorResponseDTO error = gson.fromJson(errorResponse.toString(), ErrorResponseDTO.class);
//                    }
//                    return null;
//                }
//                default -> {
//                    // ⚠️ Otros errores
//                    System.out.println("Error inesperado, código: " + code);
//                    return null;
//                }
//            }
//
//        } catch (JsonSyntaxException | IOException e) {
//            throw new RuntimeException("Error al intentar iniciar sesión: " + e.getMessage(), e);
//        }
//    }
    public boolean validarLoginAdministrador(String login, String password) {
        try {
            String baseUrl = ConfiguradorApi.getInstance().getApiBaseUrl();
            URL url = new URL(baseUrl + "/api/administrativos/validar");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json; utf-8");
            conn.setRequestProperty("Accept", "application/json");
            conn.setDoOutput(true);

            // Crear el JSON del request
            Gson gson = new Gson();
            EmpleadoLoginRequestDTO request = new EmpleadoLoginRequestDTO(login, password);
            String jsonInput = gson.toJson(request);

            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = jsonInput.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            int code = conn.getResponseCode();
            System.out.println("Response code validar: " + code);

            if (code == HttpURLConnection.HTTP_OK) {
                try (BufferedReader br = new BufferedReader(
                        new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
                    String response = br.readLine(); // será "true" o "false"
                    System.out.println("Respuesta servidor validar: " + response);
                    return Boolean.parseBoolean(response);
                }
            } else {
                System.out.println("Error al validar. Código: " + code);
                return false;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    //FUNCIÓN MOSTRARP LLAMADA EN JAVANZADO PARA LOS ADMINISTRATIVOS
    public DefaultTableModel mostrarp(String buscar) {
        String[] titulos = {"Id", "Nombres", "Apellidos", "Tipodocumento", "Documento",
            "Teléfono", "Dirección", "Email", "Pais", "Ciudad", "Acceso",
            "Login", "Password", "Estado", "Eps", "Arl"};
        DefaultTableModel modelo = new DefaultTableModel(null, titulos);

        try {
            String baseUrl = ConfiguradorApi.getInstance().getApiBaseUrl();
            String endpoint = baseUrl + "/api/empleados";

            if (buscar != null && !buscar.isEmpty()) {
                endpoint += "?documento=" + buscar;
            }

            URL url = new URL(endpoint);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");

            if (conn.getResponseCode() == 200) {
                try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"))) {
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = br.readLine()) != null) {
                        response.append(line.trim());
                    }

                    Gson gson = new Gson();
                    List<EmpleadoAdministrativoDTO> lista = gson.fromJson(
                            response.toString(), new TypeToken<List<EmpleadoAdministrativoDTO>>() {
                    }.getType()
                    );

                    for (EmpleadoAdministrativoDTO e : lista) {
                        modelo.addRow(new Object[]{
                            e.getIdempleado(),
                            e.getNombres(),
                            e.getApellidos(),
                            e.getTipodocumento(),
                            e.getDocumento(),
                            e.getTelefono(),
                            e.getDireccion(),
                            e.getEmail(),
                            e.getPais(),
                            e.getCiudad(),
                            e.getAcceso(),
                            e.getLogin(),
                            e.getPassword(),
                            e.getEstado(),
                            e.getEps(),
                            e.getArl()
                        });
                        totalregistros++;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return modelo;
    }

    //FUNCIÓN PARA HACER EL REGISTRO DELELMPLEASO EN EL MODULO ADMINISTRADOR
    public boolean insertar(EmpleadoAdminDTO empleado) {
        try {
            String baseUrl = ConfiguradorApi.getInstance().getApiBaseUrl();
            URL url = new URL(baseUrl + "/api/empleados");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json; utf-8");
            conn.setRequestProperty("Accept", "application/json");
            conn.setDoOutput(true);

            Gson gson = new Gson();
            String jsonInput = gson.toJson(empleado);

            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = jsonInput.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            int code = conn.getResponseCode();
            System.out.println("Response code insertarEmpleado: " + code);

            if (code == HttpURLConnection.HTTP_OK || code == HttpURLConnection.HTTP_CREATED) {
                try (BufferedReader br = new BufferedReader(
                        new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = br.readLine()) != null) {
                        response.append(line.trim());
                    }
                    System.out.println("Respuesta servidor: " + response);
                    return true;
                }
            } else {
                System.out.println("Error al insertar empleado. Código: " + code);
                return false;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    //FUNCIÓN PARA EDITAR EL EMPLEADO DESDE EL MOSULO DE ADMINISTRADOR
    public boolean editar(Long id, EmpleadoAdminDTO dto) {
        try {
            String baseUrl = ConfiguradorApi.getInstance().getApiBaseUrl();
            URL url = new URL(baseUrl + "/api/empleados/" + id);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("PUT");
            conn.setRequestProperty("Content-Type", "application/json; utf-8");
            conn.setRequestProperty("Accept", "application/json");
            conn.setDoOutput(true);

            Gson gson = new Gson();
            String jsonInput = gson.toJson(dto);

            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = jsonInput.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            int code = conn.getResponseCode();
            System.out.println("Response code editar: " + code);

            if (code == HttpURLConnection.HTTP_OK) {
                return true;
            } else {
                System.out.println("Error al editar empleado. Código: " + code);
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    //FUNCIÓN PARA ELIMINAR EL EMPLEADO EN EL MODULO ADMINISTRATIVO
    public boolean eliminar(Long id) {
        try {
            String baseUrl = ConfiguradorApi.getInstance().getApiBaseUrl();
            URL url = new URL(baseUrl + "/api/empleados/" + id);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("DELETE");
            conn.setRequestProperty("Accept", "application/json");

            int code = conn.getResponseCode();
            System.out.println("Response code eliminar: " + code);

            if (code == HttpURLConnection.HTTP_NO_CONTENT) {
                return true;
            } else {
                System.out.println("Error al eliminar empleado. Código: " + code);
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}
