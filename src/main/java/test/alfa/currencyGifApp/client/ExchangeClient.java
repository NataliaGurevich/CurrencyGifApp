package test.alfa.currencyGifApp.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import test.alfa.currencyGifApp.dto.rates.RatesResponse;

@FeignClient(url = "${org.openexchengerates.url}", name = "EXCHANGE-CLIENT")
public interface ExchangeClient {

    @GetMapping("/historical/{date}.json?app_id={appId}&base={base}")
    RatesResponse getRatesPerDate(@RequestParam("date") String date,
                                  @RequestParam("app_id") String app_id,
                                  @RequestParam("base") String base) throws Exception;

}
