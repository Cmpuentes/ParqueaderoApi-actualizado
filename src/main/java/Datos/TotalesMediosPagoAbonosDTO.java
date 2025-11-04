/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Datos;

/**
 *
 * @author Carlos Mario
 */
public class TotalesMediosPagoAbonosDTO {
    
    private long efectivo;
    private long tarjeta;
    private long transferencia;

    public TotalesMediosPagoAbonosDTO() {
    }
    
    

    public TotalesMediosPagoAbonosDTO(long efectivo, long tarjeta, long transferencia) {
        this.efectivo = efectivo;
        this.tarjeta = tarjeta;
        this.transferencia = transferencia;
    }

    public long getEfectivo() {
        return efectivo;
    }

    public void setEfectivo(long efectivo) {
        this.efectivo = efectivo;
    }

    public long getTarjeta() {
        return tarjeta;
    }

    public void setTarjeta(long tarjeta) {
        this.tarjeta = tarjeta;
    }

    public long getTransferencia() {
        return transferencia;
    }

    public void setTransferencia(long transferencia) {
        this.transferencia = transferencia;
    }
    
    
}
