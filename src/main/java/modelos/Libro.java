package modelos;

import utilidades.ValidadorUtil;

import java.util.Objects;
import static utilidades.ValidadorUtil.*;

public class Libro {
    private String titulo;
    private String autor;
    private String isbn; // codigo unico por libro 13 digitos

    private String estado; // "disponible" o "prestado"

    public Libro(String titulo, String autor, String isbn, String estado) {
        this.titulo = Objects.requireNonNull(titulo, "Debe ingresar un título.").trim();
        this.autor = Objects.requireNonNull(autor, "Debe ingresar Autor").trim();
        this.isbn=Objects.requireNonNull(isbn, "Debe ingresar un ISBN de 13 dígitos").trim();
        this.estado = Objects.requireNonNull(estado, "Debe ingresar estado del libro").trim();

     //   ValidadorUtil.validarIsbn(isbn);
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

    public String getIsbn (){return isbn;}

    public void setEstado(String estado) {
        this.estado = estado;
    }
    public void setIsbn (String isbn){
        this.isbn=isbn;
    }


    @Override
    public String toString() {
        return  "\nISBN: " + isbn +  "\nTÍTULO: " + titulo + "\nAUTOR: " + autor + "\nESTADO: " + estado + "\n";
    }
}