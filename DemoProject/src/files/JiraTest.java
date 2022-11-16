package files;

import static io.restassured.RestAssured.*;

import java.io.File;

import io.restassured.RestAssured;
import io.restassured.filter.session.SessionFilter;
public class JiraTest {

	public static void main(String[] args) {
		
		RestAssured.baseURI = "http://localhost:8081";

		SessionFilter session = new SessionFilter();
		String response = given().relaxedHTTPSValidation().header("Content-Type", "application/json").body("{\r\n"
				+ "    \"username\": \"rahulpce05\",\r\n"
				+ "    \"password\": \"maa@VHE85\"\r\n"
				+ "}").log().all().filter(session).when().post("rest/auth/1/session")
		.then().log().all().extract().response().asString();
		
		
		given().pathParam("key", "10005").log().all().header("Content-Type", "application/json").body("{\r\n"
				+ "    \"body\": \"Hey, I have commented using REST API\",\r\n"
				+ "    \"visibility\": {\r\n"
				+ "        \"type\": \"role\",\r\n"
				+ "        \"value\": \"Administrators\"\r\n"
				+ "    }\r\n"
				+ "}").filter(session).when().post("rest/api/2/issue/{key}/comment")
		.then().log().all().assertThat().statusCode(201);
		
		given().header("X-Atlassian-Token", "no-check").filter(session).pathParam("key", "10005")
		.header("Content-Type", "multipart/form-data")
		.multiPart("file", new File("jira.txt")).when().post("rest/api/2/issue/{key}/attachments")
		.then().log().all().assertThat().statusCode(200);
	}

}
