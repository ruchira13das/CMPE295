package com.sjsu.masterproject.util;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.ui.Model;

import com.sjsu.masterproject.form.SignInForm;
import com.sjsu.masterproject.form.SignUpForm;
import com.sjsu.masterproject.model.Product;

public class Util {

	private static final Logger log = LoggerFactory.getLogger(Util.class);
	public static final String CUSTOMER_PREFERENCES_CONFIG = "app.config.customer.preferences";

	public static String getCustomerFromRequest(HttpServletRequest request) throws Exception {
		String customerId = "";
		if (Util.isCustomerLoggedIn(request)) {
			customerId = getCustomerIdFromRequest(request);
		} else {
			// Use the customer IP as the identifier
			customerId = getCustomerIp(request);
		}

		return customerId;
	}

	private static String getCustomerIdFromRequest(HttpServletRequest request) throws Exception {
		String customerId = null;

		if (request != null) {
			try {
				Cookie[] cookies = request.getCookies();
				for (Cookie cookie : cookies) {
					if ("eshopper_customer".equals(cookie.getName())) {
						customerId = cookie.getValue();
						break;
					}
				}
			} catch (Exception e) {
				log.error("Error while reading the user-id from cookie!", e);
			}
		}

		return customerId;
	}

	public static boolean isCustomerLoggedIn(HttpServletRequest request) throws Exception {
		boolean isLoggedIn = false;

		if (request != null) {
			try {
				Cookie[] cookies = request.getCookies();
				if (cookies != null) {
					for (Cookie cookie : cookies) {
						if ("eshopper_loggedin".equals(cookie.getName())) {
							isLoggedIn = true;
							break;
						}
					}
				}
			} catch (Exception e) {
				log.error("Error while reading the user-id from cookie!", e);
			}
		}

		return isLoggedIn;
	}

	public static String getCustomerIp(HttpServletRequest request) throws Exception {
		if (request != null) {
			String ip = request.getHeader("X-Forwarded-For");
			if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
				ip = request.getHeader("Proxy-Client-IP");
			}
			if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
				ip = request.getHeader("WL-Proxy-Client-IP");
			}
			if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
				ip = request.getHeader("HTTP_CLIENT_IP");
			}
			if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
				ip = request.getHeader("HTTP_X_FORWARDED_FOR");
			}
			if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
				ip = request.getRemoteAddr();
			}

			// localhost would resolve in a IPv6 version of IP.
			if ("0:0:0:0:0:0:0:1".equals(ip)) {
				ip = "127.0.0.1";
			}

			return ip;
		}

		return null;
	}

	// Recommendation frame is a scrollable unit with 3 products in active
	// display and each scrollable unit can hold upto 3 more products
	public static Model addRecommendationsToResponse(Model model, List<Product> recommendations) throws Exception {

		if (recommendations == null || recommendations.size() == 0) {
			log.info("addRecommendationsToResponse - No recommendations!");
			return model;
		}

		if (recommendations.size() <= 3) {
			model.addAttribute("activeRecommendations", recommendations);
			return model;
		}

		// Build data for the active frame
		List<Product> activeRecommendations = recommendations.subList(0, 3);
		log.info("activeRecommendations: {}", activeRecommendations.size());

		// Build data for the scrollable frames - list of frames with each frame
		// containing upto 3 products
		List<List<Product>> scrollableRecommendations = new ArrayList<>();

		int i = 3;
		int size = recommendations.size();
		while (i <= size - 1) {
			// log.info("i :: {}, Size:: {}", i, size);
			int startIndex = i;
			i = startIndex + 3;

			if (i >= size) {
				i = size;
			}
			scrollableRecommendations.add(recommendations.subList(startIndex, i));
		}
		log.info("scrollableRecommendations size: {}", scrollableRecommendations.size());

		// Add them to the model
		model.addAttribute("activeRecommendations", activeRecommendations);
		model.addAttribute("scrollableRecommendations", scrollableRecommendations);

		return model;
	}

	public static HttpServletResponse addCustomerIdCookieToResponse(HttpServletResponse response,
			HttpServletRequest request) {

		// Add eshopper_customer cookie if it is not already present or has
		// expired
		boolean isCookiePresent = false;
		if (request != null) {
			try {
				Cookie[] cookies = request.getCookies();
				if (cookies != null) {
					for (Cookie cookie : cookies) {
						if ("eshopper_customer".equals(cookie.getName())) {
							isCookiePresent = true;
							break;
						}
					}
				}

				if (!isCookiePresent) {
					// Cookie not present. Create a new one.
					// max-age: 30 days
					CookieUtil.create(response, "eshopper_customer", Util.getCustomerFromRequest(request),
							30 * 24 * 3600);
				}

			} catch (Exception e) {
				log.error("Error while reading the user-id from cookie!", e);
			}
		}

		return response;
	}

	public static String getForwardPage(String forwardAction) {
		// Default
		String forwardPage = "home";
		if (StringUtils.isNotBlank(forwardAction)) {
			switch (forwardAction) {
			case "checkout":
				forwardPage = "checkout";
				break;
			default:
				forwardPage = "home";
				break;
			}
		}

		return forwardPage;
	}

	public static Model setModelForSignInAction(Model model, String forwardAction, SignInForm signInForm,
			String message, Environment env) throws Exception {

		signInForm.setMessage(message);

		SignUpForm signUpForm = new SignUpForm();
		signUpForm.setForwardAction(forwardAction);

		model.addAttribute("signInForm", signInForm);
		model.addAttribute("signUpForm", signUpForm);
		model.addAttribute("forward", forwardAction);
		model.addAttribute("preferences", env.getProperty(CUSTOMER_PREFERENCES_CONFIG, List.class));

		return model;
	}

	public static Model setSignInStatus(Model model, HttpServletRequest request) throws Exception {
		if (isCustomerLoggedIn(request)) {
			log.info("setSignInStatus: {}", getCustomerIdFromRequest(request));
			model.addAttribute("signedIn", true);
			model.addAttribute("signedInUser", getCustomerIdFromRequest(request));
		}
		return model;
	}
}
