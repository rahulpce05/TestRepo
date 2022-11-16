package stepDefinitions;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.List;

import org.junit.runner.RunWith;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.junit.Cucumber;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import junit.framework.Assert;
import pojo.AddPlace;
import pojo.Location;


@RunWith(Cucumber.class)
public class StepDefinitions {
	
	RequestSpecification res;
	ResponseSpecification resSpec;
	Response response;
	@Given("add Place request Payload")
    public void add_place_request_payload() {
		
		AddPlace p = new AddPlace();
		p.setAccuracy(50);
		p.setAddress("29, side layout, cohen 09");
		p.setLanguage("French");
		p.setPhone_number("(+91) 983 893 3937");
		p.setName("Frontline house");
		p.setWebsite("https://rahulshettyacademy.com");
		
		List<String> myList = new ArrayList<String>();
		myList.add("shoe park");
		myList.add("shop");
		
		p.setTypes(myList);
		Location l = new Location();
		l.setLat(-38.383494);
		l.setLng(33.427362);
		p.setLocation(l);
		
		RequestSpecification req = new RequestSpecBuilder().setBaseUri("https://rahulshettyacademy.com").addQueryParam("key", "qaclick123")
				.setContentType(ContentType.JSON).build();
		
		resSpec = (ResponseSpecification) new ResponseSpecBuilder().expectStatusCode(200).expectContentType(ContentType.JSON);
		
		res = given().spec(req)
		.body(p);
    }

    @When("user calls addPlaceAPI with POST http request")
    public void user_calls_addplaceapi_with_post_http_request() {
    	response = res.when().post("/maps/api/place/add/json")
    			.then().spec(resSpec).extract().response();
    }

    @Then("api call is success with status code {String}")
    public void api_call_is_success_with_status_code_200(Integer int1) {
    	assertEquals(response.getStatusCode(), 200);
    }

    @And("{String} in response body is {String}")
    public void in_response_body_is(String keyValue, String expectedValue) {
    	String resp = response.asString();
    	JsonPath js = new JsonPath(resp);
    	assertEquals(js.get(keyValue), expectedValue);
    }
	
     
}
