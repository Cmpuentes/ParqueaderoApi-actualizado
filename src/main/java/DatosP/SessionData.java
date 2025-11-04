/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DatosP;

/**
 *
 * @author Carlos Mario
 */
public class SessionData {
    
    private String token;
    private String nombreCompleto;
    private String fechaInicio;
    private String turno;
    private int numeroTurno;
    private boolean turnoFinalizado;

    public SessionData() {
    }

    public SessionData(String token, String nombreCompleto, String fechaInicio, String turno, int numeroTurno, boolean turnoFinalizado) {
        this.token = token;
        this.nombreCompleto = nombreCompleto;
        this.fechaInicio = fechaInicio;
        this.turno = turno;
        this.numeroTurno = numeroTurno;
        this.turnoFinalizado = turnoFinalizado;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public String getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(String fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public String getTurno() {
        return turno;
    }

    public void setTurno(String turno) {
        this.turno = turno;
    }

    public int getNumeroTurno() {
        return numeroTurno;
    }

    public void setNumeroTurno(int numeroTurno) {
        this.numeroTurno = numeroTurno;
    }

    public boolean isTurnoFinalizado() {
        return turnoFinalizado;
    }

    public void setTurnoFinalizado(boolean turnoFinalizado) {
        this.turnoFinalizado = turnoFinalizado;
    }
    
    
}
