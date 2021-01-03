package test.alfa.currencyGifApp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableFeignClients
@ComponentScan(basePackages = {"test.alfa.currencyGifApp"})
public class CurrencyGifAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(CurrencyGifAppApplication.class, args);
	}

}
