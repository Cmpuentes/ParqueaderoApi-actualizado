package LogicaP;

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class Cconexionp {

    private String user;
    private String pass;
    private String bd;
    private String ip;
    private String puerto;
    private Connection conectar = null; // 🔹 Variable para la conexión

    // 🔹 Ruta fija en C:\reservascombugas\Configuracion
    private final String CONFIG_DIR = "C:\\ConexionCombugasH_P_Gesnnova";
    private final String CONFIG_FILE = CONFIG_DIR + "\\Configuracion_P.properties";

    public Cconexionp() {
        cargarConfiguracion();
    }

    private void cargarConfiguracion() {
        Properties prop = new Properties();
        File archivoConfig = new File(CONFIG_FILE);

        if (archivoConfig.exists()) {
            // 🔹 Cargar configuración desde archivo externo
            try (InputStream fis = new FileInputStream(archivoConfig)) {
                prop.load(fis);
//                System.out.println("Configuración cargada desde: " + CONFIG_FILE);
            } catch (IOException e) {
                System.err.println("Error cargando configuración externa: " + e.getMessage());
            }
        } else {
            // 🔹 Si no existe, copiar desde el JAR y guardar en la ruta externa
//            System.out.println("Archivo externo no encontrado, cargando configuración interna...");
            try (InputStream is = getClass().getClassLoader().getResourceAsStream("Recursos/configuracion.properties")) {
                if (is == null) {
                    throw new IOException("No se encontró el archivo de configuración interno.");
                }
                prop.load(is);
                guardarConfiguracion(prop); // Guardar el archivo en la ruta externa
            } catch (IOException ex) {
                System.err.println("Error cargando configuración interna: " + ex.getMessage());
            }
        }

        // 🔹 Asignar valores
        user = prop.getProperty("db.user", "root");
        pass = prop.getProperty("db.pass", "admin");
        bd = prop.getProperty("db.name", "parqueadero_cbg1");
        ip = prop.getProperty("db.host", "localhost");
        puerto = prop.getProperty("db.port", "3306");
    }

    private void guardarConfiguracion(Properties prop) {
        try {
            File directorio = new File(CONFIG_DIR);
            if (!directorio.exists()) {
                directorio.mkdirs(); // Crear la carpeta si no existe
            }
            try (OutputStream fos = new FileOutputStream(CONFIG_FILE)) {
                prop.store(fos, "Configuración de conexión a la base de datos");
//                System.out.println("Archivo de configuración creado en: " + CONFIG_FILE);
            }
        } catch (IOException e) {
            System.err.println("Error guardando configuración: " + e.getMessage());
        }
    }


    public Connection establecerConexionp() {
        String cadena = "jdbc:mysql://" + ip + ":" + puerto + "/" + bd;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conectar = DriverManager.getConnection(cadena, user, pass);
//            System.out.println("✅ Conexión exitosa a la base de datos.");
        } catch (ClassNotFoundException | SQLException e) {
            System.err.println("❌ Error: No se encontró el driver de MySQL.");
        }
        return conectar;
    }
}
