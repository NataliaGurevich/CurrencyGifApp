package test.alfa.currencyGifApp.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import test.alfa.currencyGifApp.services.RateService;

@Controller
public class CurrencyController {

    private RateService rateService;

    private static final Logger LOGGER = LoggerFactory.getLogger(CurrencyController.class);

    @Autowired
    public CurrencyController(RateService rateService) {
        this.rateService = rateService;
    }

    @GetMapping("/{currency}")
    public String currency(@PathVariable String currency, Model model) {

        LOGGER.info("ENDPOINT /{}", currency);

        String UrlGif = rateService.isCurrencyHigher(currency);
        model.addAttribute("url_gif", UrlGif);

        return "currency";
    }
}
