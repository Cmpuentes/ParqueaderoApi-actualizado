/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Logica;

import Datos.ClienteDTO;
import Datos.ClienteRequestDTO;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Carlos Mario
 */
public class ClienteAdministrativoService {

    public Integer totalregistros = 0;

    //FUNCIÓN PARA MOSTRAR EN LA TABLA LOS CLIENTES PREPAGO
    public DefaultTableModel mostrar(JTable tabla) {
        DefaultTableModel model = null;
        try {
            String baseUrl = ConfiguradorApi.getInstance().getApiBaseUrl();
            URL url = new URL(baseUrl + "/api/clientes/escritorio/listar");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");

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

            Gson gson = new Gson();
            Type listType = new TypeToken<List<ClienteDTO>>() {
            }.getType();
            List<ClienteDTO> clientes = gson.fromJson(json.toString(), listType);

            String[] columnas = {"ID", "Fecha", "Placa", "Tipo Vehículo", "Tipo Servicio",
                "Cliente", "Teléfono", "Tarifas", "Estado", "Observaciones"};

            model = new DefaultTableModel(columnas, 0);

            for (ClienteDTO c : clientes) {
                Object[] fila = {
                    c.getIdcliente(),
                    c.getFecha(),
                    c.getPlaca(),
                    c.getTipovehiculo(),
                    c.getTiposervicio(),
                    c.getCliente(),
                    c.getTelefono(),
                    c.getTarifas(),
                    c.getEstado(),
                    c.getObservaciones()
                };
                totalregistros++;
                model.addRow(fila);
            }

            tabla.setModel(model);

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al cargar clientes: " + e.getMessage());
        }
        return model; // 🔹 devolvemos el modelo en lugar de null
    }

    //FUNCIÓN PARA ELIMINAR CLIENTES PREPAGO
    public boolean eliminar(Integer idCliente) {
        try {
            String baseUrl = ConfiguradorApi.getInstance().getApiBaseUrl();
            URL url = new URL(baseUrl + "/api/clientes/escritorio" + idCliente);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("DELETE");

            int code = conn.getResponseCode();
            if (code == HttpURLConnection.HTTP_OK) {
                System.out.println("Cliente eliminado correctamente.");
                return true;
            } else if (code == HttpURLConnection.HTTP_NOT_FOUND) {
                JOptionPane.showMessageDialog(null, "Cliente no encontrado.");
                return false;
            } else {
                JOptionPane.showMessageDialog(null, "Error al eliminar. Código: " + code);
                return false;
            }

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error en la conexión: " + e.getMessage());
            return false;
        }
    }

    //FUNCIÓN PARA INSERTAR O ALMACENAR LOS CLIENTES PREPAGO
    public boolean insertar(ClienteRequestDTO cliente) {
        try {
            String baseUrl = ConfiguradorApi.getInstance().getApiBaseUrl();
            URL url = new URL(baseUrl + "/api/clientes/escritorio/insertar");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json; utf-8");
            conn.setRequestProperty("Accept", "application/json");
            conn.setDoOutput(true);

            // Convertir el DTO a JSON
            Gson gson = new Gson();
            String jsonInput = gson.toJson(cliente);

            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = jsonInput.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            int code = conn.getResponseCode();
            System.out.println("Response code insertar cliente: " + code);

            if (code == HttpURLConnection.HTTP_OK) {
                try (BufferedReader br = new BufferedReader(
                        new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = br.readLine()) != null) {
                        response.append(line.trim());
                    }
                    System.out.println("Respuesta servidor insertar cliente: " + response);
                }
                return true;
            } else {
                System.out.println("Error al insertar cliente. Código: " + code);
                return false;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    //FUNCIÓN PARA EDITAR LOS EMPLEADOS PREPAGO
    public boolean editar(Integer id, ClienteRequestDTO dto) {
        try {
            String baseUrl = ConfiguradorApi.getInstance().getApiBaseUrl();
            URL url = new URL(baseUrl + "/api/clientes/escritorio" + id);

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
                System.out.println("Cliente actualizado correctamente.");
                return true;
            } else if (code == HttpURLConnection.HTTP_NOT_FOUND) {
                System.out.println("Cliente no encontrado.");
                return false;
            } else {
                System.out.println("Error al editar cliente. Código: " + code);
                return false;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
