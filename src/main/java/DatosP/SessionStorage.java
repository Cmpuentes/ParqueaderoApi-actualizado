/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DatosP;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Optional;

/**
 *
 * @author Carlos Mario
 */
public class SessionStorage {
    
    private static final String FILE_NAME = "session.json";
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public static void saveSession(SessionData session) {
        if (session == null) return;
        try (FileWriter writer = new FileWriter(FILE_NAME)) {
            gson.toJson(session, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // devuelve SessionData o null
    public static SessionData loadSession() {
        File f = new File(FILE_NAME);
        if (!f.exists() || f.length() == 0) return null;
        try (FileReader reader = new FileReader(FILE_NAME)) {
            return gson.fromJson(reader, SessionData.class);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    // versión Optional para compatibilidad
    public static Optional<SessionData> load() {
        return Optional.ofNullable(loadSession());
    }

    public static void clearSession() {
        try (FileWriter writer = new FileWriter(FILE_NAME)) {
            writer.write("");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
