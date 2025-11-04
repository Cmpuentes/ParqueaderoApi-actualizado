package DatosP;

public class Dotrosserviciosp {

    public int idotros;
    public String fecha;
    public String tiposervicio;
    public String cliente;
    public int valor;
    public int efectivo;
    public int tarjeta;
    public int transferencia;
    public int total;
    public String numturno;
    public String responsable;
    public String turno;

    public Dotrosserviciosp() {
    }

    public Dotrosserviciosp(int idotros, String fecha, String tiposervicio, String cliente, int valor, int efectivo, int tarjeta, int transferencia, int total, String numturno, String responsable, String turno) {
        this.idotros = idotros;
        this.fecha = fecha;
        this.tiposervicio = tiposervicio;
        this.cliente = cliente;
        this.valor = valor;
        this.efectivo = efectivo;
        this.tarjeta = tarjeta;
        this.transferencia = transferencia;
        this.total = total;
        this.numturno = numturno;
        this.responsable = responsable;
        this.turno = turno;
    }

    public int getIdotros() {
        return idotros;
    }

    public void setIdotros(int idotros) {
        this.idotros = idotros;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getTiposervicio() {
        return tiposervicio;
    }

    public void setTiposervicio(String tiposervicio) {
        this.tiposervicio = tiposervicio;
    }

    public String getCliente() {
        return cliente;
    }

    public void setCliente(String cliente) {
        this.cliente = cliente;
    }

    public int getValor() {
        return valor;
    }

    public void setValor(int valor) {
        this.valor = valor;
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

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public String getNumturno() {
        return numturno;
    }

    public void setNumturno(String numturno) {
        this.numturno = numturno;
    }

    public String getResponsable() {
        return responsable;
    }

    public void setResponsable(String responsable) {
        this.responsable = responsable;
    }

    public String getTurno() {
        return turno;
    }

    public void setTurno(String turno) {
        this.turno = turno;
    }

}
