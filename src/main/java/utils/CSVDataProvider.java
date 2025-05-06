package utils;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;

import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

public class CSVDataProvider {
    private static final String BASE_PATH = Paths.get("src", "main", "java", "utils", "data").toAbsolutePath().toString() + "/";
    /**
     * Lee datos de pasajeros desde el archivo CSV correspondiente
     */
    public static Object[][] readDatosPasajeros() {
        return readCSV(BASE_PATH + "datos_pasajeros.csv");
    }

    /**
     * Lee datos de pasajeros desde el archivo CSV correspondiente con 15 días adelante en el viaje y con un error en el nombre del pasajero.
     */
    public static Object[][] readDatosPasajerosError15d() { return readCSV(BASE_PATH + "datos_pasajeros_error_15d.csv");}

    /**
     * Lee datos de pasajeros desde el archivo CSV correspondiente con 5 días hacia adelante en el viaje y sin datos de pago.
     */
    public static Object[][] readDatosPasajerosBlankPaymentData5d() {return readCSV(BASE_PATH + "datos_pasajeros_blank_payment_data_5d.csv");}

    /**
     * Lee precios de trayectos desde el archivo CSV correspondiente.
     */
    public static Object[][] readPreciosTrayectos() {
        return readCSV(BASE_PATH + "precios_trayectos.csv");
    }

    /**
     * Método genérico para leer cualquier archivo CSV
     * @param filePath Ruta completa del archivo CSV
     */
    public static Object[][] readCSV(String filePath) {
        try (CSVReader reader = new CSVReader(new FileReader(filePath))) {
            List<String[]> data = reader.readAll();

            if (data.isEmpty()) {
                throw new RuntimeException("El archivo CSV está vacío: " + filePath);
            }

            int startRow = hasHeader(data) ? 1 : 0;
            Object[][] testData = new Object[data.size() - startRow][];

            for (int i = startRow; i < data.size(); i++) {
                testData[i - startRow] = data.get(i);
            }

        } catch (IOException | CsvException e) {
            throw new RuntimeException("Error al leer el archivo CSV: " + filePath, e);
        }
        return new Object[0][];
    }

    /**
     * Metodo para obtener rutas relativas (opcional)
     */
    public static String getRelativePath(String fileName) {
        return Paths.get("src", "main", "java", "utils", "data", fileName).toString();
    }

    private static boolean hasHeader(List<String[]> data) {
        if (data.isEmpty()) return false;
        for (String cell : data.get(0)) {
            if (cell.matches(".*[a-zA-Z].*")) {
                return true;
            }
        }
        return false;
    }
}