package com.horasan.customer.handler;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.google.gson.Gson;
import com.horasan.customer.entity.Customer;

/**
 * Handler for requests to Lambda function.
 */
public class CreateCustomerHandler
		implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

	public APIGatewayProxyResponseEvent handleRequest(final APIGatewayProxyRequestEvent input, final Context context) {
		
		Gson gson = new Gson();
		
		Customer newCustomer = gson.fromJson(input.getBody(), Customer.class);
		newCustomer.setCustomerId(UUID.randomUUID().toString());
		
		newCustomer.setCountryCode(System.getenv("DEFAULT_COUNTRY"));
		newCustomer.setCurrencyCode(System.getenv("DEFAULT_CURRENCY"));
		
		Map<String, String> returnBody = getCustomerInfoAsMap(newCustomer);
		
		APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent();
		
		response.withHeaders(getDefaultHeader());
		response.withStatusCode(200);
		//response.withBody(gson.toJson(returnBody, Map.class));
		response.withBody(gson.toJson(newCustomer, Customer.class));
		
		return response;
	}

	private Map<String, String> getDefaultHeader() {
		Map<String, String> headerInfo = new HashMap<>();
		headerInfo.put("Content-Type", "application/json");
		return headerInfo;
	}
	
	private Map<String, String> getCustomerInfoAsMap(Customer newCustomer) {
		Map<String, String> customerInfo = new HashMap<>();
		
		customerInfo.put("customerId", newCustomer.getCustomerId());
		customerInfo.put("firstName", newCustomer.getFirstName());
		customerInfo.put("lastName", newCustomer.getLastName());
		customerInfo.put("email", newCustomer.getEmail());
		customerInfo.put("countryCode", newCustomer.getCountryCode());
		customerInfo.put("currencyCode", newCustomer.getCurrencyCode());
		
		return customerInfo;
	}
}
