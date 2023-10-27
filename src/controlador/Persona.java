package src.controlador;

public class Persona {
	public String tipoPersona, id, nombre;
	public Persona(String tipoPersona, String id, String nombre) {
		this.tipoPersona = tipoPersona;
		this.id = id;
		this.nombre = nombre;
	}
	@Override
	public String toString() {
		return "Persona [tipoPersona=" + tipoPersona + ", id=" + id + ", getClass()=" + getClass() + ", hashCode()="
				+ hashCode() + ", toString()=" + super.toString() + "]";
	}
}
