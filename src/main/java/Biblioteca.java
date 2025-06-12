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
        String detallePrestamo = "Libro: '" + titulo + "' prestado el " + java.time.LocalDate.now();
        System.out.println("Libro '" + titulo + "' prestado exitosamente.");
        guardarDetallesPrestamo(detallePrestamo);
    }

    public void devolverLibro(String titulo) throws LibroNoEncontradoException {
        Libro libro = buscarLibro(titulo);

        if (libro.getEstado().equalsIgnoreCase("disponible")) {
            System.out.println("El libro '" + titulo + "' ya está disponible. No necesita ser devuelto.");
            return;
        }

        libro.setEstado("disponible");
        System.out.println("Libro '" + titulo + "' devuelto exitosamente.");
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

    //TODO: CREAR MEJOR LOGICA PARA AGREGAR LIBROS AL INICIO Y GUARDAR ESTADOS DE LIBROS TRAS CERRAR PROGRAMA
    public void cargarLibrosDesdeCSV(String rutaArchivo) {
        try (BufferedReader br = new BufferedReader(new FileReader(rutaArchivo))) {
            String line;
            System.out.println("Intentando cargar libros desde: " + rutaArchivo);
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length == 3) {
                    String titulo = data[0].trim();
                    String autor = data[1].trim();
                    String estado = data[2].trim();
                    libros.add(new Libro(titulo, autor, estado));
                    System.out.println("Cargado: " + titulo);
                } else {
                    System.out.println("Advertencia: Línea mal formateada en CSV (se esperan 3 campos): " + line);
                }
            }
            System.out.println("Libros cargados exitosamente desde '" + rutaArchivo + "'.");
        } catch (FileNotFoundException e) {
            System.out.println("Error: Archivo CSV no encontrado en la ruta '" + rutaArchivo + "'.");
        } catch (IOException e) {
            System.out.println("Error de lectura al cargar libros desde CSV: " + e.getMessage());
        }
    }

    //TODO: REHACER LOGICA DE PRESTAMO PARA AGREGAR CUANDO SE DEVUELVE
    public void guardarDetallesPrestamo(String detalles) {
        try (FileWriter fw = new FileWriter("src/main/resources/prestamos_log.txt", true)) {
            fw.write(detalles + System.lineSeparator());
            System.out.println("Log: Detalles de préstamo guardados en 'prestamos_log.txt'.");
        } catch (IOException e) {
            System.out.println("Error al guardar detalles de préstamo en archivo: " + e.getMessage());
        }
    }
}