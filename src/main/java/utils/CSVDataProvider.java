package utils;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class CSVDataProvider {
    private static final String DATA_DIR = Paths.get("resources").toString();

    public static Object[][] readDatosPasajeros() {
        return readCSVFile("datos_pasajeros.csv");
    }

    public static Object[][] readDatosPasajerosError15d() {
        return readCSVFile("datos_pasajeros_error_15d.csv");
    }

    public static Object[][] readDatos50€Tarde() {
        return readCSVFile("datos_pasajeros_50_evenings.csv");
    }

    public static Object[][] readDatosPasajerosBlankPaymentData5d() {
        return readCSVFile("datos_pasajeros_blank_payment_data_5d.csv");
    }

    public static Object[][] readPreciosTrayectos() {
        return readCSVFile("precios_trayectos.csv");
    }

    private static Object[][] readCSVFile(String filename) {
        Path filePath = Paths.get(DATA_DIR, filename);

        // Verificar que el archivo existe
        if (!Files.exists(filePath)) {
            throw new RuntimeException("Archivo no encontrado: " + filePath);
        }

        // Verificar que no esté vacío
        try {
            if (Files.size(filePath) == 0) {
                throw new RuntimeException("Archivo vacío: " + filePath);
            }
        } catch (IOException e) {
            throw new RuntimeException("Error verificando tamaño del archivo: " + filePath, e);
        }

        try (CSVReader reader = new CSVReader(new FileReader(filePath.toFile()))) {
            List<String[]> allData = reader.readAll()
                    .stream()
                    .filter(row -> !Arrays.stream(row).allMatch(String::isEmpty)) // Filtrar filas vacías
                    .collect(Collectors.toList());

            if (allData.isEmpty()) {
                throw new RuntimeException("No hay datos válidos en el archivo CSV: " + filePath);
            }

            // Determinar si hay encabezados (asumimos que la primera fila es encabezado si contiene texto)
            boolean hasHeader = Arrays.stream(allData.get(0))
                    .anyMatch(cell -> cell != null && !cell.trim().isEmpty() && cell.matches(".*[a-zA-Z].*"));

            int startRow = hasHeader ? 1 : 0;
            int rowCount = allData.size() - startRow;

            if (rowCount == 0) {
                throw new RuntimeException("Solo hay encabezados en el archivo CSV: " + filePath);
            }

            // Convertir a array bidimensional
            Object[][] testData = new Object[rowCount][];
            for (int i = startRow; i < allData.size(); i++) {
                testData[i - startRow] = allData.get(i);
            }

            return testData;

        } catch (IOException | CsvException e) {
            throw new RuntimeException("Error leyendo archivo CSV: " + filePath, e);
        }
    }
}