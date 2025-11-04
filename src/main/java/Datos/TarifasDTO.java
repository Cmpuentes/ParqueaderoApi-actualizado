/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Datos;

/**
 *
 * @author Carlos Mario
 */
public class TarifasDTO {
    
    private int precio12h;
    private int preciohoras;
    private int descuentorecibo;

    public int getPrecio12h() {
        return precio12h;
    }

    public void setPrecio12h(int precio12h) {
        this.precio12h = precio12h;
    }

    public int getPreciohoras() {
        return preciohoras;
    }

    public void setPreciohoras(int preciohoras) {
        this.preciohoras = preciohoras;
    }

    public int getDescuentorecibo() {
        return descuentorecibo;
    }

    public void setDescuentorecibo(int descuentorecibo) {
        this.descuentorecibo = descuentorecibo;
    }
}
