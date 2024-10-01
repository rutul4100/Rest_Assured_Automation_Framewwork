package com.testAutomation.tests;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.jayway.jsonpath.JsonPath;
import com.testAutomation.constants.FileName;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import net.minidev.json.JSONArray;

public class CreateBookingUsingFileAsPayload {

	@Test
	public void postAPIRequest() throws IOException {
		String requestBody = FileUtils.readFileToString(new File(FileName.CREATE_BOOKING_REQUEST_BODY),"UTF-8");
		
		
	Response response=	RestAssured
			.given()
				.contentType(ContentType.JSON)
				.body(requestBody)
				.baseUri("https://restful-booker.herokuapp.com/booking")
				.log().all()
			.when()
				.post()
			.then()
				.assertThat().statusCode(200)
				.log().all()
				.extract().response();
				

	JSONArray arr= JsonPath.read(response.body().asString(), "$.booking..firstname");
	
	String firstName = (String) arr.get(0);
	System.out.println(firstName);
	Assert.assertEquals(firstName, "Api");
	
	int bookingId = JsonPath.read(response.body().asString(),"$.bookingid");
	System.out.print(bookingId);
	
	
	String jsonSchema=FileUtils.readFileToString(new File(FileName.GET_BOOKING_BYID_RESPONSE_SCHEMA),"UTF-8");
	//System.out.println(jsonSchema);
	RestAssured
	.given()
		.contentType(ContentType.JSON)
		.baseUri("https://restful-booker.herokuapp.com/booking")
		.log().all()	
	.when()
		.get("/{bk_id}",bookingId)
	.then()
		.log().all()
		.assertThat()
		.statusCode(200)
		.body(JsonSchemaValidator.matchesJsonSchema(jsonSchema));
				
	}
}
