/* Trabalhando com querys
 * 
 */

package br.com.laviersberg;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
//import org.hamcrest.Matchers;
import org.junit.Test;

import io.restassured.http.ContentType;

public class EnviarDadosViaQuery {
	
	@Test
	public void deveEnviarDadosQuery() {
		given()
			.log().all()
		.when()
			.get("https://restapi.wcaquino.me/v2/users?format=json")
		.then()
			.log().all()
			.statusCode(200)
			.contentType(ContentType.JSON)
			
			;
		
	}

	@Test
	public void deveEnviarDadosQueryParam() {
		given()
			.log().all()
			.queryParam("format", "xml")
		.when()
			.get("https://restapi.wcaquino.me/v2/users")
		.then()
			.log().all()
			.statusCode(200)
			.contentType(ContentType.XML)
			.contentType(containsString("utf-8"))
			;
		
	}
	
	@Test
	public void deveEnviarDadosHeader() {
		given()
			.log().all()
			.accept(ContentType.XML)
		.when()
			.get("https://restapi.wcaquino.me/v2/users")
		.then()
			.log().all()
			.statusCode(200)
			.contentType(ContentType.XML)
			
			;
		
	}
	
}
