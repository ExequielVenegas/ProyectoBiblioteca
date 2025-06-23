package modelos;

import java.util.Objects;

public class Usuario implements Comparable<Usuario> {
    private String id;
    private String nombre;
    private String email;

    public Usuario(String id, String nombre, String email) {
        this.id = Objects.requireNonNull(id, "El ID del usuario no puede ser nulo.").trim();
        this.nombre = Objects.requireNonNull(nombre, "El nombre del usuario no puede ser nulo.").trim();
        // Validar y normalizar el email
        if (email != null && !email.trim().isEmpty()) {
            this.email = email.trim().toLowerCase();
        } else {
            this.email = null;
        }
    }

    public String getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        if (email != null && !email.trim().isEmpty()) {
            this.email = email.trim().toLowerCase();
        } else {
            this.email = null;
        }
    }

    @Override
    public String toString() {
        return "ID: '" + id + "', Nombre: '" + nombre + "', Email: '" + (email != null ? email : "N/A") + "'";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Usuario usuario = (Usuario) o;
        return id.equals(usuario.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public int compareTo(Usuario otroUsuario) {
        return this.nombre.compareToIgnoreCase(otroUsuario.nombre);
    }
}