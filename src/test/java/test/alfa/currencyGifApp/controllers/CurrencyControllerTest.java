package test.alfa.currencyGifApp.controllers;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import test.alfa.currencyGifApp.services.RateService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(CurrencyController.class)
public class CurrencyControllerTest {

    @MockBean
    private RateService rateService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void currencyOkTest() throws Exception {
        given(rateService.isCurrencyHigher(any())).willReturn("${app.param.url.error}");
        mockMvc.perform(get("/usd"))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.TEXT_HTML));
    }

    @Test
    public void currencyBadTest() throws Exception {
        given(rateService.isCurrencyHigher(any())).willReturn("${app.param.url.error}");
        mockMvc.perform(get("/abc"))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.TEXT_HTML));
    }
}
