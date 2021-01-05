package test.alfa.currencyGifApp.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
public class DateService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DateService.class);

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public String today() {

        LocalDate date = LocalDate.now();

        LOGGER.info("TODAY: {}", date.format(formatter));

        return date.format(formatter);
    }

    public String yesterday() {

        LocalDate date = LocalDate.now();
        date = date.minusDays(1);

        LOGGER.info("YESTERDAY: {}", date.format(formatter));

        return date.format(formatter);
    }
}
