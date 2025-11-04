/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Logica;

import Datos.ZonaAdminDTO;
import Datos.ZonaDTO;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Carlos Mario
 */
public class ZonaService {

    public Integer totalregistros = 0;

    //FUNCIÓN PARA MOSTRAR LOS DATOS DELA ZONA, ESTO HACE PRTE DEL MODULO DE ADMINISTRATIVOS
    public DefaultTableModel mostrar(String buscar) {
        String[] titulos = {"Id", "Estado", "Numero"};
        DefaultTableModel modelo = new DefaultTableModel(null, titulos);

        try {
            String baseUrl = ConfiguradorApi.getInstance().getApiBaseUrl();
            String endpoint = baseUrl + "/api/escritorio/zonas";

            if (buscar != null && !buscar.isEmpty()) {
                endpoint += "?numero=" + buscar;
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
                    List<ZonaDTO> lista = gson.fromJson(
                            response.toString(), new TypeToken<List<ZonaDTO>>() {
                    }.getType()
                    );

                    for (ZonaDTO z : lista) {
                        modelo.addRow(new Object[]{
                            z.getIdzona(),
                            z.getEstado(),
                            z.getNumero()
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

    //FUNCIÓN PARA HACER EL REGISTRO DE LA ZONA EN EL MODULO DE ADMINISTRATIVOS
    public boolean insertar(ZonaAdminDTO dts) {
        try {
            String baseUrl = ConfiguradorApi.getInstance().getApiBaseUrl();
            URL url = new URL(baseUrl + "/api/escritorio/zonas");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json; utf-8");
            conn.setRequestProperty("Accept", "application/json");
            conn.setDoOutput(true);

            Gson gson = new Gson();
            String jsonInput = gson.toJson(dts);

            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = jsonInput.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            int code = conn.getResponseCode();
            return code == HttpURLConnection.HTTP_CREATED;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    //FUNCIÓN PARA EDITAR LA ZONA DEL MODULO DE ADMINISTRADOR
    public boolean editar(Integer id, ZonaAdminDTO dts) {
        try {
            String baseUrl = ConfiguradorApi.getInstance().getApiBaseUrl();
            URL url = new URL(baseUrl + "/api/escritorio/zonas/" + id);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("PUT");
            conn.setRequestProperty("Content-Type", "application/json; utf-8");
            conn.setRequestProperty("Accept", "application/json");
            conn.setDoOutput(true);

            Gson gson = new Gson();
            String jsonInput = gson.toJson(dts);

            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = jsonInput.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            int code = conn.getResponseCode();
            return code == HttpURLConnection.HTTP_OK;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
