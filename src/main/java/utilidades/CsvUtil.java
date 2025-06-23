package utilidades;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class CsvUtil {

    private CsvUtil() {
    }

    public static <T> List<T> leerCsv(String rutaArchivo, Function<String[], T> mapper) throws IOException {
        List<T> registros = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(rutaArchivo))) {
            String linea;
            boolean primeraLinea = true; // Para saltar la cabecera si existe
            while ((linea = br.readLine()) != null) {
                if (primeraLinea) {
                    primeraLinea = false;
                    continue; // Saltar la primera línea (cabecera)
                }
                if (linea.trim().isEmpty()) {
                    continue; // Saltar líneas vacías
                }
                String[] campos = linea.split(",");
                registros.add(mapper.apply(campos));
            }
        }
        return registros;
    }

    public static <T> void escribirCsv(String rutaArchivo, List<T> datos, Function<T, String> mapper, String header) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(rutaArchivo))) {
            if (header != null && !header.trim().isEmpty()) {
                bw.write(header);
                bw.newLine();
            }
            for (T dato : datos) {
                bw.write(mapper.apply(dato));
                bw.newLine();
            }
            bw.flush(); // Asegurarse de que todos los datos se escriban al archivo
        }
    }
}