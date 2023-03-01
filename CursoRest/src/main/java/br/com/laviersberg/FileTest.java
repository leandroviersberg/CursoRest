
/*Envio de arquivo
 
 */
package br.com.laviersberg;

import static org.hamcrest.Matchers.is;

import java.io.File;

import org.junit.Test;
import static io.restassured.RestAssured.given;

	public class FileTest {

	@Test
	public void deveEnviarObrigatoriamenteArquivo() {
		given()
			.log().all()
		.when()
			.post("https://restapi.wcaquino.me/upload")
		.then()
			.log().all()
			.statusCode(404) //Deveria ser 400
			.body("error", is("Arquivo não enviado"))		
		;
	}
		
	@Test
	public void deveFazerUploadDoArquivo() {
		given()
			.log().all()
			.multiPart("arquivo", new File("src/main/resources/users.pdf")) //envio do arquivo
		.when()
			.post("https://restapi.wcaquino.me/upload")
		.then()
			.log().all()
			.statusCode(200) //Deveria ser 400
			.body("name", is("users.pdf"))		
		;
	}
}
