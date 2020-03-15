package duncan.atkinson.dataobjects;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class CHFTest {

    @Test
    void should_equal_anotherIdenticalInstance() {
        CHF amount1 = new CHF(100);
        CHF amount2 = new CHF(100);
        assertEquals(amount1, amount2);
    }

    @Test
    void should_equal_anotherInstanceConstructedWithTwoDifferentBigDecimals() {
        CHF amount1 = new CHF(100);
        CHF amount2 = new CHF(new BigDecimal("100.00"));
        assertEquals(amount1, amount2);
    }

    @Test
    void should_maintain_sub_cent_values_when_adding() {
        CHF amount1 = new CHF(new BigDecimal("100.005"));
        CHF amount2 = new CHF(new BigDecimal("100.005"));

        CHF sum = CHF.add(amount1, amount2);
        CHF expectedSum = new CHF("200.01");
        assertEquals(expectedSum, sum);
    }

    @Test
    void should_maintain_sub_cent_values_when_dividing() {
        CHF amount = new CHF(new BigDecimal("0.01"));

        CHF onePercent = amount.divide(1000);
        CHF sumOfOnePercents = new CHF(0);
        for (int i = 0; i < 1000; i++) {
            sumOfOnePercents = CHF.add(sumOfOnePercents, onePercent);
        }

        CHF expectedSum = new CHF("0.01");
        assertEquals(expectedSum, sumOfOnePercents);
    }

    @Test
    void toString_should_outputEqualValuesRoundingUp() {
        CHF amount1 = new CHF("0.01");
        CHF amount2 = new CHF("0.005");
        assertEquals(amount1.toString(), amount2.toString());
    }

    @Test
    void shouldNot_equal_despite_lookingEqual() {
        CHF amount1 = new CHF("0.01");
        CHF amount2 = new CHF("0.009");
        assertNotEquals(amount1, amount2);
    }

    @Test
    void should_toString() {
        CHF amount = new CHF(100);
        assertEquals("CHF{100.00}", amount.toString());
    }

    @Test
    void shouldOutput_formatted_givenZero() {
        CHF amount = new CHF(0);
        assertEquals("0.00", amount.formatted());
    }

    @Test
    void shouldOutput_formatted_givenThousand() {
        CHF amount = new CHF(1_000);
        assertEquals("1,000.00", amount.formatted());
    }

    @Test
    void shouldOutput_formatted_givenMillion() {
        CHF amount = new CHF(1_000_000);
        assertEquals("1,000,000.00", amount.formatted());
    }
}