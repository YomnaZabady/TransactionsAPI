import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.Instant;

import org.apache.http.HttpStatus;
import org.junit.Test;

import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.response.Response;

import junit.framework.Assert;



public class TransactionsTest{
    
    
    @Test
    public void testCreateNewTransactionRequest() throws URISyntaxException {
    	
    	Long now = Instant.now().toEpochMilli();
    	
    	Long transTime1 = now - 100;
    	Long transTime2 = now - 1000;
    	Long transTime3 = now - 10000;

    	given().
    	        accept(ContentType.JSON).
    	        contentType(ContentType.JSON).
    	        formParam("param1", Double.valueOf("25.5")).
    	        formParam("param2", transTime1).
    	when().
    	   		post(new URI("http://localhost:8080/TransactionsAPI/rest/api/newTransaction")).
    	then().
    			statusCode(201);


    	given().
    	        accept(ContentType.JSON).
    	        contentType(ContentType.JSON).
    	        formParam("param1", Double.valueOf("125")).
    	        formParam("param2", transTime2).//Long.valueOf("1528448216350")).
    	when().
    	   		post(new URI("http://localhost:8080/TransactionsAPI/rest/api/newTransaction")).
    	then().
    			statusCode(201);
    	
    	given().
		        accept(ContentType.JSON).
		        contentType(ContentType.JSON).
		        formParam("param1", Double.valueOf("49.5")).
		        formParam("param2", transTime3).
		when().
		   		post(new URI("http://localhost:8080/TransactionsAPI/rest/api/newTransaction")).
		then().
				statusCode(201);
    	

    }

    @Test
    public void testGetTransactionStats() throws URISyntaxException{

    	int code = given().accept(ContentType.JSON).when()
    			.get(new URI("http://localhost:8080/TransactionsAPI/rest/api/apiStats"))
    			.thenReturn().statusCode();
    	Response response = given().accept(ContentType.JSON).when()
    			.get(new URI("http://localhost:8080/TransactionsAPI/rest/api/apiStats"));
    	
    	given().accept(ContentType.JSON).when()
		.get(new URI("http://localhost:8080/TransactionsAPI/rest/api/apiStats"))
		.then().assertThat().statusCode(HttpStatus.SC_OK);
    	
    	//Capture the outcome of the response --> then resturns
    	//Validate -> then()
    	given().accept(ContentType.JSON).when()
		.get(new URI("http://localhost:8080/TransactionsAPI/rest/api/apiStats"))
		.then().body("sum", equalTo(200.0f)).body("max", equalTo(125.0f))
		.body("min", equalTo(25.5f)).body("count", equalTo(3));
		System.out.println(response.asString());
    	Assert.assertEquals(HttpStatus.SC_OK, code);
    	//Assert.assertEquals(HttpStatus.SC_BAD_GATEWAY, code);
    }
    
    
    @Test
    public void testFalseCreateNewTransactionRequest() throws URISyntaxException {
    	
    	Long now = Instant.now().toEpochMilli();
    	Long transTime = now - 100000;
    	
    	given().
		        accept(ContentType.JSON).
		        contentType(ContentType.JSON).
		        formParam("param1", Double.valueOf("497.9")).
		        formParam("param2", transTime).//Long.valueOf("1528498217740")).
		when().
		   		post(new URI("http://localhost:8080/TransactionsAPI/rest/api/newTransaction")).
		then().
				statusCode(204);
    }
}
