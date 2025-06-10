public class Libro {
    private String titulo;
    private String autor;
    private String estado; // "disponible" o "prestado"

    public Libro(String titulo, String autor, String estado) {
        this.titulo = titulo;
        this.autor = autor;
        this.estado = estado;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getAutor() {
        return autor;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    @Override
    public String toString() {
        return "Título: '" + titulo + "', Autor: '" + autor + "', Estado: " + estado;
    }
}