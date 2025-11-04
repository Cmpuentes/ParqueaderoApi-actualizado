package Logica;

import Datos.AbonoDTO;
import Datos.PrepagoResponseDTO;
import DatosP.Dabonosp;
import Datos.TotalesMediosPagoAbonosDTO;
import LogicaP.Fabonosp;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Carlos Mario
 */
public class ApiAbonosService {

    public Integer totalregistros = 0;

    ArrayList<Dabonosp> listacliente = new ArrayList<>();

    public void agregarCliente(Dabonosp cliente) {
        listacliente.add(cliente);
    }

    //FUNCIÓN PARA MOSTRAR LOS DATOS DE ABONO EN LA TABLA DEL JABONOSP
    public DefaultTableModel mostrar(String buscar) throws Exception {
        String baseUrl = ConfiguradorApi.getInstance().getApiBaseUrl();
        //URL url = new URL(baseUrl + "/api/abonos/buscar?cliente=" + URLEncoder.encode(buscar, "UTF-8"));
        URL url = new URL(baseUrl + "/api/escritorio/abonos/buscar?cliente=" + URLEncoder.encode(buscar, "UTF-8"));
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Accept", "application/json");

        if (conn.getResponseCode() != 200) {
            throw new RuntimeException("Error HTTP: " + conn.getResponseCode());
        }

        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        ObjectMapper mapper = new ObjectMapper();
        List<Dabonosp> lista = mapper.readValue(br, new TypeReference<List<Dabonosp>>() {
        });

        // Encabezados como en tu JTable
        String[] titulos = {"ID", "Cliente", "Fecha", "Valor", "Efectivo", "Tarjeta",
            "Transferencia", "Total", "Saldo", "Turno", "Empleado",
            "Observaciones", "Número Turno",};

        DefaultTableModel modelo = new DefaultTableModel(null, titulos);

        for (Dabonosp ab : lista) {
            Object[] registro = new Object[]{
                ab.getIdabonos(),
                ab.getCliente(),
                ab.getFecha(),
                ab.getValor(),
                ab.getEfectivo(),
                ab.getTarjeta(),
                ab.getTransferencia(),
                ab.getSaldo(),
                ab.getTotal(),
                ab.getTurno(),
                ab.getEmpleado(),
                ab.getObservaciones(),
                ab.getNumeroturno(),};

            totalregistros++;
            modelo.addRow(registro);
        }

        conn.disconnect();
        return modelo;
    }

    //FUNCIÓN PARA LLENAR EL COMBOBOX EN JABONOP
    public void llenarcboClientes(JComboBox<String> combo) {
        DefaultComboBoxModel<String> com = new DefaultComboBoxModel<>();
        combo.setModel(com);

        com.addElement("SELECCIONE UN CLIENTE");

        try {
            //URL url = new URL("http://localhost:8080/api/clientes/unicos");
            String baseUrl = ConfiguradorApi.getInstance().getApiBaseUrl();
            URL url = new URL(baseUrl + "/api/clientes/escritorio/unicos");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");

            ObjectMapper mapper = new ObjectMapper();
            List<String> clientes = mapper.readValue(conn.getInputStream(),
                    new TypeReference<List<String>>() {
            });

            for (String cliente : clientes) {
                com.addElement(cliente);

                ApiAbonosService func = new ApiAbonosService();
                Dabonosp cli = new Dabonosp();
                cli.setCliente(cliente);
                func.agregarCliente(cli);
            }

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al cargar clientes: " + e.getMessage());
        }
    }

    //FUNCIÓN PARA INSERTAR LOS DATOS DE ABONO EN LA TABLA ABONOS
    public boolean insertar(AbonoDTO abono) {
        try {

            // URL url = new URL("http://localhost:8080/api/abonos");
            String baseUrl = ConfiguradorApi.getInstance().getApiBaseUrl();
            URL url = new URL(baseUrl + "/api/abonos");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            // Configuración de la petición
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json; utf-8");
            conn.setRequestProperty("Accept", "application/json");
            conn.setDoOutput(true);

            // Convertir objeto AbonoDTO a JSON
            Gson gson = new Gson();
            String jsonInput = gson.toJson(abono);

            // Enviar el JSON en el body
            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = jsonInput.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            // Validar respuesta del servidor
            int code = conn.getResponseCode();
            if (code == 200 || code == 201) {
                return true; // Abono insertado correctamente
            } else {
                JOptionPane.showMessageDialog(null, "Error en el servidor: código " + code);
                return false;
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al insertar abono: " + e.getMessage());
            return false;
        }
    }

    //FUNCIÓN PARA EDITAR EL ABONO
    public boolean editar(Long id, AbonoDTO abono) {
        try {
            //URL url = new URL("http://localhost:8080/api/abonos/" + id);
            String baseUrl = ConfiguradorApi.getInstance().getApiBaseUrl();
            URL url = new URL(baseUrl + "/api/abonos/" + id);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("PUT");
            conn.setRequestProperty("Content-Type", "application/json; utf-8");
            conn.setRequestProperty("Accept", "application/json");
            conn.setDoOutput(true);

            Gson gson = new Gson();
            String jsonInput = gson.toJson(abono);

            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = jsonInput.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            int code = conn.getResponseCode();
            return code == 200; // éxito si el servidor responde OK

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al editar abono: " + e.getMessage());
            return false;
        }
    }

    //FUNCIÓN USADA PARA ELIMINAR ABONO
    public boolean eliminar(Long id) {
        try {
            //URL url = new URL("http://localhost:8080/api/abonos/" + id);
            String baseUrl = ConfiguradorApi.getInstance().getApiBaseUrl();
            URL url = new URL(baseUrl + "/api/abonos/" + id);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("DELETE");
            conn.setRequestProperty("Accept", "application/json");

            int code = conn.getResponseCode();

            if (code == 200) {
                return true;
            } else {
                try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getErrorStream(), "utf-8"))) {
                    StringBuilder response = new StringBuilder();
                    String responseLine;
                    while ((responseLine = br.readLine()) != null) {
                        response.append(responseLine.trim());
                    }
                    JOptionPane.showMessageDialog(null, "Error: " + response.toString());
                }
                return false;
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al eliminar: " + e.getMessage());
            return false;
        }
    }

    //MÉTODO PARA OBTENER EL VALOR PREPAGO QUE OPERA CON EL ABONO
    public PrepagoResponseDTO obtenerPrepago(String cliente) {
        try {
            //URL url = new URL("http://localhost:8080/api/abonos/prepago/" + cliente);
            String baseUrl = ConfiguradorApi.getInstance().getApiBaseUrl();
            URL url = new URL(baseUrl + "/api/abonos/prepago/" + cliente);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");

            if (conn.getResponseCode() == 200) {
                try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"))) {
                    StringBuilder response = new StringBuilder();
                    String responseLine;
                    while ((responseLine = br.readLine()) != null) {
                        response.append(responseLine.trim());
                    }

                    Gson gson = new Gson();
                    return gson.fromJson(response.toString(), PrepagoResponseDTO.class);
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al obtener prepago: " + e.getMessage());
        }
        return null;
    }

}
