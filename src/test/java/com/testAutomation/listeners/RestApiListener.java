package com.testAutomation.listeners;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import io.restassured.filter.Filter;
import io.restassured.filter.FilterContext;
import io.restassured.response.Response;
import io.restassured.specification.FilterableRequestSpecification;
import io.restassured.specification.FilterableResponseSpecification;

public class RestApiListener implements Filter{

	private static final Logger logger = LogManager.getLogger(RestApiListener.class);
	@Override
	public Response filter(FilterableRequestSpecification requestSpec, FilterableResponseSpecification responseSpec,
			FilterContext ctx) {
		
		Response response = ctx.next(requestSpec, responseSpec);
//		if(response.getStatusCode() >= 200  && response.getStatusCode() <300) {
//			
//		}else {
			logger.info("\n Method Name " + requestSpec.getMethod() +
					"\n URI => " + requestSpec.getURI() + 
					"\n Request Body " + requestSpec.getBody() + 
					"\n Response Body " + response.getBody().prettyPrint());
		//}
		return response;
	}

}
