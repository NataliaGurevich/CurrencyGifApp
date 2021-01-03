package test.alfa.currencyGifApp.services;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class CurrencyService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CurrencyService.class);

    private static String exchange_app_id = "59a2f61c51ae4c8b9ebd56cae9187df0";

    private static String rateUrl = "https://openexchangerates.org/api/";

    private static String errorUrl = "img/giphy.webp";

    private static String richUrl = "img/giphy_rich.gif";

    private static String brokeUrl = "img/giphy_broke.webp";

    private static String gif_app_id = "nDBKN22fHAZLxT9Tsrq1YHxYQJ7bu1H0";


    private String yesterday() {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        Calendar gc = new GregorianCalendar();
        gc.add(Calendar.DATE, -1);

        Date yesterday = gc.getTime();

        LOGGER.info("YESTERDAY: {}", sdf.format(yesterday));

        return sdf.format(yesterday) + ".json";
    }

    private double rateCurrency(String uri, String currency) throws JSONException, MalformedURLException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(uri)).build();

        String responseBody = client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .join();

        JSONObject ratesToday = new JSONObject(responseBody);
        double rateCurrency = ratesToday.getJSONObject("rates").getDouble(currency.toUpperCase());

        return rateCurrency;
    }

    public String isCurrencyHigher(String currency) {
        String base = "USD";

        //https://openexchangerates.org/api/latest.json?app_id=59a2f61c51ae4c8b9ebd56cae9187df0
        //https://openexchangerates.org/api/historical/2021-01-01.json?app_id=59a2f61c51ae4c8b9ebd56cae9187df0

        String uriTodayStr = rateUrl + "latest.json?app_id=" + exchange_app_id;
        String uriYesterdayStr = rateUrl + "historical/" + yesterday() + "?app_id=" + exchange_app_id;

        double rateRubToday;
        double rateRubYesterday;

        double rateCurrencyToday;
        double rateCurrencyYesterday;

        boolean isHigher = false;

        try {
            rateRubToday = rateCurrency(uriTodayStr, "RUB");
            rateRubYesterday = rateCurrency(uriYesterdayStr, "RUB");
            LOGGER.info("RUB TODAY: {} RUB YESTERDAY: {}", rateRubToday, rateRubYesterday);

            rateCurrencyToday = rateCurrency(uriTodayStr, currency);
            rateCurrencyYesterday = rateCurrency(uriYesterdayStr, currency);
            LOGGER.info("{} TODAY: {} {} YESTERDAY: {}", currency.toUpperCase(), rateCurrencyToday, currency.toUpperCase(), rateCurrencyYesterday);

            BigDecimal currencyToRubToday;
            BigDecimal currencyToRubYesterday;

            if (currency.toUpperCase().equals(base.toUpperCase())) {
                currencyToRubToday = new BigDecimal(1 / rateRubToday);
                currencyToRubYesterday = new BigDecimal(1 / rateRubYesterday);
            } else {
                currencyToRubToday = new BigDecimal(rateCurrencyToday / rateRubToday);
                currencyToRubYesterday = new BigDecimal(rateCurrencyYesterday / rateRubYesterday);
            }

            LOGGER.info("{} TO  RUB TODAY {}", currency.toUpperCase(), currencyToRubToday);
            LOGGER.info("{} TO  RUB YESTERDAY {}", currency.toUpperCase(), currencyToRubYesterday);

            if (currencyToRubToday.compareTo(currencyToRubYesterday) > 0) {
                isHigher = true;
            }
        } catch (JSONException | MalformedURLException e) {
            LOGGER.error("{}", e.getMessage());
            return errorUrl;
        }

        LOGGER.info("Higher => {}", isHigher);

        String gifSearch = isHigher ? "rich" : "broke";
        String uri = "https://api.giphy.com/v1/gifs/search?api_key=" + gif_app_id + "&q=" + gifSearch + "&limit=250&offset=0&rating=g&lang=en";

        String gifUrl;
        try {
            gifUrl = getGif(uri);
        } catch (JSONException | MalformedURLException e) {
            LOGGER.error("{}", e.getMessage());

            if (isHigher) {
                return richUrl;
            } else {
                return brokeUrl;
            }
        }

        return gifUrl;
    }

    private String getGif(String uri) throws JSONException, MalformedURLException {

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(uri)).build();

        String responseBody = client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .join();

        JSONObject data = new JSONObject(responseBody);
        JSONArray dataImages = data.getJSONArray("data");

        List<String> gifUrls = new ArrayList<>();

        for (int i = 0; i < dataImages.length(); i++) {

            gifUrls.add(dataImages.getJSONObject(i).getJSONObject("images").getJSONObject("original").getString("url"));
        }

        Random random = new Random();
        int i = random.nextInt(gifUrls.size());

        String uri1 = "https://api.giphy.com/v1/gifs/search?api_key=" + gif_app_id + "&q=" + "rich" + "&limit=1&offset=0&rating=g&lang=en";
        HttpRequest request1 = HttpRequest.newBuilder().uri(URI.create(uri1)).build();
        String responseBody1 = client.sendAsync(request1, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .join();
        System.out.println(responseBody1);


        return gifUrls.get(i);
    }
}
