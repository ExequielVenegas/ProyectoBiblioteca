package gestores; // Asegúrate de que el paquete sea 'gestores'

import constantes.Constantes;
import excepciones.LibroNoEncontradoException;
import excepciones.LibroYaPrestadoException;
import modelos.Libro;
import modelos.Usuario;
import utilidades.CsvUtil;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.TreeSet;

public class Biblioteca {
    private ArrayList<Libro> libros;
    private HashMap<String, Usuario> usuarios;
    private HashSet<String> isbnsRegistrados;
    private HashSet<String> emailsRegistrados;

    public Biblioteca() {
        this.libros = new ArrayList<>();
        this.usuarios = new HashMap<>();
        this.isbnsRegistrados = new HashSet<>();
        this.emailsRegistrados = new HashSet<>();
        cargarLibrosDesdeCSV();
        cargarUsuariosDesdeCSV();
    }

    public void agregarLibro(Libro libro) {
        if (isbnsRegistrados.contains(libro.getIsbn())) {
            System.out.println("El libro con el ISBN: " + libro.getIsbn() + " ya existe. No se puede registrar el mismo ISBN nuevamente.");
            return;
        }
        libros.add(libro);
        isbnsRegistrados.add(libro.getIsbn());
        System.out.println("Libro '" + libro.getTitulo() + "' agregado.");
        guardarInventario();
    }

    public void eliminarLibro(String titulo) throws LibroNoEncontradoException {
        Libro libro = buscarLibro(titulo);
        libros.remove(libro);
        isbnsRegistrados.remove(libro.getIsbn());
        System.out.println("Libro '" + libro.getTitulo() + "' eliminado.");
        guardarInventario();
    }

    public void agregarUsuario(Usuario usuario) {
        // Verificar si el ID ya existe
        if (usuarios.containsKey(usuario.getId())) {
            System.out.println("Error: Ya existe un usuario con el ID " + usuario.getId());
            return;
        }

        // Verificar unicidad del email (si el email no es nulo)
        if (usuario.getEmail() != null && !usuario.getEmail().isEmpty()) { // Añadir chequeo de vacío
            if (emailsRegistrados.contains(usuario.getEmail())) {
                System.out.println("Error: El email '" + usuario.getEmail() + "' ya está registrado por otro usuario.");
                return;
            }
        }

        // Si ID y email (si existe) son únicos, agregar al HashMap de usuarios
        usuarios.put(usuario.getId(), usuario);
        // Si el usuario tiene un email, agregarlo al HashSet de emails registrados
        if (usuario.getEmail() != null && !usuario.getEmail().isEmpty()) {
            emailsRegistrados.add(usuario.getEmail());
        }
        guardarUsuarios();
    }

    // Metodo buscarLibro
    public Libro buscarLibro(String titulo) throws LibroNoEncontradoException {
        for (Libro libro : libros) {
            if (libro.getTitulo().equalsIgnoreCase(titulo)) {
                return libro;
            }
        }
        throw new LibroNoEncontradoException("Libro con título '" + titulo + "' no encontrado.");
    }

