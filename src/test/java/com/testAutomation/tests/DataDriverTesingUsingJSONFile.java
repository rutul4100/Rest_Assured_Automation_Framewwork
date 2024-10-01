package com.testAutomation.tests;

import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;

import org.apache.commons.io.FileUtils;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.jayway.jsonpath.JsonPath;
import com.testAutomation.constants.FileName;
import com.testAutomation.listeners.RestApiListener;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;

public class DataDriverTesingUsingJSONFile {

	
	@Test(dataProvider="getTestData")
	public void dataDriverTest(LinkedHashMap<String,String> testData) throws IOException {
		JSONObject booking = new JSONObject();
		JSONObject bookingDates = new JSONObject();
		
		booking.put("firstname", testData.get("firstname"));
		booking.put("lastname", testData.get("lastname"));
		booking.put("totalprice", 1000);
		booking.put("depositpaid",true);
		booking.put("bookingdates",bookingDates);
		booking.put("additionalneeds", "super");
		
		bookingDates.put("checkin", "2018-01-01");
		bookingDates.put("checkout", "2019-01-02");
		
		RestAssured
			.given()
				.filter(new RestApiListener())
				.contentType(ContentType.JSON)
				.body(booking.toString())
				.baseUri("https://restful-booker.herokuapp.com/booking")
				//.log().body()
			.when()
				.post()
			.then()
				//.log().ifValidationFails()	
				.assertThat()
				.statusCode(200);
	}
	
	@DataProvider(name="getTestData")
	public Object[] getTestDataUsingJSON() throws IOException {
		Object[] obj = null;
		String jsonTestData = FileUtils.readFileToString(new File(FileName.DATA_DRIVER_TESTING_USING_JSONFile),"UTF-8");
		
		JSONArray arr = JsonPath.read(jsonTestData, "$");
		obj =  new Object[arr.size()];
		
		for(int i=0;i<arr.size();i++) {
			obj[i]=arr.get(i);
		}
		return obj;
	}
	
}
