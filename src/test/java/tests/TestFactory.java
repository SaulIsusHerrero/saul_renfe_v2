package tests;

import org.testng.annotations.Factory;
import java.util.ArrayList;
import java.util.List;

public class TestFactory {

    @Factory
    public Object[] createTests() {
        String[] browsers = {"chrome", "firefox", "edge"};
        List<Object> tests = new ArrayList<>();

        for (String browser : browsers) {
            tests.add(new EmptyBuyerDataTest5days(browser));
            tests.add(new InvalidCardPaymentTest(browser));
            tests.add(new InvalidDataTraveler15days(browser));
            tests.add(new SelectTrainMinor80EurosEvenings(browser)); // Renamed to avoid encoding issues
        }

        return tests.toArray();
    }
}