/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Datos;

/**
 *
 * @author Carlos Mario
 */
public class TotalesMediosPagoDTO {
    
    private int totalEfectivo;
    private int totalTarjeta;
    private int totalTransferencia;

    public TotalesMediosPagoDTO() {
    }
    

    public TotalesMediosPagoDTO(int totalEfectivo, int totalTarjeta, int totalTransferencia) {
        this.totalEfectivo = totalEfectivo;
        this.totalTarjeta = totalTarjeta;
        this.totalTransferencia = totalTransferencia;
    }
    
    

    public int getTotalEfectivo() {
        return totalEfectivo;
    }

    public void setTotalEfectivo(int totalEfectivo) {
        this.totalEfectivo = totalEfectivo;
    }

    public int getTotalTarjeta() {
        return totalTarjeta;
    }

    public void setTotalTarjeta(int totalTarjeta) {
        this.totalTarjeta = totalTarjeta;
    }

    public int getTotalTransferencia() {
        return totalTransferencia;
    }

    public void setTotalTransferencia(int totalTransferencia) {
        this.totalTransferencia = totalTransferencia;
    }
    
    
    
}
