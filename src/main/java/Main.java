import constantes.Constantes;
import excepciones.LibroNoEncontradoException;
import excepciones.LibroYaPrestadoException;
import modelos.Libro;
import modelos.Usuario;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        //TODO: REALIZAR PRUEBAS Y REFACTOR PARA COMPLETAR EL PROYECTO
        Biblioteca miBiblioteca = new Biblioteca();
        Scanner scanner = new Scanner(System.in);

        //BORRAR SI ES NECESARIO
        miBiblioteca.agregarLibro(new Libro("Cien años de soledad", "Gabriel García Márquez", "disponible"));
        miBiblioteca.agregarLibro(new Libro("1984", "George Orwell", "disponible"));
        miBiblioteca.agregarLibro(new Libro("Don Quijote de la Mancha", "Miguel de Cervantes", "prestado"));
        miBiblioteca.agregarUsuario(new Usuario("U001", "Ana Pérez"));
        miBiblioteca.agregarUsuario(new Usuario("U002", "Juan García"));

        miBiblioteca.cargarLibrosDesdeCSV(Constantes.LIBROS_CSV);


        int opcion = -1;

        do {
            System.out.println("\n--- Menú de la Biblioteca DUOC UC ---");
            System.out.println("1. Listar Libros");
            System.out.println("2. Buscar Libro");
            System.out.println("3. Prestar Libro");
            System.out.println("4. Devolver Libro");
            System.out.println("5. Agregar Nuevo Libro");
            System.out.println("6. Listar Usuarios");
            System.out.println("7. Agregar Nuevo Usuario");
            System.out.println("0. Salir");
            System.out.print("Seleccione una opción: ");

            try {
                opcion = scanner.nextInt();
                scanner.nextLine();

                switch (opcion) {
                    case 1:
                        listarLibros(miBiblioteca);
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
                    case 6:
                        listarUsuarios(miBiblioteca);
                        break;
                    case 7:
                        ingresarNuevoUsuario(scanner, miBiblioteca);
                        break;
                    case 0:
                        salir();
                        break;
                    default:
                        System.out.println("Opción no válida. Por favor, intente de nuevo.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Error: Entrada inválida. Por favor, ingrese un número para seleccionar una opción.");
                scanner.nextLine();
                opcion = -1;
            } catch (Exception e) {
                System.out.println("Ha ocurrido un error inesperado: " + e.getMessage());
                e.printStackTrace();
                opcion = -1;
            }

        } while (opcion != 0);

        scanner.close();
    }

    private static void salir() {
        System.out.println("Saliendo del programa. ¡Hasta luego!");
    }

    private static void ingresarNuevoUsuario(Scanner scanner, Biblioteca miBiblioteca) {
        System.out.print("Ingrese el ID del nuevo usuario: ");
        String nuevoIdUsuario = scanner.nextLine();
        System.out.print("Ingrese el nombre del nuevo usuario: ");
        String nuevoNombreUsuario = scanner.nextLine();
        miBiblioteca.agregarUsuario(new Usuario(nuevoIdUsuario, nuevoNombreUsuario));
    }

    private static void listarUsuarios(Biblioteca miBiblioteca) {
        miBiblioteca.listarUsuarios();
    }

    private static void ingresarNuevoLibro(Scanner scanner, Biblioteca miBiblioteca) {
        System.out.print("Ingrese el título del nuevo libro: ");
        String nuevoTitulo = scanner.nextLine();
        System.out.print("Ingrese el autor del nuevo libro: ");
        String nuevoAutor = scanner.nextLine();
        miBiblioteca.agregarLibro(new Libro(nuevoTitulo, nuevoAutor, "disponible"));
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

    private static void listarLibros(Biblioteca miBiblioteca) {
        miBiblioteca.listarLibros();
    }
}