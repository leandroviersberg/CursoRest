/*Trabalhando com Verbos Rest
 
 */


package br.com.laviersberg;

import static io.restassured.RestAssured.given;
//import org.hamcrest.Matchers;
import static org.hamcrest.Matchers.*;

import org.junit.Assert;
import org.junit.Test;
import io.restassured.http.*;
import java.util.*;
public class VerbosTest {
	
	@Test
	public void SalvandUsuario() {
		
		given()
			.log().all()
			.contentType("application/json") //Tipo de objeto a ser tratado, no caso JSON
			.body("{\"name\": \"Jose\",\"age\": 50}")
			
		.when()
			.post("https://restapi.wcaquino.me/users")
		.then()
			.log().all()
			.statusCode(201)
			.body("id", is(notNullValue()))
			.body("name", is("Jose"))
			.body("age", is(50))
			
		;	
		
		
	}
	
	//Serialização de MAP: Convertendo para JSON
	@Test
	public void SalvandUsuarioUsandoMAP() {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("name", "Usuario via map");
		params.put("age", 25);
		
		given()
			.log().all()
			.contentType("application/json") //Tipo de objeto a ser tratado, no caso JSON
			.body(params)
			
		.when()
			.post("https://restapi.wcaquino.me/users")
		.then()
			.log().all()
			.statusCode(201)
			.body("id", is(notNullValue()))
			.body("name", is("Usuario via map"))
			.body("age", is(25))
			
		;	
	
	}
	
	//Serialização de Objeto: Convertendo para JSON
		@Test
		public void SalvandUsuarioUsandoObjeto() {
			User user = new User("Usuario via objeto", 35);
						
			given()
				.log().all()
				.contentType("application/json") //Tipo de objeto a ser tratado, no caso JSON
				.body(user)
				
			.when()
				.post("https://restapi.wcaquino.me/users")
			.then()
				.log().all()
				.statusCode(201)
				.body("id", is(notNullValue()))
				.body("name", is("Usuario via objeto"))
				.body("age", is(35))
				
			;	
		
		}
	
		
		//Deserializando Objeto
		@Test
		public void deserializandoObjetoAoSalvarUsuario() {
			User user = new User("Usuario deserializado", 35);
						
			User usuarioInserido = given()
				.log().all()
				.contentType("application/json") //Tipo de objeto a ser tratado, no caso JSON
				.body(user)
				
			.when()
				.post("https://restapi.wcaquino.me/users")
			.then()
				.log().all()
				.statusCode(201)
				.extract().body().as(User.class)
				
			;	
			
			//System.out.println(usuarioInserido);
			Assert.assertThat(usuarioInserido.getId(), notNullValue());
			Assert.assertEquals("Usuario deserializado",usuarioInserido.getName());
			Assert.assertThat(usuarioInserido.getAge(), is(35));
			
		
		}
		
	@Test
	public void naoSalvarUserSemNome() {
		given()
		.log().all()
		.contentType("application/json") //Tipo de objeto a ser tratado, no caso JSON
		.body("{\"age\": 50}")
		
	.when()
		.post("https://restapi.wcaquino.me/users")
	.then()
		.log().all()
		.statusCode(400)
		.body("id", is(nullValue()))
		.body("error", is("Name é um atributo obrigatório"))
		;
	}
	
	
	
	@Test
	public void salvarUsuarioComXML() {
		
		given()
		.log().all()
		.contentType(ContentType.XML) //Tipo de objeto a ser tratado, no caso XML, foi utilizado ENUM (ContentType)
		.body("<user><name>Jose</name><age>50</age></user>")
		
	.when()
		.post("https://restapi.wcaquino.me/usersXML")
	.then()
		.log().all()
		.statusCode(201)
		.body("user.@id", is(notNullValue()))
		.body("user.name", is("Jose"))
		.body("user.age", is("50"))
		;
	}
	
	//Serializando com XML
	@Test
	public void salvarUsuarioComXMLUsandoObjeto() {
		User user = new User("Usuario XML", 40);
		
		given()
		.log().all()
		.contentType(ContentType.XML) 
		.body(user)
		
	.when()
		.post("https://restapi.wcaquino.me/usersXML")
	.then()
		.log().all()
		.statusCode(201)
		.body("user.@id", is(notNullValue()))
		.body("user.name", is("Usuario XML"))
		.body("user.age", is("40"))
		;
	}
	
	
	//deserializar XML
	@Test
	public void deserializarXMLAoSalvarUsuario() {
		User user = new User("Usuario XML", 40);
		
		User usuarioInserido = given()
		.log().all()
		.contentType(ContentType.XML) 
		.body(user)
		
	.when()
		.post("https://restapi.wcaquino.me/usersXML")
	.then()
		.log().all()
		.statusCode(201)
		.extract().body().as(User.class)
		;
		
		Assert.assertThat(usuarioInserido.getId(), notNullValue());
		Assert.assertThat(usuarioInserido.getName(), is("Usuario XML"));
		Assert.assertThat(usuarioInserido.getAge(), is(40));
		Assert.assertThat(usuarioInserido.getSalary(), nullValue());
	}
	
	
	
	//requisiçao PUT ( alteração de recurso )
	
	@Test
	public void alterandoUsuario() {
		
		given()
			.log().all()
			.contentType("application/json") //Tipo de objeto a ser tratado, no caso JSON
			.body("{\"name\": \"Usuario alterado\",\"age\": 80}")
			
		.when()
			.put("https://restapi.wcaquino.me/users/1")
		.then()
			.log().all()
			.statusCode(200)
			.body("id", is(1))
			.body("name", is("Usuario alterado"))
			.body("age", is(80))
			.body("salary", is(1234.5678f))
			;
	}
	
	//Customização de URL alternativa 1
	@Test
	public void customizacaoURL() {
		
		given()
			.log().all()
			.contentType("application/json") //Tipo de objeto a ser tratado, no caso JSON
			.body("{\"name\": \"Usuario alterado\",\"age\": 80}")
			
		.when()
			.put("https://restapi.wcaquino.me/{entidade}/{userId}", "users", "1")
		.then()
			.log().all()
			.statusCode(200)
			.body("id", is(1))
			.body("name", is("Usuario alterado"))
			.body("age", is(80))
			.body("salary", is(1234.5678f))
			;
	}
	
	@Test
	public void alterandoUsuarioOutraForma() {
		
		given()
			.log().all()
			.contentType("application/json") //Tipo de objeto a ser tratado, no caso JSON
			.body("{\"name\": \"Usuario alterado\",\"age\": 80}")
			.pathParam("entidade","users")
			.pathParam("userId", 1)
			
		.when()
			.put("https://restapi.wcaquino.me/{entidade}/{userId}", "users", "1")
		.then()
			.log().all()
			.statusCode(200)
			.body("id", is(1))
			.body("name", is("Usuario alterado"))
			.body("age", is(80))
			.body("salary", is(1234.5678f))
			;
	}
	
	//Delete usuario
	@Test
	public void removerUsuario() {
		
		given()
			.log().all()
		.when()
			.delete("https://restapi.wcaquino.me/users/1")
		.then()
			.log().all()
			.statusCode(204)
		
			;
	}
	
	@Test
	public void naoDeveRemoverUsuarioInexistente() {
		
		given()
			.log().all()
		.when()
			.delete("https://restapi.wcaquino.me/users/1000")
		.then()
			.log().all()
			.statusCode(400)
			.body("error", is("Registro inexistente"))
			
			;
			
	}		
}
	
	
	


