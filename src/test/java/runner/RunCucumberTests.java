package runner;

import org.junit.runner.RunWith;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;

@RunWith(Cucumber.class)
@CucumberOptions(
    features = "src/test/resources/features", // Ruta a los archivos .feature
    glue = "steps", // Paquete donde están los Step Definitions
    plugin = {"pretty", "html:target/cucumber-reports.html", "json:target/cucumber.json"},
    monochrome = true
)
public class RunCucumberTests {
    // Clase vacía, solo sirve como punto de entrada para Cucumber
}
