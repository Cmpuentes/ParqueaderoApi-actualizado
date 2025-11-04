package DatosP;

public class Dempleadop {

    public int idempleado;
    public String nombres;
    public String apellidos;
    public String tipodocumento;
    public String documento;
    public String telefono;
    public String direccion;
    public String email;
    public String pais;
    public String ciudad;
    public String acceso;
    public String login;
    public String password;
    public String estado;
    public String eps;
    public String arl;

    public Dempleadop() {
        
    }


    public Dempleadop(int idempleado, String nombres, String apellidos, String tipodocumento, String documento, String telefono, String direccion, String email, String pais, String ciudad, String acceso, String login, String password, String estado, String eps, String arl) {
        this.idempleado = idempleado;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.tipodocumento = tipodocumento;
        this.documento = documento;
        this.telefono = telefono;
        this.direccion = direccion;
        this.email = email;
        this.pais = pais;
        this.ciudad = ciudad;
        this.acceso = acceso;
        this.login = login;
        this.password = password;
        this.estado = estado;
        this.eps = eps;
        this.arl = arl;
    }

    public Dempleadop(String acceso, String login, String password, String estado, String eps, String arl) {
        this.acceso = acceso;
        this.login = login;
        this.password = password;
        this.estado = estado;
        this.eps = eps;
        this.arl = arl;
    }

    public Dempleadop(int id, String nombres, String apellidos, String acceso, String loginUsuario, String passwordUsuario, String estado) {
        this.idempleado = id;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.acceso = acceso;
        this.login = loginUsuario;
        this.password = passwordUsuario;
        this.estado = estado;
    }

    public int getIdempleado() {
        return idempleado;
    }

    public void setIdempleado(int idempleado) {
        this.idempleado = idempleado;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getTipodocumento() {
        return tipodocumento;
    }

    public void setTipodocumento(String tipodocumento) {
        this.tipodocumento = tipodocumento;
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public String getAcceso() {
        return acceso;
    }

    public void setAcceso(String acceso) {
        this.acceso = acceso;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getEps() {
        return eps;
    }

    public void setEps(String eps) {
        this.eps = eps;
    }

    public String getArl() {
        return arl;
    }

    public void setArl(String arl) {
        this.arl = arl;
    }

}
