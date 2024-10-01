package com.testAutomation.tests;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.hamcrest.Matchers;
import org.testng.annotations.Test;

import com.testAutomation.constants.FileName;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

public class UpdateResourcePutCall {

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
	
	int bookingId = response.path("bookingid");
	
	
	String tokenAPIRequestBody = FileUtils.readFileToString(new File(FileName.TOKEN_GENERATOR_REQUEST_BODY),"UTF-8");
	String token = RestAssured
		.given()
			.contentType(ContentType.JSON)
			.baseUri("https://restful-booker.herokuapp.com/auth")
			.body(tokenAPIRequestBody)
		.when()
			.post()
		.then()
			.statusCode(200)
			.extract()
			.path("token");
	
	String updateResourcePUTRequestBody = FileUtils.readFileToString(new File(FileName.UPDATE_RSOURCE_REQUEST_BODY),"UTF-8");
	RestAssured
		.given().log().all()
			.baseUri("https://restful-booker.herokuapp.com/booking/")
			.contentType(ContentType.JSON)
			.pathParam("b_id", bookingId)
			.body(updateResourcePUTRequestBody)
			.header("Cookie","token="+token)
		.when()
			.put("{b_id}")
		.then()
			.statusCode(200)
			.log().all()
			.body("firstname", Matchers.equalTo("Specflow"))
			.body("lastname", Matchers.equalTo("Selenium C#"));
			
	//Partial Update PATCH Call
	String partialupdateResourcePUTRequestBody = FileUtils.readFileToString(new File(FileName.PARTIAL_UPDATE_RSOURCE_REQUEST_BODY),"UTF-8");
	RestAssured
		.given().log().all()
			.baseUri("https://restful-booker.herokuapp.com/booking/")
			.contentType(ContentType.JSON)
			.pathParam("b_id", bookingId)
			.body(partialupdateResourcePUTRequestBody)
			.header("Cookie","token="+token)
		.when()
			.patch("{b_id}")
		.then()
			.statusCode(200)
			.log().all()
			.body("firstname", Matchers.equalTo("Testers Talk"));
	}
}
