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
    void tooManySimCards() {
        String output = application.process(new String[]{
                "SIM_CARD", "SIM_CARD", "SIM_CARD", "SIM_CARD", "SIM_CARD",
                "SIM_CARD", "SIM_CARD", "SIM_CARD", "SIM_CARD", "SIM_CARD",
                "SIM_CARD",
        });
        assertEquals("Unable to checkout because: SIM_CARD purchase limit is 10 Please adjust your basket.", output);
    }

    @Test
    void buy10OfEverything() {
        String output = application.process(new String[]{
                "SIM_CARD", "SIM_CARD", "SIM_CARD", "SIM_CARD", "SIM_CARD", "SIM_CARD", "SIM_CARD", "SIM_CARD", "SIM_CARD", "SIM_CARD",
                "PHONE_CASE", "PHONE_CASE", "PHONE_CASE", "PHONE_CASE", "PHONE_CASE", "PHONE_CASE", "PHONE_CASE", "PHONE_CASE", "PHONE_CASE", "PHONE_CASE",
                "PHONE_INSURANCE", "PHONE_INSURANCE", "PHONE_INSURANCE", "PHONE_INSURANCE", "PHONE_INSURANCE", "PHONE_INSURANCE", "PHONE_INSURANCE", "PHONE_INSURANCE", "PHONE_INSURANCE", "PHONE_INSURANCE",
                "WIRED_EARPHONES", "WIRED_EARPHONES", "WIRED_EARPHONES", "WIRED_EARPHONES", "WIRED_EARPHONES", "WIRED_EARPHONES", "WIRED_EARPHONES", "WIRED_EARPHONES", "WIRED_EARPHONES", "WIRED_EARPHONES",
                "WIRELESS_EARPHONES", "WIRELESS_EARPHONES", "WIRELESS_EARPHONES", "WIRELESS_EARPHONES", "WIRELESS_EARPHONES", "WIRELESS_EARPHONES", "WIRELESS_EARPHONES", "WIRELESS_EARPHONES", "WIRELESS_EARPHONES", "WIRELESS_EARPHONES",
        });
        assertEquals("========================================\n" +
                "Sim Card * 10                 200.00 CHF\n" +
                "BOGOF                        -100.00 CHF\n" +
                "Wired Headphones * 10         300.00 CHF\n" +
                "Wireless Headphones * 10      500.00 CHF\n" +
                "Phone Insurance * 10        1,200.00 CHF\n" +
                "SPECIAL                      -240.00 CHF\n" +
                "Phone Case * 10               100.00 CHF\n" +
                "========================================\n" +
                "Sales Tax                     120.00 CHF\n" +
                "Total                       2,080.00 CHF\n", output);
    }
}
