/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Datos;

/**
 *
 * @author Carlos Mario
 */
public class FinTurnoDTO {
    
    private Long idturno;
    private String empleado;
    private String fechainicio;
    private String turno;
    private int numeroturno;
    private String estado;

    public Long getIdturno() {
        return idturno;
    }

    public void setIdturno(Long idturno) {
        this.idturno = idturno;
    }

    public String getEmpleado() {
        return empleado;
    }

    public void setEmpleado(String empleado) {
        this.empleado = empleado;
    }

    public String getFechainicio() {
        return fechainicio;
    }

    public void setFechainicio(String fechainicio) {
        this.fechainicio = fechainicio;
    }

    public String getTurno() {
        return turno;
    }

    public void setTurno(String turno) {
        this.turno = turno;
    }

    public int getNumeroturno() {
        return numeroturno;
    }

    public void setNumeroturno(int numeroturno) {
        this.numeroturno = numeroturno;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
    
    
    
}
