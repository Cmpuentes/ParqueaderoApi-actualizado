/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DatosP;

/**
 *
 * @author Carlos Mario
 */
public class SessionManager {
    
    private static SessionManager instance;
    private SessionData sessionData;
    
    private SessionManager() {
        // Constructor privado → patrón Singleton
    }
    
    public static SessionManager getInstance() {
        if (instance == null) {
            instance = new SessionManager();
        }
        return instance;
    }
    
    public void setSessionData(SessionData data) {
        this.sessionData = data;
    }

    public SessionData getSessionData() {
        return this.sessionData;
    }

    public void clearSession() {
        this.sessionData = null;
    }

    public boolean isLoggedIn() {
        return this.sessionData != null;
    }
}
