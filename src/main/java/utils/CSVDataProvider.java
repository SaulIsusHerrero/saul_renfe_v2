package utils;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;

import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class CSVDataProvider {
<<<<<<< Updated upstream
    private static final String BASE_PATH = "C:/Users/sisus_amaris.com/Desktop/repositorios/saul_renfe-main/src/main/java/utils/data/";
=======
    private static final String BASE_PATH = Paths.get("src", "main", "java", "utils", "data").toAbsolutePath().toString() + "/";
>>>>>>> Stashed changes

    /**
     * Lee datos desde el archivo CSV correspondiente y devuelve un Object[][] para el DataProvider.
     * Cada fila del CSV se convierte en un conjunto de pares clave-valor.
     */
    public static Object[][] readData(String fileName) {
        Object[][] rawData = readCSV(BASE_PATH + fileName);

        // Validar que el archivo CSV no esté vacío
        if (rawData == null || rawData.length == 0) {
            System.out.println("El archivo CSV no contiene datos válidos.");
            return new Object[0][0];
        }

        // Validar que haya al menos una fila de claves y una fila de valores
        if (rawData.length < 2) {
            System.out.println("El archivo CSV debe contener al menos una fila de claves y una fila de valores.");
            return new Object[0][0];
        }

        Object[][] testData = new Object[rawData.length - 1][1];

        // La primera fila contiene las claves
        String[] keys = (String[]) rawData[0];
        System.out.println("Claves encontradas: " + java.util.Arrays.toString(keys));

        // Las filas restantes contienen los valores
        for (int i = 1; i < rawData.length; i++) { // Comienza en 1 para omitir las claves
            Object[] row = rawData[i];
            if (row == null || row.length != keys.length) {
                System.out.println("Fila inválida en el índice " + i + ": " + java.util.Arrays.toString(row));
                continue; // Saltar filas inválidas
            }

            Object[] keyValuePairs = new Object[keys.length * 2];
            for (int j = 0; j < keys.length; j++) {
                keyValuePairs[j * 2] = keys[j];       // Clave
                keyValuePairs[j * 2 + 1] = row[j];   // Valor
            }
            testData[i - 1][0] = keyValuePairs; // Ajustar índice para testData
            System.out.println("Pares clave-valor procesados: " + java.util.Arrays.toString(keyValuePairs));
        }

        return testData;
    }

    /**
     * Método genérico para leer cualquier archivo CSV.
     * @param filePath Ruta completa del archivo CSV.
     * @return Un Object[][] con los datos del archivo CSV.
     */
    private static Object[][] readCSV(String filePath) {
        try (CSVReader reader = new CSVReader(new FileReader(filePath))) {
            List<String[]> data = reader.readAll();

            if (data.isEmpty()) {
                throw new RuntimeException("El archivo CSV está vacío: " + filePath);
            }

            // Crear el array para almacenar los datos
            Object[][] testData = new Object[data.size()][];

            // Leer todas las filas del archivo CSV
            for (int i = 0; i < data.size(); i++) {
                testData[i] = data.get(i);
                System.out.println("Fila leída: " + java.util.Arrays.toString(data.get(i)));
            }

            return testData;
        } catch (IOException | CsvException e) {
            throw new RuntimeException("Error al leer el archivo CSV: " + filePath, e);
        }
    }
}