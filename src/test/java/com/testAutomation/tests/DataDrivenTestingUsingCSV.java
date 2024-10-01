package com.testAutomation.tests;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import com.testAutomation.constants.FileName;
import com.testAutomation.listeners.RestApiListener;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import net.minidev.json.JSONObject;

public class DataDrivenTestingUsingCSV {
	
  @Test(dataProvider="data")
  public void dataDriverTest(Map<String,String> map) {
	  JSONObject booking = new JSONObject();
		JSONObject bookingDates = new JSONObject();
		
		booking.put("firstname", map.get("firstname"));
		booking.put("lastname", map.get("lastname"));
		booking.put("totalprice", map.get("totalprice"));
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
  
  @DataProvider
  public Object[][] data() throws CsvValidationException, IOException {
	  Object[][] obj= null;
	  Map<String,String> map = null;
	  List<Map<String,String>> list = null;
	  
	  CSVReader csvReader = new CSVReader(new FileReader(FileName.TESTDATA_TESTING_USING_CSVFile));
	  list = new ArrayList<>();
	  
	  String[]  line =null;
	  int count=0;
	  while((line =csvReader.readNext())!=null) {
		  
		  if(count==0) {
			  count++;
			  continue;
		  }
		  map=new TreeMap<String,String>(String.CASE_INSENSITIVE_ORDER);
		  map.put("firstname", line[0]);
		  map.put("lastname", line[1]);
		  map.put("totalprice", line[2]);
		  
		  list.add(map);
	  }
	  obj = new Object[list.size()][1];
	  
	  for(int i=0;i<list.size();i++) {
		  obj[i][0]= list.get(i);
	  }
	  return  obj;
  }
}