    public void prestarLibro(String titulo) throws LibroNoEncontradoException, LibroYaPrestadoException {
        Libro libro = buscarLibro(titulo);
        if (libro.getEstado().equalsIgnoreCase("prestado")) {
            throw new LibroYaPrestadoException("El libro '" + titulo + "' ya se encuentra prestado.");
        }
        libro.setEstado("Prestado");
        String detalles = "Préstamo: '" + libro.getTitulo() + "' (ISBN: " + libro.getIsbn() + ") - Fecha: " + LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE);
        guardarDetallesPrestamo(detalles);
        System.out.println("Libro '" + titulo + "' prestado.");
        guardarInventario(); // Guardar cambios de estado
    }

    public void devolverLibro(String titulo) throws LibroNoEncontradoException {
        Libro libro = buscarLibro(titulo);
        if (libro.getEstado().equalsIgnoreCase("disponible")) {
            System.out.println("El libro '" + titulo + "' ya estaba disponible.");
            return;
        }
        libro.setEstado("Disponible");
        String detalles = "Devolución: '" + libro.getTitulo() + "' (ISBN: " + libro.getIsbn() + ") - Fecha: " + LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE);
        guardarDetallesDevolucion(detalles);
        System.out.println("Libro '" + titulo + "' devuelto.");
        guardarInventario(); // Guardar cambios de estado
    }

    // Metodo listarLibros
    public void listarLibros() {
        if (libros.isEmpty()) {
            System.out.println("No hay libros registrados en la biblioteca.");
            return;
        }
        System.out.println("\n--- Libros en la Biblioteca ---");
        for (Libro libro : libros) {
            System.out.println(libro);
        }
    }

    // Metodo listarUsuarios (por ID)
    public void listarUsuarios() {
        if (usuarios.isEmpty()) {
            System.out.println("No hay usuarios registrados en la biblioteca.");
            return;
        }
        System.out.println("\n--- Usuarios Registrados (por ID) ---");
        for (Usuario usuario : usuarios.values()) {
            System.out.println(usuario);
        }
    }

    // Metodo listarUsuariosOrdenadosPorNombre
    public void listarUsuariosOrdenadosPorNombre() {
        if (usuarios.isEmpty()) {
            System.out.println("No hay usuarios registrados en la biblioteca para ordenar.");
            return;
        }
        System.out.println("\n--- Usuarios Registrados (Ordenados por Nombre) ---");
        // Copiar los usuarios del HashMap a un TreeSet
        TreeSet<Usuario> usuariosOrdenados = new TreeSet<>(usuarios.values());
        for (Usuario usuario : usuariosOrdenados) {
            System.out.println(usuario);
        }
    }

    public void cargarLibrosDesdeCSV() {
        try {
            List<Libro> librosCargados = CsvUtil.leerCsv(Constantes.INVENTARIO_CSV, datos -> {
                if (datos.length == 4) {
                    String titulo = datos[0].trim();
                    String autor = datos[1].trim();
                    String isbn = datos[2].trim();
                    String estado = datos[3].trim();
                    try {
                        return new Libro(titulo, autor, isbn, estado);
                    } catch (IllegalArgumentException e) {
                        System.err.println("Error al cargar libro (ISBN inválido): " + e.getMessage() + " - Línea: " + String.join(",", datos));
                        return null; // Retorna null para indicar que este libro no pudo ser cargado
                    }
                }
                System.err.println("Advertencia: Línea con formato incorrecto en CSV de inventario: " + String.join(",", datos));
                return null;
            });

            // Filtrar los libros nulos (que no se pudieron cargar por errores)
            // Y añadir solo los que no tienen ISBN duplicado ya en memoria
            for (Libro libro : librosCargados) {
                if (libro != null && !isbnsRegistrados.contains(libro.getIsbn())) {
                    libros.add(libro);
                    isbnsRegistrados.add(libro.getIsbn());
                } else if (libro != null) {
                    System.out.println("Advertencia: ISBN duplicado en CSV '" + libro.getIsbn() + "'. Saltando libro: " + libro.getTitulo());
                }
            }
        } catch (IOException e) {
            System.out.println("Error al cargar libros desde CSV: " + e.getMessage());
        }
    }

    public void guardarInventario() {
        try {
            CsvUtil.escribirCsv(Constantes.INVENTARIO_CSV, libros, libro ->
                            libro.getTitulo() + "," + libro.getAutor() + "," + libro.getIsbn() + "," + libro.getEstado(),
                    "Titulo,Autor,ISBN,Estado"
            );
        } catch (IOException e) {
            System.out.println("Error al actualizar el inventario: " + e.getMessage());
        }
    }

    // Métodos para guardar logs de préstamos/devoluciones
    public void guardarDetallesPrestamo(String detalles) {
        try (FileWriter fw = new FileWriter("src/main/resources/prestamos_log.txt", true)) {
            fw.write(detalles + System.lineSeparator());
        } catch (IOException e) {
            System.out.println("Error al guardar detalles de préstamo en archivo: " + e.getMessage());
        }
    }

    public void guardarDetallesDevolucion(String detalles) {
        try (FileWriter fw = new FileWriter("src/main/resources/prestamos_log.txt", true)) {
            fw.write(detalles + System.lineSeparator());
        } catch (IOException e) {
            System.out.println("Error al guardar detalles de devolución en archivo: " + e.getMessage());
        }
    }

    public void cargarUsuariosDesdeCSV() {
        try {
            List<Usuario> usuariosCargados = CsvUtil.leerCsv(Constantes.USUARIOS_CSV, datos -> {
                if (datos.length >= 3) {
                    String id = datos[0].trim();
                    String nombre = datos[1].trim();
                    String email = datos[2].trim();
                    return new Usuario(id, nombre, email);
                }
                System.err.println("Advertencia: Línea con formato incorrecto en CSV de usuarios: " + String.join(",", datos));
                return null;
            });

            // Añadir solo los usuarios válidos y no duplicados
            for (Usuario usuario : usuariosCargados) {
                if (usuario != null) { // Asegurarse de que el mapeo no devolvió null
                    this.agregarUsuario(usuario);
                }
            }
        } catch (IOException e) {
            System.out.println("Error al cargar usuarios desde CSV: " + e.getMessage());
        }
    }

    public void guardarUsuarios() {
        try {
            CsvUtil.escribirCsv(Constantes.USUARIOS_CSV, new ArrayList<>(usuarios.values()), usuario ->
                            usuario.getId() + "," + usuario.getNombre() + "," + (usuario.getEmail() != null ? usuario.getEmail() : ""),
                    "ID,Nombre,Email"
            );
        } catch (IOException e) {
            System.out.println("Error al guardar usuarios: " + e.getMessage());
        }
    }

    public Usuario buscarUsuarioPorId(String id) {
        return usuarios.get(id);
    }

    public boolean eliminarUsuario(String id) {
        Usuario usuarioEliminado = usuarios.remove(id); // Elimina del HashMap
        if (usuarioEliminado != null) {
            if (usuarioEliminado.getEmail() != null && !usuarioEliminado.getEmail().isEmpty()) {
                emailsRegistrados.remove(usuarioEliminado.getEmail()); // Elimina el email del HashSet de emails registrados
            }
            System.out.println("Usuario '" + usuarioEliminado.getNombre() + "' (ID: " + usuarioEliminado.getId() + ") eliminado.");
            return true;
        } else {
            System.out.println("Usuario con ID '" + id + "' no encontrado para eliminar.");
            return false;
        }
    }


}