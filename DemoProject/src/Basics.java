import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.testng.Assert;

import files.Payload;
import files.ReUsableMethods;

public class Basics {
	
	public static void main(String[] args) throws IOException {
		
		//validate addPlace API is working fine
		//given - all input details 
		//when - submit the API
		//then - validate the response
		
		//AddPlace->UpdatePlace with new address ->GetPlace to validate new address present in response
		
		RestAssured.baseURI = "https://rahulshettyacademy.com";
		
		String response = given().log().all().queryParam("key", "qaclick123").header("Content-Type", "application/json")
		.body(new String(Files.readAllBytes(Paths.get("C:\\Users\\krahul\\Downloads\\addPlace.json"))))
		.when().post("maps/api/place/add/json").then().assertThat().statusCode(200)
		.body("scope", equalTo("APP"))
		.header("Server", "Apache/2.4.41 (Ubuntu)").extract().response().asString();
		
		
		System.out.println("Response: " +response);
		JsonPath js = new JsonPath(response);
		String placeID = js.getString("place_id");
		System.out.println("PlaceID: " +placeID);
		
		//updatePlace
		String newAddress = "Summer Walk, Africa";
		
		given().log().all().queryParam("key", "qaclick123").header("Content-Type", "application/json")
		.body("{\r\n"
				+ "\"place_id\":\""+placeID+"\",\r\n"
				+ "\"address\":\""+newAddress+"\",\r\n"
				+ "\"key\":\"qaclick123\"\r\n"
				+ "}\r\n"
				+ "")
		.when().put("maps/api/place/update/json")
		.then().log().all().assertThat().statusCode(200).body("msg", equalTo("Address successfully updated"));
		
		//GetPlace
		String getPlaceResponse = given().log().all().queryParam("key", "qaclick123").queryParam("place_id", placeID)
		.when().put("maps/api/place/get/json")
		.then().log().all().assertThat().statusCode(200).extract().response().asString();
		
		System.out.println("Get Place response:" +getPlaceResponse);
		
		JsonPath js1 = ReUsableMethods.rawToJson(getPlaceResponse);
		String actualAddress = js1.getString("address");
		System.out.println("Actual Address: " +actualAddress);
		
		//Assertion
		Assert.assertEquals(actualAddress, newAddress, "Response address matched");
	}

}
