package test.alfa.currencyGifApp.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import test.alfa.currencyGifApp.dto.giphy.GiphyResponse;

@FeignClient(url = "${com.giphy.developers.url}", name = "GIPHY-CLIENT")
public interface GiphyClient {

    @GetMapping("/search?api_key={appKey}&q={search}&limit=250&offset=0&rating=g&lang=en")
    GiphyResponse getGiphys(@RequestParam("api_key") String appKey,
                            @RequestParam("search") String search) throws Exception;
}
