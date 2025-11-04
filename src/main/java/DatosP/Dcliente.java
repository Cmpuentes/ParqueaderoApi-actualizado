package DatosP;

public class Dcliente {

    public int idcliente;
    public String fecha;
    public String placa;
    public String tipo_vehiculo;
    public String tipo_servicio;
    public String cliente;
    public String telefono;
    public int tarifas;
    public String estado;
    public String observaciones;

    public Dcliente() {
    }

    public Dcliente(int idcliente, String fecha, String placa, String tipo_vehiculo, String tipo_servicio, String cliente, String telefono, int tarifas, String estado, String observaciones) {
        this.idcliente = idcliente;
        this.fecha = fecha;
        this.placa = placa;
        this.tipo_vehiculo = tipo_vehiculo;
        this.tipo_servicio = tipo_servicio;
        this.cliente = cliente;
        this.telefono = telefono;
        this.tarifas = tarifas;
        this.estado = estado;
        this.observaciones = observaciones;
    }
public Dcliente(int tarifas) {
        this.tarifas = tarifas;
        
    }
    public int getIdcliente() {
        return idcliente;
    }

    public void setIdcliente(int idcliente) {
        this.idcliente = idcliente;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public String getTipo_vehiculo() {
        return tipo_vehiculo;
    }

    public void setTipo_vehiculo(String tipo_vehiculo) {
        this.tipo_vehiculo = tipo_vehiculo;
    }

    public String getTipo_servicio() {
        return tipo_servicio;
    }

    public void setTipo_servicio(String tipo_servicio) {
        this.tipo_servicio = tipo_servicio;
    }

    public String getCliente() {
        return cliente;
    }

    public void setCliente(String cliente) {
        this.cliente = cliente;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public int getTarifas() {
        return tarifas;
    }

    public void setTarifas(int tarifas) {
        this.tarifas = tarifas;
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

}
