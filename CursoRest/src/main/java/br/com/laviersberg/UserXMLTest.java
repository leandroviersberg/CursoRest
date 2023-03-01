
/*
 Aqui será analisado de como trabalhar com XML, a estrutura é essa:
 
 <users>
<user id="1">
<name>João da Silva</name>
<age>30</age>
<salary>1234.5678</salary>
</user>
<user id="2">
<name>Maria Joaquina</name>
<age>25</age>
<salary>2500</salary>
<endereco>
<rua>Rua dos bobos</rua>
<numero>0</numero>
</endereco>
</user>
<user id="3">
<name>Ana Julia</name>
<age>20</age>
<filhos>
<name>Zezinho</name>
<name>Luizinho</name>
</filhos>
</user>
</users>
Observação: Todos os valores de XML são considerados como Strings

 */


package br.com.laviersberg;

import static io.restassured.RestAssured.given;

import static org.hamcrest.Matchers.*;

import java.util.ArrayList;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import io.restassured.internal.path.xml.NodeImpl;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;

//import org.hamcrest.Matchers.*;


public class UserXMLTest {
	
	public static RequestSpecification reqSpec;
	public static ResponseSpecification resSpec;
	
	
	@BeforeClass
	public static void setup() {
		RestAssured.baseURI = "https://restapi.wcaquino.me";
//		RestAssured.port = 443;
//		RestAssured.basePath = "/v2";
		
		RequestSpecBuilder reqBuilder = new RequestSpecBuilder();
		reqBuilder.log(LogDetail.ALL);
		reqSpec = reqBuilder.build();
		
		ResponseSpecBuilder resBuilder = new ResponseSpecBuilder();
		resBuilder.expectStatusCode(200);
		resSpec = resBuilder.build();
		
		RestAssured.requestSpecification = reqSpec;
		RestAssured.responseSpecification = resSpec;
		
	}
	
	@Test
	public void TrabalharComXML() {
			
		given()
		.when()
			.get("/usersXML/3")
		.then()
			//.statusCode(200)
				
			/*Nó Raiz: User, se eu definir o RootPath "User", não será mais necessário
			colocar "name", pois a partir desta definição ja deixará atribuido para todo o body
				
			*/
			
			.rootPath("user")
			.body("name",is("Ana Julia"))
			.body("@id", is("3")) //Para o XML todos os valores são string
			//Outra forma é repetir o Root Path, neste caso será 'user.filhos'
			.rootPath("user.filhos")
			.body("name.size()", is(2)) //Uma Observação: Aqui não está sendo tratado '2' Como String, ele esta relacionado ao tamanho do elemento referenciado, no caso name possui dois elementos.
			//.detachRootPath("filhos") - Caso queira remover apenas os filhos
			//.appendRootPath("filhos") -  caso queira adicionar novamente os filhos
			.body("name[0]", is("Zezinho")) //Verifica a posição da lista 
			.body("name[1]", is("Luizinho"))
			.body("name", hasItem("Luizinho")) //Verifica apenas um item
			.body("name", hasItems("Luizinho", "Zezinho")) //Verifica mais de um item
			;
			
		
		
	}
	
	@Test
	public void TrabalharComXMLAvancado() {
		given()
		.when()
			.get("/usersXML")
		.then()
		
		//.statusCode(200)
			.body("users.user.size()", is(3))
			//Foi usado 'ToInteger' na idade para converter a tipagem do valor, ou seja , de String para Int.
			.body("users.user.findAll{it.age.toInteger() <= 25}.size()", is(2))
			.body("users.user.@id", hasItems("1", "2", "3"))
			.body("users.user.find{it.age == 25}.name", is("Maria Joaquina"))
			.body("users.user.findAll{it.name.toString().contains('n')}.name", hasItems("Maria Joaquina", "Ana Julia"))
			.body("users.user.salary.find{it != null}.toDouble()", is(1234.5678d))
			.body("users.user.age.collect{it.toInteger() * 2 }", hasItems(40, 50, 60))
			.body("users.user.name.findAll{it.toString().startsWith('Maria')}.collect{it.toString().toUpperCase()}", is("MARIA JOAQUINA"));
				
	}		
	
	@Test
	public void unindoXMLComJava() {
		//Object path = given()
		ArrayList<NodeImpl> nomes = given()
		//String nome = given()
		.when()
			.get("/usersXML")
		.then()
			.statusCode(200)
			//.extract().path("users.user.name.findAll{it.toString().startsWith('Maria')}");
			.extract().path("users.user.name.findAll{it.toString().contains('n')}");
		Assert.assertEquals(2, nomes.size());
		//System.out.println(path);
		Assert.assertEquals("Maria Joaquina".toUpperCase(), nomes.get(0).toString().toUpperCase());
		Assert.assertTrue("ANA JULIA".equalsIgnoreCase(nomes.get(1).toString()));
		
		
  }		

	@Test
	public void unindoXMLComXPath() {
	
		given()
		.when()
			.get("/usersXML")
		.then()
			.statusCode(200)
			.body(hasXPath("count(/users/user)", is("3")))
			.body(hasXPath("/users/user[@id = '1']")) //Para trazer o atributo pelo xPath é usado [@(atributo) no caso é [@id = '1']
		    .body(hasXPath("//user[@id = '2']")) // neste exemplo (//) ira fazer a busca do nivel que está associado ao ID e vai descendo até achar 
			.body(hasXPath("//name[text() = 'Luizinho']/../../name", is("Ana Julia")))
		    .body(hasXPath("//name[text() = 'Ana Julia']/following-sibling::filhos", allOf(containsString("Zezinho"), containsString("Luizinho"))))
			.body(hasXPath("/users/user/name", is("João da Silva")))		
			.body(hasXPath("//name", is("João da Silva")))			
			.body(hasXPath("/users/user[2]/name", is("Maria Joaquina")))			
			.body(hasXPath("/users/user[last()]/name", is("Ana Julia")))
			.body(hasXPath("count(/users/user/name[contains(.,'n')])", is("2")))	
			.body(hasXPath("//user[age < 24]/name", is("Ana Julia")))	
			.body(hasXPath("//user[age > 20 and age < 30]/name", is("Maria Joaquina")))
			.body(hasXPath("//user[age > 20][ age < 30]/name", is("Maria Joaquina")))
			
			
		    ;
		    
		    
		    
		    
		    
	}
	
}

