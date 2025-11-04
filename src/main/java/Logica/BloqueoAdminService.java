/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Logica;

import Datos.BloqueosAdministrativoDTO;
import Datos.BloqueosDTO;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Carlos Mario
 */
public class BloqueoAdminService {

    public Integer totalregistros = 0;

    //FUNCIÓN PARA MOSTRAR BLOQUEOS EN EL MODULO ADMINISTRATIVO
    public DefaultTableModel mostrar(String buscar) {
        String[] titulos = {"Id", "Tipo bloqueo", "Codigo"};
        DefaultTableModel modelo = new DefaultTableModel(null, titulos);

        try {
            String baseUrl = ConfiguradorApi.getInstance().getApiBaseUrl();
            String endpoint = baseUrl + "/api/bloqueos/escritorio/administrativos";

            if (buscar != null && !buscar.isEmpty()) {
                endpoint += "?codigo=" + buscar;
            }

            URL url = new URL(endpoint);  // ✅ usar endpoint ya construido
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
                    List<BloqueosDTO> lista = gson.fromJson(
                            response.toString(), new TypeToken<List<BloqueosDTO>>() {
                    }.getType()
                    );

                    for (BloqueosDTO b : lista) {

                        modelo.addRow(new Object[]{
                            b.getIdbloqueo(),
                            b.getTipobloqueo(),
                            b.getCodigo()
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

    //FUNCIÓN PARA INSERTAR LOS BLOQUEOS EN MODULO DE ADMINISTRATIVOS
    public boolean insertar(BloqueosAdministrativoDTO dto) {
        try {
            String baseUrl = ConfiguradorApi.getInstance().getApiBaseUrl();
            URL url = new URL(baseUrl + "/api/bloqueos/escritorio/insertar/administrativo");

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json; utf-8");
            conn.setRequestProperty("Accept", "application/json");
            conn.setDoOutput(true);

            Gson gson = new Gson();
            String jsonInput = gson.toJson(dto);

            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = jsonInput.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            int responseCode = conn.getResponseCode();
            if (responseCode == 201) { // creado correctamente
                return true;
            } else {
                JOptionPane.showMessageDialog(null, "Error al insertar: código " + responseCode);
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al insertar: " + e.getMessage());
        }
        return false;
    }

    //FUNCIÓN PARA EDITAR LOS BLOQUEOS EN EL MODULO ADMINISTRATIVO
    public boolean editar(Integer id, BloqueosAdministrativoDTO dto) {
        try {
            String baseUrl = ConfiguradorApi.getInstance().getApiBaseUrl();
            URL url = new URL(baseUrl + "/api/bloqueos/escritorio/editar/administrativo/" + id);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("PUT");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            Gson gson = new Gson();
            String json = gson.toJson(dto);

            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = json.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            int responseCode = conn.getResponseCode();
            return responseCode == 200; // true si fue exitoso
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    //FUNCIÓN PARA ELIMINAR LOS BLOQUEOS EN EL MODULO ADMINISTRATIVO
    public boolean eliminar(Integer idbloqueo) {
        try {
            String baseUrl = ConfiguradorApi.getInstance().getApiBaseUrl();
            URL url = new URL(baseUrl + "/api/bloqueos/escritorio/eliminar/administrativo/" + idbloqueo);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("DELETE");
            conn.setRequestProperty("Accept", "application/json");

            int responseCode = conn.getResponseCode();

            if (responseCode == 200) {
                return true; // eliminado correctamente
            } else if (responseCode == 404) {
                JOptionPane.showMessageDialog(null, "El registro no existe en la base de datos.");
            } else {
                JOptionPane.showMessageDialog(null, "Error al eliminar: código " + responseCode);
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al eliminar: " + e.getMessage());
        }
        return false;
    }
}
