
package DatosP;


public class Dbloqueos {
    
  public  int idbloqueo;
    String tipo_bloqueo;
    String codigo;

    public Dbloqueos() {
    }

    public Dbloqueos(int idbloqueo, String tipo_bloqueo, String codigo) {
        this.idbloqueo = idbloqueo;
        this.tipo_bloqueo = tipo_bloqueo;
        this.codigo = codigo;
    }

    public int getIdbloqueo() {
        return idbloqueo;
    }

    public void setIdbloqueo(int idbloqueo) {
        this.idbloqueo = idbloqueo;
    }

    public String getTipo_bloqueo() {
        return tipo_bloqueo;
    }

    public void setTipo_bloqueo(String tipo_bloqueo) {
        this.tipo_bloqueo = tipo_bloqueo;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }
    
    
    
}
