/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Datos;

/**
 *
 * @author Carlos Mario
 */
public class PrepagoResponseDTO {
    
    private int consumo;          // valor consumido en salida
    private int saldoRestante;    // saldo después del descuento
    private String status;        // OK / ERROR
    private String mensaje;

    public PrepagoResponseDTO(int consumo, int saldoRestante, String status, String mensaje) {
        this.consumo = consumo;
        this.saldoRestante = saldoRestante;
        this.status = status;
        this.mensaje = mensaje;
    }

    public int getConsumo() {
        return consumo;
    }

    public void setConsumo(int consumo) {
        this.consumo = consumo;
    }

    public int getSaldoRestante() {
        return saldoRestante;
    }

    public void setSaldoRestante(int saldoRestante) {
        this.saldoRestante = saldoRestante;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }
    
    
}
