package files;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.specification.RequestSpecification;
import pojo.LoginRequest;
import pojo.LoginResponse;
import pojo.OrderDetails;
import pojo.Orders;

import static io.restassured.RestAssured.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.testng.Assert;

public class EcommerceAPITest {

	public static void main(String[] args) {

		RequestSpecification req = new RequestSpecBuilder().setBaseUri("https://rahulshettyacademy.com")
		.setContentType(ContentType.JSON).build();
		
		LoginRequest loginRequest = new LoginRequest();
		loginRequest.setUserEmail("rahulshetty@gmail.com");
		loginRequest.setUserPassword("Iamking@000");
		
		RequestSpecification reqLogin = given().relaxedHTTPSValidation().log().all().spec(req).body(loginRequest);
		LoginResponse loginResponse = reqLogin.when().post("/api/ecom/auth/login").then().log().all().extract().response().as(LoginResponse.class);
		String token = loginResponse.getToken();
		String userID = loginResponse.getUserId();
		System.out.println("Token: " +token);
		System.out.println("UserId: " +userID);
		
		//Add Product
		RequestSpecification addProductBaseRequest = new RequestSpecBuilder().setBaseUri("https://rahulshettyacademy.com")
				.addHeader("authorization", token).build();
		
		RequestSpecification reqAddProduct = given().log().all().spec(addProductBaseRequest)
		.param("productName", "Clothing").param("productAddedBy", userID).param("productCategory", "fashion")
		.param("productSubCategory", "shirts").param("productPrice", 1500).param("productDescription", "Adidas Originals")
		.param("productFor", "Women").multiPart("productImage", new File("C:\\Users\\krahul\\Downloads\\ladies-short.jpg"));
		
		String addProductResponse = reqAddProduct.when().post("/api/ecom/product/add-product")
		.then().log().all().extract().response().asString();
		
		JsonPath js = new JsonPath(addProductResponse);
		String productId = js.get("productId");
		
		//Create Order
		RequestSpecification createOrderBaseRequest = new RequestSpecBuilder().setBaseUri("https://rahulshettyacademy.com")
				.addHeader("authorization", token).setContentType(ContentType.JSON).build();
		
		OrderDetails orderDetails = new OrderDetails();
		orderDetails.setCountry("India");
		orderDetails.setProductOrderedId(productId);
		
		List<OrderDetails> orderDetailsList = new ArrayList<OrderDetails>();
		orderDetailsList.add(orderDetails);
		
		Orders orders = new Orders();
		orders.setOrders(orderDetailsList);
		
		RequestSpecification createOrderReq = given().log().all().spec(createOrderBaseRequest).body(orders);
		String responseAddOrder = createOrderReq.when().post("/api/ecom/order/create-order")
		.then().log().all().extract().response().asString();
		System.out.println(responseAddOrder);
		
		//Delete Product
		RequestSpecification deleteProductBaseRequest = new RequestSpecBuilder().setBaseUri("https://rahulshettyacademy.com")
				.addHeader("authorization", token).setContentType(ContentType.JSON).build();
		
		RequestSpecification deleteProductReq = given().log().all().spec(deleteProductBaseRequest).pathParam("productId", productId);
		
		String deleteProductResponse = deleteProductReq.when().delete("/api/ecom/product/delete-product/{productId}")
		.then().log().all().extract().response().asString();
		System.out.println(deleteProductResponse);
		
		JsonPath js1 = new JsonPath(deleteProductResponse);
		Assert.assertEquals("Product Deleted Successfully", js1.get("message"));
	}

}
