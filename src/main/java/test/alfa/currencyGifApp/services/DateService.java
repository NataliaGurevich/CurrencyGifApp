package test.alfa.currencyGifApp.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

@Service
public class DateService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DateService.class);

    public String today() {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        Calendar gc = new GregorianCalendar();
        Date today = gc.getTime();

        LOGGER.info("TODAY: {}", sdf.format(today));

        return sdf.format(today);
    }

    public String yesterday() {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        Calendar gc = new GregorianCalendar();
        gc.add(Calendar.DATE, -1);

        Date yesterday = gc.getTime();

        LOGGER.info("TOMORROW: {}", sdf.format(yesterday));

        return sdf.format(yesterday);
    }
}
