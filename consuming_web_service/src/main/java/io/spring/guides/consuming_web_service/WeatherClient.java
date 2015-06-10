package io.spring.guides.consuming_web_service;

import io.spring.guides.consuming_rest.wsdl.GetCityForecastByZIP;
import io.spring.guides.consuming_rest.wsdl.GetCityForecastByZIPResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;
import org.springframework.ws.soap.client.core.SoapActionCallback;

public class WeatherClient extends WebServiceGatewaySupport {
    private static final Logger LOGGER = LoggerFactory.getLogger(WeatherClient.class);

    public GetCityForecastByZIPResponse getCityForecastByZip(String zipCode) {
        GetCityForecastByZIP request = new GetCityForecastByZIP();
        request.setZIP(zipCode);

        LOGGER.info("Requesting forecast for {}", zipCode);

        return (GetCityForecastByZIPResponse) getWebServiceTemplate().marshalSendAndReceive(
                request, new SoapActionCallback("http://ws.cdyne.com/WeatherWS/GetCityForecastByZIP"));
    }
}
