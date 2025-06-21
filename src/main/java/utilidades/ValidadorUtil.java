package utilidades;
import static constantes.Constantes.*;

public class ValidadorUtil {
    private ValidadorUtil (){}

    public static  void validarIsbn(String isbn) {
        if (isbn.length() != LENGTH_ISBN) {
            throw new IllegalArgumentException("El ISBN debe tener exactamente 13 dígitos");
        }
        if (isbn.matches("\\d{13}")) {
            throw new IllegalArgumentException("El ISBN debe tener solo números.");
        }

    }

}
