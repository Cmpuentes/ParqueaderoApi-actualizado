/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DatosP;

/**
 *
 * @author Carlos Mario
 */
public class LoginRequest {

    private String login;
    private String password;
    private String turno;
    private String fecha_inicio;

    public LoginRequest(String login, String password, String turno, String fecha_inicio) {
        this.login = login;
        this.password = password;
        this.turno = turno;
        this.fecha_inicio = fecha_inicio;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getTurno() {
        return turno;
    }

    public void setTurno(String turno) {
        this.turno = turno;
    }

    public String getFecha_inicio() {
        return fecha_inicio;
    }

    public void setFecha_inicio(String fecha_inicio) {
        this.fecha_inicio = fecha_inicio;
    }
    
    
}
