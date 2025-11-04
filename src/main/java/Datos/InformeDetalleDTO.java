/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Datos;

/**
 *
 * @author Carlos Mario
 */
public class InformeDetalleDTO {
    
    private String turno;
    private String numeroTurno;
    private String empleado;
    private String fechaIngreso;
    private String fechaSalida;
    private int efectivo;
    private int tarjeta;
    private int transferencia;
    private int otrosIngresos;
    private int efectivoLiquido;
    private int totalRecaudado;

    public String getTurno() {
        return turno;
    }

    public void setTurno(String turno) {
        this.turno = turno;
    }

    public String getNumeroTurno() {
        return numeroTurno;
    }

    public void setNumeroTurno(String numeroTurno) {
        this.numeroTurno = numeroTurno;
    }

    public String getEmpleado() {
        return empleado;
    }

    public void setEmpleado(String empleado) {
        this.empleado = empleado;
    }

    public String getFechaIngreso() {
        return fechaIngreso;
    }

    public void setFechaIngreso(String fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }

    public String getFechaSalida() {
        return fechaSalida;
    }

    public void setFechaSalida(String fechaSalida) {
        this.fechaSalida = fechaSalida;
    }

    public int getEfectivo() {
        return efectivo;
    }

    public void setEfectivo(int efectivo) {
        this.efectivo = efectivo;
    }

    public int getTarjeta() {
        return tarjeta;
    }

    public void setTarjeta(int tarjeta) {
        this.tarjeta = tarjeta;
    }

    public int getTransferencia() {
        return transferencia;
    }

    public void setTransferencia(int transferencia) {
        this.transferencia = transferencia;
    }

    public int getOtrosIngresos() {
        return otrosIngresos;
    }

    public void setOtrosIngresos(int otrosIngresos) {
        this.otrosIngresos = otrosIngresos;
    }

    public int getEfectivoLiquido() {
        return efectivoLiquido;
    }

    public void setEfectivoLiquido(int efectivoLiquido) {
        this.efectivoLiquido = efectivoLiquido;
    }

    public int getTotalRecaudado() {
        return totalRecaudado;
    }

    public void setTotalRecaudado(int totalRecaudado) {
        this.totalRecaudado = totalRecaudado;
    }
    
}
