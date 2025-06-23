import constantes.Constantes;
import excepciones.LibroNoEncontradoException;
import excepciones.LibroYaPrestadoException;
import gestores.Biblioteca; // Importar Biblioteca desde su nuevo paquete
import modelos.Libro;
import modelos.Usuario;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Biblioteca miBiblioteca = new Biblioteca();
        Scanner scanner = new Scanner(System.in);

        int opcion = -1;

        do {
            System.out.println("\n--- Menú de la Biblioteca DUOC UC ---");
            System.out.println("1. Listar Libros");
            System.out.println("2. Buscar Libro");
            System.out.println("3. Prestar Libro");
            System.out.println("4. Devolver Libro");
            System.out.println("5. Agregar Nuevo Libro");
            System.out.println("6. Eliminar libro del inventario");
            System.out.println("7. Listar Usuarios (por ID)");
            System.out.println("8. Listar Usuarios (ordenados por nombre)");
            System.out.println("9. Agregar Nuevo Usuario");
            System.out.println("0. Salir");
            System.out.print("Seleccione una opción: ");

            try {
                opcion = scanner.nextInt();
                scanner.nextLine(); // Consumir el salto de línea

                switch (opcion) {
                    case 1:
                        miBiblioteca.listarLibros();
                        break;
                    case 2:
                        buscarLibro(scanner, miBiblioteca);
                        break;
                    case 3:
                        prestarLibro(scanner, miBiblioteca);
                        break;
                    case 4:
                        devolverLibro(scanner, miBiblioteca);
                        break;
                    case 5:
                        ingresarNuevoLibro(scanner, miBiblioteca);
                        break;
                    case 7:
                        miBiblioteca.listarUsuarios();
                        break;
                    case 9:
                        ingresarNuevoUsuario(scanner, miBiblioteca);
                        break;
                    case 6:
                        eliminarLibroInventario(scanner, miBiblioteca);
                        break;
                    case 8:
                        miBiblioteca.listarUsuariosOrdenadosPorNombre();
                        break;
                    case 10: // Nuevo caso para buscar usuario
                        buscarUsuario(scanner, miBiblioteca);
                        break;
                    case 11: // Nuevo caso para eliminar usuario
                        eliminarUsuario(scanner, miBiblioteca);
                        break;
                    case 0:
                        System.out.println("Saliendo del sistema. ¡Hasta pronto!");
                        break;
                    default:
                        System.out.println("Opción no válida. Por favor, intente de nuevo.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Error: Entrada inválida. Por favor, ingrese un número.");
                scanner.nextLine(); // Limpiar el buffer del scanner
                opcion = -1; // Para que el bucle continúe
            }
        } while (opcion != 0);

        scanner.close();
    }

    private static void ingresarNuevoLibro(Scanner scanner, Biblioteca miBiblioteca) {
        System.out.print("Ingrese el título del nuevo libro: ");
        String titulo = scanner.nextLine();
        System.out.print("Ingrese el autor del nuevo libro: ");
        String autor = scanner.nextLine();
        System.out.print("Ingrese el ISBN del nuevo libro (13 dígitos): ");
        String isbn = scanner.nextLine();
        // El estado inicial de un libro nuevo siempre será "disponible"
        miBiblioteca.agregarLibro(new Libro(titulo, autor, isbn, "Disponible"));
    }

    private static void eliminarLibroInventario(Scanner scanner, Biblioteca miBiblioteca) {
        System.out.print("Ingrese el título del libro a eliminar: ");
        String tituloEliminar = scanner.nextLine();
        try {
            miBiblioteca.eliminarLibro(tituloEliminar);
        } catch (LibroNoEncontradoException e) {
            System.out.println("Error al eliminar: " + e.getMessage());
        }
    }

    private static void devolverLibro(Scanner scanner, Biblioteca miBiblioteca) {
        System.out.print("Ingrese el título del libro a devolver: ");
        String tituloDevolver = scanner.nextLine();
        try {
            miBiblioteca.devolverLibro(tituloDevolver);
        } catch (LibroNoEncontradoException e) {
            System.out.println("Error al devolver: " + e.getMessage());
        }
    }

    private static void prestarLibro(Scanner scanner, Biblioteca miBiblioteca) {
        System.out.print("Ingrese el título del libro a prestar: ");
        String tituloPrestar = scanner.nextLine();
        try {
            miBiblioteca.prestarLibro(tituloPrestar);
        } catch (LibroNoEncontradoException e) {
            System.out.println("Error al prestar: " + e.getMessage());
        } catch (LibroYaPrestadoException e) {
            System.out.println("Error al prestar: " + e.getMessage());
        }
    }

    private static void buscarLibro(Scanner scanner, Biblioteca miBiblioteca) {
        System.out.print("Ingrese el título del libro a buscar: ");
        String tituloBuscar = scanner.nextLine();
        try {
            Libro encontrado = miBiblioteca.buscarLibro(tituloBuscar);
            System.out.println("Libro encontrado: " + encontrado);
        } catch (LibroNoEncontradoException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void ingresarNuevoUsuario(Scanner scanner, Biblioteca miBiblioteca) {
        System.out.print("Ingrese el ID del nuevo usuario: ");
        String nuevoIdUsuario = scanner.nextLine();
        System.out.print("Ingrese el nombre del nuevo usuario: ");
        String nuevoNombreUsuario = scanner.nextLine();
        System.out.print("Ingrese el email del nuevo usuario: ");
        String nuevoEmailUsuario = scanner.nextLine(); // Leer el email
        miBiblioteca.agregarUsuario(new Usuario(nuevoIdUsuario, nuevoNombreUsuario, nuevoEmailUsuario));
    }

    private static void buscarUsuario(Scanner scanner, Biblioteca miBiblioteca) {
        System.out.print("Ingrese el ID del usuario a buscar: ");
        String idBuscar = scanner.nextLine();
        Usuario usuarioEncontrado = miBiblioteca.buscarUsuarioPorId(idBuscar);
        if (usuarioEncontrado != null) {
            System.out.println("Usuario encontrado: " + usuarioEncontrado);
        } else {
            System.out.println("Usuario con ID '" + idBuscar + "' no encontrado.");
        }
    }

    private static void eliminarUsuario(Scanner scanner, Biblioteca miBiblioteca) {
        System.out.print("Ingrese el ID del usuario a eliminar: ");
        String idEliminar = scanner.nextLine();
        if (miBiblioteca.eliminarUsuario(idEliminar)) {
            System.out.println("Usuario eliminado con éxito.");
        } else {
            System.out.println("No se pudo eliminar el usuario.");
        }
    }
}