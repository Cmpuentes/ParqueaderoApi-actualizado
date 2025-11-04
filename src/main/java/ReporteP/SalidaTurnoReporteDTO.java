
package ReporteP;

/**
 *
 * @author JEFE CIBERSEGURIDA
 */
public class SalidaTurnoReporteDTO {
        private String turno;
    private String numeroturno;
    private String empleado;
    private String fechaingreso;
    private String fechasalida;
    private int totalvehiculos;
    private int base;
    private int efectivo;
    private int tarjeta;
    private int transferencia;
    private int otrosingresos;
    private int efectivoliquido;
    private int totalrecaudado;
    private String observaciones;
    private int totalabonos;

    public SalidaTurnoReporteDTO(String turno, String numeroturno, String empleado, String fechaingreso, String fechasalida, int totalvehiculos, int base, int efectivo, int tarjeta, int transferencia, int otrosingresos, int efectivoliquido, int totalrecaudado, String observaciones, int totalabonos) {
        this.turno = turno;
        this.numeroturno = numeroturno;
        this.empleado = empleado;
        this.fechaingreso = fechaingreso;
        this.fechasalida = fechasalida;
        this.totalvehiculos = totalvehiculos;
        this.base = base;
        this.efectivo = efectivo;
        this.tarjeta = tarjeta;
        this.transferencia = transferencia;
        this.otrosingresos = otrosingresos;
        this.efectivoliquido = efectivoliquido;
        this.totalrecaudado = totalrecaudado;
        this.observaciones = observaciones;
        this.totalabonos = totalabonos;
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

    public String getFechaingreso() {
        return fechaingreso;
    }

    public void setFechaingreso(String fechaingreso) {
        this.fechaingreso = fechaingreso;
    }

    public String getFechasalida() {
        return fechasalida;
    }

    public void setFechasalida(String fechasalida) {
        this.fechasalida = fechasalida;
    }

    public int getTotalvehiculos() {
        return totalvehiculos;
    }

    public void setTotalvehiculos(int totalvehiculos) {
        this.totalvehiculos = totalvehiculos;
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

    public int getOtrosingresos() {
        return otrosingresos;
    }

    public void setOtrosingresos(int otrosingresos) {
        this.otrosingresos = otrosingresos;
    }

    public int getEfectivoliquido() {
        return efectivoliquido;
    }

    public void setEfectivoliquido(int efectivoliquido) {
        this.efectivoliquido = efectivoliquido;
    }

    public int getTotalrecaudado() {
        return totalrecaudado;
    }

    public void setTotalrecaudado(int totalrecaudado) {
        this.totalrecaudado = totalrecaudado;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public int getTotalabonos() {
        return totalabonos;
    }

    public void setTotalabonos(int totalabonos) {
        this.totalabonos = totalabonos;
    }
    
    
}
