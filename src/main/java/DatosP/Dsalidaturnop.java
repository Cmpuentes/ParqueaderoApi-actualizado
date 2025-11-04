package DatosP;

public class Dsalidaturnop {

    public int idfinturno;
    public String turno;
    public String numero_turno;
    public String empleado;
    public String fechaingreso;
    public String fechasalida;
    public String recibos;
    public String total_vehiculos;
    public int base;
    public int efectivo;
    public int tarjeta;
    public int transferencia;
    public int otros_ingresos;
    public int efectivo_liquido;
    public int total_recaudado;
    public String estado;
    public String observaciones;
    public int total_abonos;

    public Dsalidaturnop() {
    }

    public Dsalidaturnop(int idfinturno, String turno, String numero_turno, String empleado, String fechaingreso, 
            String fechasalida, String recibos, String total_vehiculos, int base, int efectivo, int tarjeta,
            int transferencia, int otros_ingresos, int efectivo_liquido, int total_recaudado, String estado,
            String observaciones, int total_abonos) {
        this.idfinturno = idfinturno;
        this.turno = turno;
        this.numero_turno = numero_turno;
        this.empleado = empleado;
        this.fechaingreso = fechaingreso;
        this.fechasalida = fechasalida;
        this.recibos = recibos;
        this.total_vehiculos = total_vehiculos;
        this.base = base;
        this.efectivo = efectivo;
        this.tarjeta = tarjeta;
        this.transferencia = transferencia;
        this.otros_ingresos = otros_ingresos;
        this.efectivo_liquido = efectivo_liquido;
        this.total_recaudado = total_recaudado;
        this.estado = estado;
        this.observaciones = observaciones;
        this.total_abonos = total_abonos;
    }

    public int getIdfinturno() {
        return idfinturno;
    }

    public void setIdfinturno(int idfinturno) {
        this.idfinturno = idfinturno;
    }

    public String getTurno() {
        return turno;
    }

    public void setTurno(String turno) {
        this.turno = turno;
    }

    public String getNumero_turno() {
        return numero_turno;
    }

    public void setNumero_turno(String numero_turno) {
        this.numero_turno = numero_turno;
    }

    public String getEmpleado() {
        return empleado;
    }

    public void setEmpleado(String empleado) {
        this.empleado = empleado;
    }

    public String getFechasalida() {
        return fechasalida;
    }

    public void setFechasalida(String fechasalida) {
        this.fechasalida = fechasalida;
    }

    public String getRecibos() {
        return recibos;
    }

    public void setRecibos(String recibos) {
        this.recibos = recibos;
    }

    public String getTotal_vehiculos() {
        return total_vehiculos;
    }

    public void setTotal_vehiculos(String total_vehiculos) {
        this.total_vehiculos = total_vehiculos;
    }

    public int getBase() {
        return base;
    }

    public void setBase(int base) {
        this.base = base;
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

    public int getOtros_ingresos() {
        return otros_ingresos;
    }

    public void setOtros_ingresos(int otros_ingresos) {
        this.otros_ingresos = otros_ingresos;
    }

    public int getEfectivo_liquido() {
        return efectivo_liquido;
    }

    public void setEfectivo_liquido(int efectivo_liquido) {
        this.efectivo_liquido = efectivo_liquido;
    }

    public int getTotal_recaudado() {
        return total_recaudado;
    }

    public void setTotal_recaudado(int total_recaudado) {
        this.total_recaudado = total_recaudado;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public String getFechaingreso() {
        return fechaingreso;
    }

    public void setFechaingreso(String fechaingreso) {
        this.fechaingreso = fechaingreso;
    }

    public int getTotal_abonos() {
        return total_abonos;
    }

    public void setTotal_abonos(int total_abonos) {
        this.total_abonos = total_abonos;
    }

}
