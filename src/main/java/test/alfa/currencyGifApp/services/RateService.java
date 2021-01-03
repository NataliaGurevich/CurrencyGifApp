package test.alfa.currencyGifApp.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.stereotype.Service;
import test.alfa.currencyGifApp.client.ExchangeClient;
import test.alfa.currencyGifApp.client.GiphyClient;
import test.alfa.currencyGifApp.dto.giphy.Datum;
import test.alfa.currencyGifApp.dto.giphy.GiphyResponse;
import test.alfa.currencyGifApp.dto.rates.Rates;
import test.alfa.currencyGifApp.dto.rates.RatesResponse;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
@EnableFeignClients
public class RateService {

    private static final Logger LOGGER = LoggerFactory.getLogger(RateService.class);

    @Value("${org.openexchangerates.app_id}")
    private String EXCHANGE_APP_ID;

    @Value("${org.openexchengerates.url}")
    private String EXCHANGE_URL;

    @Value("${app.param.url.error}")
    private String ERROR_URL;

    @Value("${com.giphy.developers.app_key}")
    private String GIPHY_API_ID;

    @Value("${app.param.base.currency}")
    private String BASE;

    @Value("${app.param.local.currency}")
    private String RUB;

    @Value("${app.param.search.rich}")
    private String RICH;

    @Value("${app.param.search.broke}")
    private String BROKE;

    @Value("${com.giphy.developers.url}")
    private String GIPHY_URL;

    private DateService dateService;
    private ExchangeClient exchangeClient;
    private GiphyClient giphyClient;

    @Autowired
    public RateService(DateService dateService, ExchangeClient exchangeClient, GiphyClient giphyClient) {
        this.dateService = dateService;
        this.exchangeClient = exchangeClient;
        this.giphyClient = giphyClient;
    }

    public String isCurrencyHigher(String currency) {

        try {
            String gifSearch = isHigher(currency) ? RICH : BROKE;
            return getGifUrl(gifSearch);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return ERROR_URL;
        }
    }

    private boolean isHigher(String currency) throws Exception {

        double rateRubToday = getRatePerDate(RUB, dateService.today());
        double rateRubYesterday = getRatePerDate(RUB, dateService.yesterday());

        double rateCurrencyToday = getRatePerDate(currency, dateService.today());
        double rateCurrencyYesterday = getRatePerDate(currency, dateService.yesterday());

        if (rateRubToday == 0 || rateRubYesterday == 0) {
            throw new Exception("ERROR BY RATE");
        }

        BigDecimal currencyToRubToday = new BigDecimal(rateCurrencyToday / rateRubToday);
        BigDecimal currencyToRubYesterday = new BigDecimal(rateCurrencyYesterday / rateRubYesterday);

        boolean isHigher = currencyToRubToday.compareTo(currencyToRubYesterday) > 0;

        LOGGER.info("HIGHER => {}", isHigher);

        return isHigher;
    }

    private double getRatePerDate(String currency, String date) throws Exception {

        RatesResponse ratesResponse = exchangeClient.getRatesPerDate(date,
                EXCHANGE_APP_ID, BASE);

        String methodName = "get" + currency.toLowerCase();

        Rates rates = ratesResponse.getRates();
        double rateValue = getValue(rates, methodName);

        LOGGER.info("RATE {} TO BASE {} -> {}", currency.toUpperCase(), BASE, rateValue);

        return rateValue;
    }

    private double getValue(Rates rates, String methodName) throws Exception {

        double rateValue = Double.MIN_VALUE;

        Class classRates = rates.getClass();
        for (Method method : classRates.getMethods()) {

            if (method.getName().toLowerCase().equals(methodName)) {
                rateValue = (Double) method.invoke(rates);
            }
        }
        LOGGER.info("VALUE FROM RATES {} => {}", methodName, rateValue);

        if (rateValue == Double.MIN_VALUE) throw new Exception("NO SUCH CURRENCY");

        return rateValue;
    }

    private String getGifUrl(String searchKey) throws Exception {

        GiphyResponse giphyResponse = giphyClient.getGiphys(GIPHY_API_ID, searchKey);

        List<String> gifUrl = new ArrayList<>();

        List<Datum> datums = giphyResponse.getData();

        for (Datum data : datums) {
            gifUrl.add(data.getImages().getOriginal().getUrl());
        }

        Random random = new Random();
        int i = random.nextInt(datums.size());

        LOGGER.info("GIF_URL {}", gifUrl.get(i));

        return gifUrl.get(i);
    }
}
