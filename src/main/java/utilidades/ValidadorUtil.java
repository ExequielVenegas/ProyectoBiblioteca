package utilidades; // Cambiado de 'default package' a 'utilidades'

import static constantes.Constantes.*;

public class ValidadorUtil {
    private ValidadorUtil() {}

    public static void validarIsbn(String isbn) {
        if (isbn == null || isbn.trim().isEmpty()) {
            throw new IllegalArgumentException("El ISBN no puede ser nulo o vacío.");
        }
        // La excepción se lanza si NO cumple con esto.
        if (isbn.length() != LENGTH_ISBN) {
            throw new IllegalArgumentException("El ISBN debe tener exactamente " + LENGTH_ISBN + " dígitos.");
        }
        if (!isbn.matches("\\d+")) { // Verifica que solo contenga dígitos
            throw new IllegalArgumentException("El ISBN debe contener solo números.");
        }
    }

}