package duncan.atkinson;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ApplicationIntegrationTest {

    private static Application application;

    @BeforeAll
    static void beforeAll() {
        application = new Application();
    }

    @Test
    void zeroItems() {
        String output = application.process(new String[0]);
        assertEquals("========================================\n" +
                "========================================\n" +
                "Sales Tax                       0.00 CHF\n" +
                "Total                           0.00 CHF\n", output);
    }

    @Test
    void SimCardsDiscount() {
        String output = application.process(new String[]{
                "SIM_CARD", "SIM_CARD", "SIM_CARD", "SIM_CARD", "SIM_CARD"});
        assertEquals("========================================\n" +
                "Sim Card * 5                  100.00 CHF\n" +
                "BOGOF                         -40.00 CHF\n" +
                "========================================\n" +
                "Sales Tax                       7.20 CHF\n" +
                "Total                          67.20 CHF\n", output);
    }

    @Test
    void tooManySimCards() {
        String output = application.process(new String[]{
                "SIM_CARD", "SIM_CARD", "SIM_CARD", "SIM_CARD", "SIM_CARD",
                "SIM_CARD", "SIM_CARD", "SIM_CARD", "SIM_CARD", "SIM_CARD",
                "SIM_CARD",
        });
        assertEquals("Unable to checkout because: SIM_CARD purchase limit is 10 Please adjust your basket.", output);
    }

}
