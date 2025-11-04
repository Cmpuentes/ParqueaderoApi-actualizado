package DatosP;

public class Dingresop {

    public int idingreso;
    public String turno;
    public String numeroturno;
    public String empleado;
    public String placa;
    public String fechaingreso;
    public String tipovehiculo;
    public String tiposervicio;
    public String cliente;
    public String zona;
    public String observaciones;
    public String estado;

    public Dingresop() {
    }

    public Dingresop(int idingreso, String cliente, String turno, String numeroturno, String empleado, String placa, String fechaingreso, String tipovehiculo, String tiposervicio, String zona, String observaciones, String estado) {
        this.idingreso = idingreso;
        this.turno = turno;
        this.numeroturno = numeroturno;
        this.empleado = empleado;
        this.placa = placa;
        this.fechaingreso = fechaingreso;
        this.tipovehiculo = tipovehiculo;
        this.tiposervicio = tiposervicio;
        this.cliente = cliente;
        this.zona = zona; 
        this.observaciones = observaciones;
        this.estado = estado;
    }

    

    public int getIdingreso() {
        return idingreso;
    }

    public void setIdingreso(int idingreso) {
        this.idingreso = idingreso;
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

    public String getEmpleado() {
        return empleado;
    }

    public void setEmpleado(String empleado) {
        this.empleado = empleado;
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public String getFechaingreso() {
        return fechaingreso;
    }

    public void setFechaingreso(String fechaingreso) {
        this.fechaingreso = fechaingreso;
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

    public String getCliente() {
        return cliente;
    }

    public void setCliente(String cliente) {
        this.cliente = cliente;
    }

    public String getZona() {
        return zona;
    }

    public void setZona(String zona) {
        this.zona = zona;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

   
    

}
