package Clases;

public class CHuesped extends CPersona {

    private int idHuesped;
    private String telefono;
    private String correo;

    public CHuesped() {
        super();
    }

    public CHuesped(int idHuesped, String nombre, String apellido, String dni, String telefono, String correo) {
        super(idHuesped, nombre, apellido, dni);
        this.idHuesped = idHuesped;
        this.telefono = telefono;
        this.correo = correo;
    }

    @Override
    public String obtenerDescripcion() {
        return "Huésped: " + getNombre() + " " + getApellido() + " - DNI: " + getDni();
    }

    public int getIdHuesped() {
        return idHuesped;
    }

    public void setIdHuesped(int idHuesped) {
        this.idHuesped = idHuesped;
        this.idPersona = idHuesped;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }
}
