package io.spring.guides.consuming_web_service;

import io.spring.guides.consuming_rest.wsdl.Forecast;
import io.spring.guides.consuming_rest.wsdl.ForecastReturn;
import io.spring.guides.consuming_rest.wsdl.GetCityForecastByZIPResponse;
import io.spring.guides.consuming_rest.wsdl.Temp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

import java.text.SimpleDateFormat;

@SpringBootApplication
public class Application implements CommandLineRunner {
    private static final Logger LOGGER = LoggerFactory.getLogger(Application.class);
    @Autowired
    WeatherClient weatherClient;

    @Bean
    public Jaxb2Marshaller marshaller() {
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        marshaller.setContextPath("io.spring.guides.consuming_rest.wsdl");
        return marshaller;
    }

    @Bean
    public WeatherClient weatherClient(Jaxb2Marshaller marshaller) {
        WeatherClient client = new WeatherClient();
        client.setDefaultUri("http://wsf.cdyne.com/WeatherWS/Weather.asmx");
        client.setMarshaller(marshaller);
        client.setUnmarshaller(marshaller);
        return client;
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        GetCityForecastByZIPResponse cityForecastByZip = weatherClient.getCityForecastByZip("94304");
        ForecastReturn forecastReturn = cityForecastByZip.getGetCityForecastByZIPResult();
        if (forecastReturn.isSuccess()) {
            LOGGER.info("Forecast for {}, {}", forecastReturn.getCity(), forecastReturn.getState());
        }
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        for (Forecast forecast : forecastReturn.getForecastResult().getForecast()) {
            Temp temperatures = forecast.getTemperatures();
            LOGGER.info("{} {} {}°-{}°",
                    format.format(forecast.getDate().toGregorianCalendar().getTime()),
                    forecast.getDesciption(),
                    temperatures.getMorningLow(),
                    temperatures.getDaytimeHigh()
            );
        }
    }
}
