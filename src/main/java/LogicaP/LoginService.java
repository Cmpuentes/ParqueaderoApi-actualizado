/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package LogicaP;

import DatosP.LoginRequest;
import DatosP.LoginResponse;
import DatosP.SessionData;
import DatosP.SessionManager;
import DatosP.SessionStorage;
import Logica.ConfiguradorApi;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

/**
 *
 * @author Carlos Mario
 */
public class LoginService {

    private static final String BASE_URL = ConfiguradorApi.getInstance().getApiBaseUrl(); // ajusta tu URL
    private static final Gson gson = new Gson();

    // Login
    public LoginResponse login(String turno, String login, String password) throws IOException {
        URL url = new URL(BASE_URL + "/api/auth/login");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setDoOutput(true);

        System.out.println("Datos de envío: " + turno + ", " + login + ", " + password);
        // Crear LoginRequest con fecha generada
        String fecha = FechaUtil.generarFechaActual();
        System.out.println("Fecha de envío: " + fecha);
        LoginRequest request = new LoginRequest(login, password, turno, fecha);

        // Serializar a JSON
        String body = gson.toJson(request);

        System.out.println("Json: " + body.toString());

        try (OutputStream os = conn.getOutputStream()) {
            os.write(body.getBytes());
        }

        try (InputStream is = conn.getInputStream(); Reader reader = new InputStreamReader(is)) {

            LoginResponse response = gson.fromJson(reader, LoginResponse.class);

            if (response.isSuccess()) {
                SessionData sessionData = new SessionData(
                        response.getToken(),
                        response.getNombreCompleto(),
                        response.getFecha_inicio(),
                        response.getTurno(),
                        response.getNumero_turno(),
                        false
                );

                SessionManager.getInstance().setSessionData(sessionData);
                SessionStorage.saveSession(sessionData);
            }

            return response;
        }
    }

    //Check-session
    // Este método consulta /api/auth/check-session y devuelve Optional<SessionData>
    public static Optional<SessionData> checkSessionAndGetSession(String token) {
        try {
            URL url = new URL(BASE_URL + "/api/auth/check-session-full?token=" + token);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("Accept", "application/json");

            int code = con.getResponseCode();
            InputStream is = (code >= 200 && code < 300) ? con.getInputStream() : con.getErrorStream();
            try (BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }
                String resp = sb.toString();

                System.out.println("check-session-full response: " + resp);

                JsonObject json = JsonParser.parseString(resp).getAsJsonObject();
                boolean success = json.has("success") && json.get("success").getAsBoolean();
                if (!success) {
                    return Optional.empty();
                }

                String tokenResp = json.has("token") ? json.get("token").getAsString() : token;
                String nombre = json.has("nombreCompleto") ? json.get("nombreCompleto").getAsString() : "";
                String fechaInicio = json.has("fecha_inicio") ? json.get("fecha_inicio").getAsString() : "";
                String turno = json.has("turno") ? json.get("turno").getAsString() : "";
                int numeroTurno = json.has("numero_turno") ? json.get("numero_turno").getAsInt() : 0;

                SessionData session = new SessionData(
                        tokenResp,
                        nombre,
                        fechaInicio,
                        turno,
                        numeroTurno,
                        false
                );

                return Optional.of(session);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return Optional.empty();
        }
    }

    // Si prefieres la versión que solo devuelve booleano:
    public static boolean checkSession(String token) {
        return checkSessionAndGetSession(token).isPresent();
    }

}
