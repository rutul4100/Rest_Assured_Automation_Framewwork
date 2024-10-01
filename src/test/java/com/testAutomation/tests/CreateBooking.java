package com.testAutomation.tests;

import org.hamcrest.Matchers;
import org.testng.annotations.Test;

import com.testAutomation.utils.BaseTest;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import net.minidev.json.JSONObject;

public class CreateBooking extends BaseTest{

	
	@Test
	public void createBooking() {
		
		
		
		JSONObject booking = new JSONObject();
		JSONObject bookingDates = new JSONObject();
		
		booking.put("firstname", "api testing");
		booking.put("lastname", "tutorial");
		booking.put("totalprice", 1000);
		booking.put("depositpaid",true);
		booking.put("bookingdates",bookingDates);
		booking.put("additionalneeds", "super");
		
		bookingDates.put("checkin", "2018-01-01");
		bookingDates.put("checkout", "2019-01-02");
		
	Response response=	RestAssured
			.given()
				.contentType(ContentType.JSON)
				.body(booking.toString())
				.baseUri("https://restful-booker.herokuapp.com/booking")
				//.log().body()
			.when()
				.post()
			.then()
				//.log().ifValidationFails()	
				.assertThat()
				.statusCode(200)
				.body("booking.firstname", Matchers.equalTo("api testing"))
				.body("booking.totalprice", Matchers.equalTo(1000))
		.body("booking.bookingdates.checkin", Matchers.equalTo("2018-01-01"))
		.extract().response();
	
	
	int bookingId = response.path("bookingid");
	String name = response.path("booking.firstname");
	System.out.println(name);
	RestAssured
		.given()
			.contentType(ContentType.JSON)
			.pathParam("bookingId", bookingId)
			.baseUri("https://restful-booker.herokuapp.com/booking")
			
		.when()
			.get("{bookingId}")
		.then().log().all()
			.assertThat()
			.statusCode(200);
			
		
		
					
		
	}
}
