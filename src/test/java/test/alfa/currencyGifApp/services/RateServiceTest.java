package test.alfa.currencyGifApp.services;

import org.junit.Assert;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import test.alfa.currencyGifApp.client.ExchangeClient;
import test.alfa.currencyGifApp.client.GiphyClient;
import test.alfa.currencyGifApp.dto.giphy.Datum;
import test.alfa.currencyGifApp.dto.giphy.GiphyResponse;
import test.alfa.currencyGifApp.dto.giphy.Images;
import test.alfa.currencyGifApp.dto.giphy.Original;
import test.alfa.currencyGifApp.dto.rates.Rates;
import test.alfa.currencyGifApp.dto.rates.RatesResponse;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(RateServiceTest.class)
public class RateServiceTest {

    private static String currency;
    private static String currencyError;
    private static String dateToday;
    private static String dateYesterday;
    private static GiphyResponse giphyResponse;
    private static GiphyResponse giphyResponseError;
    private static RatesResponse ratesResponseToday;
    private static RatesResponse ratesResponseYesterday;
    private static RatesResponse ratesResponseTodayError;
    private static RatesResponse ratesResponseYesterdayError;

    private static String ERROR_URL;

    private static String BASE;

    private static String RUB;

    private static String RICH;

    private static String BROKE;

    @Autowired
    private RateService rateService;

    @MockBean
    private GiphyClient giphyClient;

    @MockBean
    private ExchangeClient exchangeClient;

    @MockBean
    private DateService dateService;

    @BeforeAll
    public static void init() throws Exception {

        currency = "eur";
        currencyError = "rubl";

        dateToday = "2021-01-03";
        dateYesterday = "2021-01-02";

        BASE = "USD";
        RUB = "RUB";
        RICH = "rich";
        BROKE = "broke";
        ERROR_URL = "img/giphy.webp";

        Original original1 = new Original();
        original1.setUrl("gif Url1");

        Original original2 = new Original();
        original2.setUrl("gif Url2");

        Images images1 = new Images();
        images1.setOriginal(original1);

        Images images2 = new Images();
        images2.setOriginal(original2);

        Datum datum1 = new Datum();
        datum1.setImages(images1);

        Datum datum2 = new Datum();
        datum2.setImages(images2);

        List<Datum> datums = new ArrayList<>();
//        datums.add(datum1);
        datums.add(datum2);

        giphyResponse = new GiphyResponse();
        giphyResponse.setData(datums);

        //giphyResponseError

        Original originalError = new Original();
        original1.setUrl("gif Url error");

        Images imagesError = new Images();
        imagesError.setOriginal(originalError);

        Datum datumError = new Datum();
        datumError.setImages(imagesError);

        List<Datum> datumsError = new ArrayList<>();
        datumsError.add(datumError);

        giphyResponseError = new GiphyResponse();
        giphyResponseError.setData(datumsError);

        Rates ratesToday = new Rates();
        ratesToday.setRUB(75.0);
        ratesToday.setEUR(0.8);
        ratesToday.setUSD(1.0);

        Rates ratesYesterday = new Rates();
        ratesYesterday.setRUB(74.0);
        ratesYesterday.setEUR(0.76);
        ratesYesterday.setUSD(1.0);

        ratesResponseToday = new RatesResponse();
        ratesResponseToday.setRates(ratesToday);

        ratesResponseYesterday = new RatesResponse();
        ratesResponseYesterday.setRates(ratesYesterday);

        //ratesResponseError

        Rates ratesTodayError = new Rates();
        ratesTodayError.setRUB(0.0);
        ratesTodayError.setEUR(0.0);
        ratesTodayError.setUSD(1.0);

        Rates ratesYesterdayError = new Rates();
        ratesYesterdayError.setRUB(0.0);
        ratesYesterdayError.setEUR(0.0);
        ratesYesterdayError.setUSD(1.0);

        ratesResponseTodayError = new RatesResponse();
        ratesResponseTodayError.setRates(ratesTodayError);

        ratesResponseYesterdayError = new RatesResponse();
        ratesResponseYesterdayError.setRates(ratesYesterdayError);
    }

    @Test
    public void isCurrencyHigherTest() throws Exception {

        Mockito.when(dateService.today()).thenReturn(dateToday);
        Mockito.when(dateService.yesterday()).thenReturn(dateYesterday);

        Mockito.when(giphyClient.getGiphys(any(), eq(RICH)))
                .thenReturn(giphyResponse);

        Mockito.when(giphyClient.getGiphys(any(), eq(BROKE)))
                .thenReturn(giphyResponse);

        Mockito.when(exchangeClient.getRatesPerDate(eq(dateToday), anyString(), anyString()))
                .thenReturn(ratesResponseToday);

        Mockito.when(exchangeClient.getRatesPerDate(eq(dateYesterday), anyString(), anyString()))
                .thenReturn(ratesResponseYesterday);

        String url = rateService.isCurrencyHigher(currency);

        Assert.assertEquals(url, "gif Url2");
    }

    @Test
    public void isCurrencyHigherErrorTest() {

        Mockito.when(dateService.today()).thenReturn(dateToday);
        Mockito.when(dateService.yesterday()).thenReturn(dateYesterday);

        try {
            Mockito.when(giphyClient.getGiphys(any(), eq(RICH)))
                    .thenReturn(giphyResponseError);
        } catch (Exception e) {
            Assert.assertEquals("NO SUCH CURRENCY", e.getMessage());
        }

        try {
            Mockito.when(giphyClient.getGiphys(any(), eq(BROKE)))
                    .thenReturn(giphyResponseError);
        } catch (Exception e) {
            Assert.assertEquals("NO SUCH CURRENCY", e.getMessage());
        }

        try {
            Mockito.when(exchangeClient.getRatesPerDate(eq(dateToday), any(), anyString()))
                    .thenReturn(ratesResponseTodayError);
        } catch (Exception e) {
            Assert.assertEquals("ERROR BY RATE", e.getMessage());
        }

        try {
            Mockito.when(exchangeClient.getRatesPerDate(eq(dateYesterday), any(), anyString()))
                    .thenReturn(ratesResponseYesterdayError);
        } catch (Exception e) {
            Assert.assertEquals("ERROR BY RATE", e.getMessage());
        }

        String url = rateService.isCurrencyHigher(currencyError);

        Assert.assertEquals(url, ERROR_URL);
    }
}
