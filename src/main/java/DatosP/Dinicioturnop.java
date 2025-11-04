package DatosP;

public class Dinicioturnop {

    public int idturno;
    public String fechainicio;
    public String turno;
    public int numeroturno;
    public String estado;
    public String empleado;

    public Dinicioturnop() {
    }

    public Dinicioturnop(int idturno, String fechainicio, String turno, int numeroturno, String estado, String empleado) {
        this.idturno = idturno;
        this.fechainicio = fechainicio;
        this.turno = turno;
        this.numeroturno = numeroturno;
        this.estado = estado;
        this.empleado = empleado;
    }
    
    public Dinicioturnop(String empleado, String fechainicio, String turno, int numeroturno, String estado){
        this.empleado = empleado;
        this.fechainicio = fechainicio;
        this.turno = turno;
        this.numeroturno = numeroturno;
        this.estado = estado;
    }

    public Dinicioturnop( int numeroturno, int idturno) {
        this.numeroturno = numeroturno;
        this.idturno = idturno;
    }

    public Dinicioturnop(int numeroturno, String empleado) {
        this.numeroturno = numeroturno;
        this.empleado = empleado;
    }

    public int getIdturno() {
        return idturno;
    }

    public void setIdturno(int idturno) {
        this.idturno = idturno;
    }

    public String getFechainicio() {
        return fechainicio;
    }

    public void setFechainicio(String fechainicio) {
        this.fechainicio = fechainicio;
    }

    public String getTurno() {
        return turno;
    }

    public void setTurno(String turno) {
        this.turno = turno;
    }

    public int getNumeroturno() {
        return numeroturno;
    }

    public void setNumeroturno(int numeroturno) {
        this.numeroturno = numeroturno;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getEmpleado() {
        return empleado;
    }

    public void setEmpleado(String empleado) {
        this.empleado = empleado;
    }
    


}
