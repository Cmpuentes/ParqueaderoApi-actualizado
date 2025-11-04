/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Logica;

import Datos.TarifasAdminResDTO;
import Datos.TarifasAdministrativoDTO;
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
public class TarifasAdminService {

    public Integer totalregistros = 0;

    //FUNCIÓN PARA MOSTRAR LAS TARIFAS EN EL MODULO ADMINISTRADOR
    public DefaultTableModel mostrar(String buscar) {
        String[] titulos = {"ID", "Tipo Vehiculo", "Tipo Servicio", "Precio 12h", "Descuento Recibo", "Precio Horas"};
        DefaultTableModel modelo = new DefaultTableModel(null, titulos);

        try {
            String baseUrl = ConfiguradorApi.getInstance().getApiBaseUrl();
            String endpoint = baseUrl + "/api/tarifas/escritorio/administrativos";

            if (buscar != null && !buscar.isEmpty()) {
                endpoint += "?tipovehiculo=" + buscar;
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
                    List<TarifasAdministrativoDTO> lista = gson.fromJson(
                            response.toString(), new TypeToken<List<TarifasAdministrativoDTO>>() {
                    }.getType()
                    );

                    for (TarifasAdministrativoDTO t : lista) {
                        modelo.addRow(new Object[]{
                            t.getIdtarifas(),
                            t.getTipovehiculo(),
                            t.getTiposervicio(),
                            t.getPrecio12h(),
                            t.getDescuentorecibo(),
                            t.getPreciohoras()
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

    //FUNCIÓN PARA LA INSERSIÓN DE LAS TARIFAS EN EL MODULO ADMINISTRATIVO
    public boolean insertar(TarifasAdminResDTO dto) {
        try {
            String baseUrl = ConfiguradorApi.getInstance().getApiBaseUrl();
            URL url = new URL(baseUrl + "/api/tarifas/escritorio");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("POST");
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
            System.out.println("Response code insertar tarifa: " + code);

            return code == HttpURLConnection.HTTP_OK || code == HttpURLConnection.HTTP_CREATED;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    //FUNCIÓN PARA EDITAR LAS TARIFAS EN EL MODULO DE ADMINISTRATIVOS
    public boolean editar(Integer id, TarifasAdminResDTO dts) {
        try {
            String baseUrl = ConfiguradorApi.getInstance().getApiBaseUrl();
            URL url = new URL(baseUrl + "/api/tarifas/escritorio" + id);
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
            if (code == HttpURLConnection.HTTP_OK) {
                return true; // actualización exitosa
            } else {
                System.err.println("Error al editar tarifa. Código: " + code);
                return false;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    //FUNCIÓN PARA ELIMINAR EL REGISTRO DE TARIFAS
    public boolean eliminar(Integer id) {
        try {
            String baseUrl = ConfiguradorApi.getInstance().getApiBaseUrl();
            URL url = new URL(baseUrl + "/api/tarifas/escritorio" + id);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("DELETE");

            int code = conn.getResponseCode();
            return code == 200; // éxito
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
