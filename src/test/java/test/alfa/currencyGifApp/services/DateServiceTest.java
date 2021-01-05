package test.alfa.currencyGifApp.services;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DateServiceTest {

    public static DateService dateService;

    @BeforeAll
    public static void init() {
        dateService = new DateService();
    }

    @Test
    public void testToToday() {
        //expected date today in format yyyy-MM-dd
        assertEquals("2021-01-05", dateService.today());
    }

    @Test
    public void testToYesterday() {
        //expected date yesterday in format yyyy-MM-dd
        assertEquals("2021-01-04", dateService.yesterday());
    }
}
