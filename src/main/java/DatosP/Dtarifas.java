package DatosP;

public class Dtarifas {

    public int idtarifas;
    public String tipovehiculo;
    public String tiposervicio;
    public int precio12h;
    public int descuentorecibo;
    public int preciohoras;

    public Dtarifas() {
    }

    public Dtarifas(int idtarifas, String tipovehiculo, String tiposervicio, int precio12h, int descuentorecibo, int preciohoras) {
        this.idtarifas = idtarifas;
        this.tipovehiculo = tipovehiculo;
        this.tiposervicio = tiposervicio;
        this.precio12h = precio12h;
        this.descuentorecibo = descuentorecibo;
        this.preciohoras = preciohoras;
    }

    public Dtarifas(int precio12h, int preciohoras) {
        this.precio12h = precio12h;
        this.preciohoras = preciohoras;
    }
    public Dtarifas(int descuento) {
        this.descuentorecibo = descuento;
        
    }

    public int getIdtarifas() {
        return idtarifas;
    }

    public void setIdtarifas(int idtarifas) {
        this.idtarifas = idtarifas;
    }

    public String getTipovehiculo() {
        return tipovehiculo;
    }

    public void setTipovehiculo(String tipovehiculo) {
        this.tipovehiculo = tipovehiculo;
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

    public String getTiposervicio() {
        return tiposervicio;
    }

    public void setTiposervicio(String tiposervicio) {
        this.tiposervicio = tiposervicio;
    }

}
