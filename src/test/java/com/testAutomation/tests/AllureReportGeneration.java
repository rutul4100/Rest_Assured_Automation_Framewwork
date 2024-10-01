package com.testAutomation.tests;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.Test;

import com.testAutomation.constants.FileName;
import com.testAutomation.listeners.RestApiListener;
import com.testAutomation.utils.BaseTest;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

@Epic("Epic-01")
@Feature("Create update delete booking")
public class AllureReportGeneration extends BaseTest{

private static final Logger logger=LogManager.getLogger(AllureReportGeneration.class);
		
// mvn test -Dsuitefilename="allureReport.xml"
//It will openn webpage report
//mvn serve
		@Story("Story 1")
		@Test(description="e2e API Test")
		@Description("end to end testing")
		@Severity(SeverityLevel.CRITICAL)
		public void  deleteResource() throws IOException {
			
			logger.info("e2e test started...");
					
		String requestBody = FileUtils.readFileToString(new File(FileName.CREATE_BOOKING_REQUEST_BODY),"UTF-8");
		
		
		Response response=	RestAssured
				.given()
					.filter(new AllureRestAssured())
					.filter(new RestApiListener())
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
		
		RestAssured
		.given()
			.filter(new AllureRestAssured())
			.filter(new RestApiListener())
			.contentType(ContentType.JSON)
			.pathParam("bookingId", bookingId)
			.baseUri("https://restful-booker.herokuapp.com/booking")
			
		.when()
			.get("{bookingId}")
		.then()
			.assertThat()
			.statusCode(200);
		
		String tokenAPIRequestBody = FileUtils.readFileToString(new File(FileName.TOKEN_GENERATOR_REQUEST_BODY),"UTF-8");
		String token = RestAssured
			.given()
				.filter(new AllureRestAssured())
				.filter(new RestApiListener())
				.contentType(ContentType.JSON)
				.baseUri("https://restful-booker.herokuapp.com/auth")
				.body(tokenAPIRequestBody)
			.when()
				.post()
			.then()
				.statusCode(200)
				.extract()
				.path("token");
		
		RestAssured
			.given()
				.filter(new AllureRestAssured())
				//.filter(new RestApiListener())
				.contentType(ContentType.JSON)
				.header("cookie","token="+token)
				.baseUri("https://restful-booker.herokuapp.com/booking/")
				.pathParam("b_id", bookingId)
			.when()
				.delete("{b_id}")
			.then()
				.statusCode(201);
		
		RestAssured
		.given()
			.filter(new AllureRestAssured())
			.contentType(ContentType.JSON)
			.pathParam("bookingId", bookingId)
			.baseUri("https://restful-booker.herokuapp.com/booking")
			
		.when()
			.get("{bookingId}")
		.then()
			.assertThat()
			.statusCode(404);
		logger.info("e2e test ended...");

	}
	
}
