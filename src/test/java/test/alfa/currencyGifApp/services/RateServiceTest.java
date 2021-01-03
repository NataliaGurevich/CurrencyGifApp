package test.alfa.currencyGifApp.services;

import org.junit.Assert;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
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

import static org.mockito.ArgumentMatchers.any;

@RunWith(SpringRunner.class)
@WebMvcTest(RateServiceTest.class)
public class RateServiceTest {

    private static List<Datum> datums;
    private static String currency;
    private static String dateToday;
    private static String dateYesterday;
    private static Rates ratesToday;
    private static Rates ratesYesterday;
    private static GiphyResponse giphyResponse;
    private static RatesResponse ratesResponseToday;
    private static RatesResponse ratesResponseYesterday;

    private static String searchKeyRich;
    private static String searchKeyBroke;

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

    @MockBean
    private GiphyClient giphyClient;

    @MockBean
    private ExchangeClient exchangeClient;

    @MockBean
    private DateService dateService;

    @Autowired
    private RateService rateService;

    @Before
    public static void init(){

        currency = "usd";
        searchKeyRich = "rich";
        searchKeyRich = "broke";

        dateToday = "2021-01-03";
        dateYesterday = "2021-01-02";

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

        datums = new ArrayList<>();
        datums.add(datum1);
        datums.add(datum2);

        giphyResponse.setData(datums);

        ratesToday = new Rates();
        ratesToday.setRUB(75.0);
        ratesToday.setEUR(0.8);
        ratesToday.setUSD(1.0);

        ratesYesterday = new Rates();
        ratesYesterday.setRUB(74.0);
        ratesYesterday.setEUR(0.76);
        ratesYesterday.setUSD(1.0);

        ratesResponseToday = new RatesResponse();
        ratesResponseToday.setRates(ratesToday);

        ratesResponseYesterday = new RatesResponse();
        ratesResponseYesterday.setRates(ratesYesterday);
    }

    @Test
    public void isCurrencyHigherTest() throws Exception {

        Mockito.when(dateService.today()).thenReturn(dateToday);
        Mockito.when(dateService.yesterday()).thenReturn(dateYesterday);

        Mockito.when(giphyClient.getGiphys(GIPHY_API_ID, RICH))
                .thenReturn(giphyResponse);

        Mockito.when(giphyClient.getGiphys(GIPHY_API_ID, BROKE))
                .thenReturn(giphyResponse);

        Mockito.when(exchangeClient.getRatesPerDate(dateToday, EXCHANGE_APP_ID, BASE))
                .thenReturn(ratesResponseToday);

        String url = rateService.isCurrencyHigher(currency);

        Assert.assertEquals(url, "gif Url1");
    }
}
