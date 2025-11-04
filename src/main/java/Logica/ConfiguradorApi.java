
package Logica;

import java.io.*;
import java.util.Properties;

/**
 *
 * @author Carlos Mario
 */
public class ConfiguradorApi {

    private static ConfiguradorApi instancia;
    private Properties propiedades;

    private ConfiguradorApi() {
        propiedades = new Properties();
        try {
            // Ruta del archivo externo
            String ruta = "C:\\Conexion\\config.properties"; // ← Cambia esta ruta según tu sistema
            File archivo = new File(ruta);

            if (!archivo.exists()) {
                throw new RuntimeException("❌ No se encontró el archivo de configuración: " + ruta);
            }

            try (FileInputStream input = new FileInputStream(archivo)) {
                propiedades.load(input);
            }

        } catch (IOException e) {
            throw new RuntimeException("❌ Error cargando config.properties: " + e.getMessage(), e);
        }
    }

    public static ConfiguradorApi getInstance() {
        if (instancia == null) {
            instancia = new ConfiguradorApi();
        }
        return instancia;
    }

    public String getApiBaseUrl() {
        String url = propiedades.getProperty("api.base.url");
        if (url == null || url.trim().isEmpty()) {
            throw new RuntimeException("❌ 'api.base.url' no está definido en config.properties");
        }
        return url;
    }

    // Métodos adicionales (opcional)
    public int getConnectionTimeout() {
        return Integer.parseInt(propiedades.getProperty("api.timeout.connection", "5000"));
    }

    public int getReadTimeout() {
        return Integer.parseInt(propiedades.getProperty("api.timeout.read", "10000"));
    }
}