/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Datos;

/**
 *
 * @author Carlos Mario
 */
public class TarifasAdminResDTO {
    
    private String tipovehiculo;
    private String tiposervicio;
    private int precio12h;
    private int descuentorecibo;
    private int preciohoras;

    public TarifasAdminResDTO() {
    }

    public String getTipovehiculo() {
        return tipovehiculo;
    }

    public void setTipovehiculo(String tipovehiculo) {
        this.tipovehiculo = tipovehiculo;
    }

    public String getTiposervicio() {
        return tiposervicio;
    }

    public void setTiposervicio(String tiposervicio) {
        this.tiposervicio = tiposervicio;
    }

    public int getPrecio12h() {
        return precio12h;
    }

    public void setPrecio12h(int precio12h) {
        this.precio12h = precio12h;
    }

    public int getDescuentorecibo() {
        return descuentorecibo;
    }

    public void setDescuentorecibo(int descuentorecibo) {
        this.descuentorecibo = descuentorecibo;
    }

    public int getPreciohoras() {
        return preciohoras;
    }

    public void setPreciohoras(int preciohoras) {
        this.preciohoras = preciohoras;
    }
    
    
    
}
