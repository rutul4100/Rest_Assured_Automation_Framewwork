package com.testAutomation.tests;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.codoid.products.exception.FilloException;
import com.codoid.products.fillo.Connection;
import com.codoid.products.fillo.Fillo;
import com.codoid.products.fillo.Recordset;
import com.testAutomation.constants.FileName;
import com.testAutomation.listeners.RestApiListener;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import net.minidev.json.JSONObject;

public class DDTUsingExcelFile {
	
	
  @Test(dataProvider="testData")
  public void testFromExcel(TreeMap<String,String> testData) {
	  JSONObject booking = new JSONObject();
		JSONObject bookingDates = new JSONObject();
		
		booking.put("firstname", testData.get("firstname"));
		booking.put("lastname", testData.get("lastname"));
		booking.put("totalprice",  testData.get("totalprice"));
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
  public Object[][] testData() throws FilloException{
	  
	  String query="select * from Sheet1 where Run='Y'";
	  Object[][] obj = null;
	  Map<String,String> map = null;
	  List<Map<String,String>> list =null;
	  
	  Fillo filo=new Fillo();
	  Connection con = null;
	  Recordset recordset=null;
	 con= filo.getConnection(FileName.TESTDATA_TESTING_USING_EXCELFile);
	  recordset = con.executeQuery(query);
	  list=new ArrayList<>();
	  
	  while(recordset.next()) {
		  map=new TreeMap<>();
		  
		  for(String field:recordset.getFieldNames()) {
			  map.put(field, recordset.getField(field));
		  }
		  list.add(map);
	  }
	  
	  obj = new  Object[list.size()][1];
	  for(int i=0;i<list.size();i++) {
		  obj[i][0]= list.get(i);
	  }
	  
	  return obj;
  }
}
