package com.sjsu.masterproject.client;

import org.apache.commons.lang3.StringUtils;
import org.springframework.core.env.Environment;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

public class OnlineStoreServiceClient {

	private static final String BASE_URL = "http://localhost:8081/rest/";

	protected static String getBaseServiceUrl(Environment env) {
		String baseUrl = env.getProperty("app.onlineStoreService.baseUrl");
		System.out.println("baseUrl: " + baseUrl);

		return StringUtils.isNotBlank(baseUrl) ? baseUrl : BASE_URL;
	}

	public static RestTemplate getRestTemplate() {
		RestTemplate restTemplate = new RestTemplate();

		restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
		restTemplate.getMessageConverters().add(new StringHttpMessageConverter());

		return restTemplate;
	}
}
