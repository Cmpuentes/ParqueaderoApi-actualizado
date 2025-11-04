package DatosP;

public class Dabonosp {

    public Long idabonos;
    public String fecha;
    public String cliente;
    public int valor;
    public int efectivo;
    public int tarjeta;
    public int transferencia;
    public int total;
    public String turno;
    public String empleado;
    public String observaciones;
    public String numeroturno;
    public int saldo;

    public Dabonosp() {
    }

    public Dabonosp(Long idabonos, String fecha, String cliente, int valor, int efectivo, int tarjeta, int transferencia, int total, String turno, String empleado, String observaciones, String numeroturno, int saldo) {
        this.idabonos = idabonos;
        this.fecha = fecha;
        this.cliente = cliente;
        this.valor = valor;
        this.efectivo = efectivo;
        this.tarjeta = tarjeta;
        this.transferencia = transferencia;
        this.total = total;
        this.turno = turno;
        this.empleado = empleado;
        this.observaciones = observaciones;
        this.numeroturno = numeroturno;
        this.saldo = saldo;
    }

    public Long getIdabonos() {
        return idabonos;
    }

    public void setIdabonos(Long idabonos) {
        this.idabonos = idabonos;
    }

    

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
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

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public String getEmpleado() {
        return empleado;
    }

    public void setEmpleado(String empleado) {
        this.empleado = empleado;
    }

    public String getTurno() {
        return turno;
    }

    public void setTurno(String turno) {
        this.turno = turno;
    }

    public String getNumeroturno() {
        return numeroturno;
    }

    public void setNumeroturno(String numeroturno) {
        this.numeroturno = numeroturno;
    }

    public int getSaldo() {
        return saldo;
    }

    public void setSaldo(int saldo) {
        this.saldo = saldo;
    }

}
