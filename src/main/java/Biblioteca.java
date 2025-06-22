import constantes.Constantes;
import excepciones.LibroNoEncontradoException;
import excepciones.LibroYaPrestadoException;
import modelos.Libro;
import modelos.Usuario;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

public class Biblioteca {
    private ArrayList<Libro> libros;
    private HashMap<String, Usuario> usuarios;

    public Biblioteca() {
        this.libros = new ArrayList<>();
        this.usuarios = new HashMap<>();
    }

    public void agregarLibro(Libro libro) {
        libros.add(libro);
        System.out.println("Libro '" + libro.getTitulo() + "' agregado.");
        actualizarInventario(Constantes.INVENTARIO_CSV,libros);
    }

    public void eliminarLibro(String titulo) throws LibroNoEncontradoException{
        Libro libro = buscarLibro(titulo);
        libros.remove(libro);
        System.out.println("Libro " + libro.getTitulo() +  " eliminado.");
        actualizarInventario(Constantes.INVENTARIO_CSV,libros);
    }

    public void agregarUsuario(Usuario usuario) {
        if (!usuarios.containsKey(usuario.getId())) {
            usuarios.put(usuario.getId(), usuario);
            System.out.println("Usuario '" + usuario.getNombre() + "' (ID: " + usuario.getId() + ") agregado.");
        } else {
            System.out.println("Error: Ya existe un usuario con el ID " + usuario.getId());
        }
    }

    public Libro buscarLibro(String titulo) throws LibroNoEncontradoException {
        for (Libro libro : libros) {
            if (libro.getTitulo().equalsIgnoreCase(titulo)) {
                return libro;
            }
        }
        throw new LibroNoEncontradoException("El libro '" + titulo + "' no fue encontrado en la biblioteca.");
    }

    public void prestarLibro(String titulo) throws LibroNoEncontradoException, LibroYaPrestadoException {
        Libro libro = buscarLibro(titulo);

        if (libro.getEstado().equalsIgnoreCase("prestado")) {
            throw new LibroYaPrestadoException("El libro '" + titulo + "' ya se encuentra prestado.");
        }

        libro.setEstado("prestado");
        String detallePrestamo = "Libro: '" + titulo +  "' ISBN: " + libro.getIsbn() + " ESTADO: Prestado el " + java.time.LocalDate.now();
        System.out.println("Libro '" + titulo + "' ISBN: " + libro.getIsbn() +  " prestado exitosamente.");
        guardarDetallesPrestamo(detallePrestamo);
        actualizarInventario(Constantes.INVENTARIO_CSV,libros);
    }

    public void devolverLibro(String titulo) throws LibroNoEncontradoException {
        Libro libro = buscarLibro(titulo);

        if (libro.getEstado().equalsIgnoreCase("disponible")) {
            System.out.println("El libro '" + titulo + "' ya está disponible. No necesita ser devuelto.");
            return;
        }

        libro.setEstado("disponible");
        String detalleDevolucion = "Libro: '" + titulo +  "' ISBN: " + libro.getIsbn() + " ESTADO: Devuelto el " + java.time.LocalDate.now();
        System.out.println("Libro '" + titulo + "' ISBN: " + libro.getIsbn() +  " devuelto exitosamente.");
        guardarDetallesDevolucion(detalleDevolucion);
        actualizarInventario(Constantes.INVENTARIO_CSV,libros);

    }

    public void listarLibros() {
        if (libros.isEmpty()) {
            System.out.println("No hay libros registrados en la biblioteca.");
            return;
        }
        System.out.println("\n--- Libros en la Biblioteca ---");
        for (Libro libro : libros) {
            System.out.println(libro);
        }
        System.out.println("------------------------------");
    }

    public void listarUsuarios() {
        if (usuarios.isEmpty()) {
            System.out.println("No hay usuarios registrados.");
            return;
        }
        System.out.println("\n--- Usuarios Registrados ---");
        for (Usuario usuario : usuarios.values()) {
            System.out.println(usuario);
        }
        System.out.println("----------------------------");
    }
    public void cargarLibrosDesdeCSV(String rutaArchivo) {
        try (BufferedReader br = new BufferedReader(new FileReader(rutaArchivo))) {
            String line;
            System.out.println("Cargando inventario desde: " + rutaArchivo);
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length == 4) {
                    String titulo = data[0].trim();
                    String autor = data[1].trim();
                    String isbn = data[2].trim();
                    String estado = data[3].trim();
                    libros.add(new Libro(titulo, autor, isbn, estado));
                } else {
                    System.out.println("Advertencia: Línea mal formateada en CSV (se esperan 4 campos: Título, Autor, ISBN, disponible/no disponible): " + line);
                }
            }
            System.out.println("Libros cargados exitosamente desde '" + rutaArchivo + "'.");
        } catch (FileNotFoundException e) {
            System.out.println("Error: Archivo CSV no encontrado en la ruta '" + rutaArchivo + "'.");
        } catch (IOException e) {
            System.out.println("Error de lectura al cargar libros desde CSV: " + e.getMessage());
        }
    }

    public void guardarDetallesPrestamo(String detalles) {
        try (FileWriter fw = new FileWriter("src/main/resources/prestamos_log.txt", true)) {
            fw.write(detalles + System.lineSeparator());
            System.out.println("Log: Detalles de préstamo guardados en 'prestamos_log.txt'.");
        } catch (IOException e) {
            System.out.println("Error al guardar detalles de préstamo en archivo: " + e.getMessage());
        }
    }
    public void guardarDetallesDevolucion(String detalles) {
        try (FileWriter fw = new FileWriter("src/main/resources/prestamos_log.txt", true)) {
            fw.write(detalles + System.lineSeparator());
            System.out.println("Log: Detalles de devolución guardados en 'prestamos_log.txt'.");
        } catch (IOException e) {
            System.out.println("Error al guardar detalles de devolución en archivo: " + e.getMessage());
        }
    }

    public ArrayList<Libro> getLibros() {
        return libros;
    }

    public void setLibros(ArrayList<Libro> libros) {
        this.libros = libros;
    }

    public void actualizarInventario (String rutaArchivo, ArrayList<Libro> libros){
        try (FileWriter fw = new FileWriter(rutaArchivo)) {
            for(Libro libro : libros){
                String linea =libro.getTitulo() + "," + libro.getAutor() + "," + libro.getIsbn() + "," + libro.getEstado() + "\n";
                fw.write(linea);
            }
            fw.flush();
            System.out.println("Log: Inventario actualizado.");
        } catch (IOException e) {
            System.out.println("Error al actualizar el inventario : " + e.getMessage());
        }


    }
}