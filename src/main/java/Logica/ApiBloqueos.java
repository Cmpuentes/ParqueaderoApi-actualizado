/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Logica;

import DatosP.Dbloqueos;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Carlos Mario
 */
public class ApiBloqueos {
    
//    private final String API_URL = "http://localhost:8080/api/bloqueos";
    private final Gson gson = new Gson();
    
    // ✅ Verificar si un código está bloqueado
    public boolean verificarBloqueo(String codigo) {
        try {
            
            String baseUrl = ConfiguradorApi.getInstance().getApiBaseUrl();
            URL url = new URL(baseUrl + "/api/bloqueos/verificar/"+ codigo);
            
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
    
    // ✅ Insertar nuevo bloqueo
    public boolean insertar(Dbloqueos dto) {
        try {
            String baseUrl = ConfiguradorApi.getInstance().getApiBaseUrl();
            URL url = new URL(baseUrl + "/api/bloqueos");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json");
            con.setDoOutput(true);

            String json = gson.toJson(dto);
            OutputStream os = con.getOutputStream();
            os.write(json.getBytes());
            os.flush();

            return con.getResponseCode() == 200 || con.getResponseCode() == 201;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // ✅ Editar bloqueo existente
    public boolean editar(Long id, Dbloqueos dto) {
        try {
            //URL url = new URL(API_URL + "/" + id);
            
            String baseUrl = ConfiguradorApi.getInstance().getApiBaseUrl();
            URL url = new URL(baseUrl + "/api/bloqueos/"+id);
            
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("PUT");
            con.setRequestProperty("Content-Type", "application/json");
            con.setDoOutput(true);

            String json = gson.toJson(dto);
            OutputStream os = con.getOutputStream();
            os.write(json.getBytes());
            os.flush();

            return con.getResponseCode() == 200;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // ✅ Eliminar bloqueo por ID
    public boolean eliminar(Long id) {
        try {
            //URL url = new URL(API_URL + "/" + id);
            
            String baseUrl = ConfiguradorApi.getInstance().getApiBaseUrl();
            URL url = new URL(baseUrl + "/api/bloqueos/"+ id);
            
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("DELETE");

            return con.getResponseCode() == 200;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // ✅ Mostrar todos los bloqueos
    public List<Dbloqueos> mostrar() {
        List<Dbloqueos> lista = new ArrayList<>();

        try {
            //URL url = new URL(API_URL);
            
            String baseUrl = ConfiguradorApi.getInstance().getApiBaseUrl();
            URL url = new URL(baseUrl + "/api/bloqueos/");
            
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");

            if (con.getResponseCode() == 200) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
                StringBuilder response = new StringBuilder();
                String linea;

                while ((linea = reader.readLine()) != null) {
                    response.append(linea);
                }

                Type listType = new TypeToken<List<Dbloqueos>>() {}.getType();
                lista = gson.fromJson(response.toString(), listType);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return lista;
    }
    
    // ✅ Buscar bloqueos por código (parcial o exacto)
    public List<Dbloqueos> buscarPorCodigo(String codigo) {
        List<Dbloqueos> lista = new ArrayList<>();

        try {
            //URL url = new URL(API_URL + "/buscar/" + codigo);
            
            String baseUrl = ConfiguradorApi.getInstance().getApiBaseUrl();
            URL url = new URL(baseUrl + "/api/bloqueos/buscar"+ codigo);
            
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");

            if (con.getResponseCode() == 200) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
                StringBuilder response = new StringBuilder();
                String linea;

                while ((linea = reader.readLine()) != null) {
                    response.append(linea);
                }

                Type listType = new TypeToken<List<Dbloqueos>>() {}.getType();
                lista = gson.fromJson(response.toString(), listType);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return lista;
    }
}
    

