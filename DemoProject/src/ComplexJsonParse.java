import files.Payload;
import io.restassured.path.json.JsonPath;

public class ComplexJsonParse {

	public static void main(String[] args) {

		JsonPath js = new JsonPath(Payload.coursePrice());

		// print no of courses return by API
		int count = js.getInt("courses.size()");
		System.out.println("Courses Size: " + count);

		// print purchase amount
		int totalAmount = js.getInt("dashboard.purchaseAmount");
		System.out.println("Total Amount: " + totalAmount);

		// Print title of first course
		String titleFirstCourse = js.getString("courses[0].title");
		System.out.println("Title of First Course: " + titleFirstCourse);

		// print all courses and respective prices
		for (int i = 0; i < count; i++) {
			String courseTitles = js.getString("courses[" + i + "].title");
			System.out.print("courseTitles: " + courseTitles);

			if (courseTitles.equalsIgnoreCase("RPA")) {

				int copies = js.getInt("courses[" + i + "].copies");
				System.out.print(", RPA Copies: " + copies);
			}

			int coursePrices = js.getInt("courses[" + i + "].price");
			System.out.print(", coursePrices: " + coursePrices);
			System.out.println("\n");
		}
		
		//Sum of all purchase amount
		int actualPurchaseAmount = 0;
		int expPurchaseAmount = 1162;
		for (int i = 0; i < count; i++) {
			String courseTitles = js.getString("courses[" + i + "].title");
			System.out.print("courseTitles: " + courseTitles);


			int copies = js.getInt("courses[" + i + "].copies");
			System.out.print("copies: " +copies);

			int coursePrices = js.getInt("courses[" + i + "].price");
			System.out.print(", coursePrices: " + coursePrices);
			System.out.println("\n");
			
			actualPurchaseAmount = actualPurchaseAmount + coursePrices * copies;
		}
		System.out.println("Purchase Amount: " +actualPurchaseAmount);

	}

}
