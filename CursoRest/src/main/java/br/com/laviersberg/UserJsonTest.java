/* O parametro usado para o Primeiro nivel são estes:
 {
	"id": 1,
	"name": "João da Silva",
	"age": 30,
	"salary": 1234.5678
}
 */


package br.com.laviersberg;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
//import static org.hamcrest.Matchers.arrayContaining;
//import static org.hamcrest.Matchers.arrayWithSize;
//import static org.hamcrest.Matchers.contains;
//import static org.hamcrest.Matchers.containsString;
//import static org.hamcrest.Matchers.greaterThan;
//import static org.hamcrest.Matchers.hasItem;
//import static org.hamcrest.Matchers.hasItems;
//import static org.hamcrest.Matchers.hasSize;

import java.util.ArrayList;
import java.util.Arrays;

//import org.hamcrest.Matchers.*;
import org.junit.Assert;
import org.junit.Test;

import io.restassured.RestAssured;

import io.restassured.http.Method;

import io.restassured.path.json.JsonPath;

import io.restassured.response.Response;

public class UserJsonTest {
	
	@Test
	public void primeiroNivelJson() {
		given()
		.when()
			.get("https://restapi.wcaquino.me/users/1") //metodo GET
		.then()
		.statusCode(200)
		.body("id", is(1))
		.body("name", containsString("Silva"))
		.body("age", greaterThan(18));
		
	
		
	}
	
	@Test
	public void verificarPrimeiroNivelOutraForma() {
		Response response = RestAssured.request(Method.GET, "https://restapi.wcaquino.me/users/1");
		
		//path
		
		Assert.assertEquals(new Integer(1), response.path("id"));
		Assert.assertEquals(new Integer(1), response.path("%s", "id"));
		
		//Jsonpath
		
		JsonPath jpath = new JsonPath(response.asString());
		Assert.assertEquals(1, jpath.getInt("id"));
		
		//from
		
		int id = JsonPath.from(response.asString()).getInt("id");
		Assert.assertEquals(1, id);
	}
	
	//Trabalhando com segundo nivel
	@Test
	public void segundoNivelJson() {
		given()
		.when()
			.get("https://restapi.wcaquino.me/users/2") 
		.then()
		.statusCode(200)
		.body("name", containsString("Joaquina"))
		.body("endereco.rua", is("Rua dos bobos"));
		
	}
	
	/* Trabalhando com o nivel 3,
	 Será analisado a lista deste nivel, no caso será tratadp dois objetos 
	a referencia do objeto é o 'name', sera verificado o tamanho da lista name.
	
	 {
	"id": 3,
	"name": "Ana Júlia",
	"age": 20,
	"filhos": [
		{
			"name": "Zezinho"
		},
		{
			"name": "Luizinho"
		}
	]
	 */
	
	@Test
	public void verificarLista() {
		given()
		.when()
			.get("https://restapi.wcaquino.me/users/3") 
		.then()
		.statusCode(200)
		.body("name", containsString("Ana"))
		.body("filhos", hasSize(2))
		.body("filhos[0].name", is("Zezinho")) //o item "filhos" contem um array onde fica a posição da lista, neste caso [0] se refere ao zezinho.
		.body("filhos[1].name", is("Luizinho"))
		.body("filhos.name", hasItem("Zezinho"))
		.body("filhos.name", hasItems("Zezinho","Luizinho"))
		
		;
		
	}
		
	@Test
	public void erroUsuarioInexistente() {
	given()
	.when()
		.get("https://restapi.wcaquino.me/users/4") 
	.then()
	.statusCode(404)
	.body("error", is("Usuário inexistente"))
	;
  }
	
	
	/*
	 Estamos trabalhando com a lista completa
	 
	 [
	{
		"id": 1,
		"name": "João da Silva",
		"age": 30,
		"salary": 1234.5678
	},
	{
		"id": 2,
		"name": "Maria Joaquina",
		"endereco": {
			"rua": "Rua dos bobos",
			"numero": 0
		},
		"age": 25,
		"salary": 2500
	},
	{
		"id": 3,
		"name": "Ana Júlia",
		"age": 20,
		"filhos": [
			{
				"name": "Zezinho"
			},
			{
				"name": "Luizinho"
			}
		]
	}
]
	 
	 */
	@Test
	public void deveListarRaiz() {
	given()
	.when()
		.get("https://restapi.wcaquino.me/users/") 
	.then()
	.statusCode(200)
	.body("$", hasSize(3))
	.body("name", hasItems("João da Silva", "Maria Joaquina", "Ana Júlia"))
	.body("age[1]", is(25))
	.body("filhos.name", hasItem(Arrays.asList("Zezinho", "Luizinho")))
	.body("salary", contains(1234.5678f,2500, null)); //Observe que o numero "1234.5678" possui a letra 'f', isto significa que este numero é do tipo float, ou seja, não inteiro
	
	}
	//Verificações Avançadas
	
	
	@Test
	
	public void verificacoesAvancadas() {
		given()
		.when()
			.get("https://restapi.wcaquino.me/users/") 
		.then()
		.statusCode(200)
		.body("$", hasSize(3))
		.body("age.findAll{it <=25}.size()",is(2))
		.body("age.findAll{it <=25 && it > 20}.size()",is(1))
		.body("findAll{it.age <=25 && it.age > 20}.name",hasItem("Maria Joaquina"))
		.body("findAll{it.age <=25}[0].name",is("Maria Joaquina"))
		.body("findAll{it.age <=25}[-1].name",is("Ana Júlia")) // [-1] é ultimo que contem da lista, será verificado de baixo para cima.
		.body("find{it.age <=25}.name",is("Maria Joaquina")) //"find" ira trazer apenas um registro 
		.body("findAll{it.name.contains('n')}.name",hasItems("Maria Joaquina", "Ana Júlia")) //Será verificado quais os nomes contem(contains) a letra N
		.body("findAll{it.name.length()> 10}.name",hasItems("João da Silva", "Maria Joaquina"))//Verificar qual a qtd de caracteres de cada nome (length() >10
		.body("name.collect{it.toUpperCase()}",hasItem("MARIA JOAQUINA"))
		.body("name.findAll{it.startsWith('Maria')}.collect{it.toUpperCase()}",hasItem("MARIA JOAQUINA"))
		//.body("name.findAll{it.startWith('Maria')}.collect{it.toUpperCase()}.toArray()", allOf(arrayContaining("MARIA JOAQUINA"), arrayWithSize(1)))
		.body("age.collect{it * 2}", hasItems(60, 50, 40))
		.body("id.max()", is(3))
		.body("salary.min()", is(1234.5678f))
		.body("salary.findAll{it != null}.sum()", is(closeTo(3734.5678f, 0.001)))
		.body("salary.findAll{it != null}.sum()", allOf(greaterThan(3000d), lessThan(5000d)))
		
		;
		
		
	
	}

	@Test
	
	public void unirJsonPathComJava() {
		ArrayList<String> nomes = 
		given()
		.when()
				.get("https://restapi.wcaquino.me/users/") 
		.then()
		.statusCode(200)
		.extract().path("name.findAll{it.startsWith('Maria')}")
		;
		
		Assert.assertEquals(1, nomes.size());
		Assert.assertTrue(nomes.get(0).equalsIgnoreCase("maria JoaquiNa"));
		Assert.assertEquals(nomes.get(0).toUpperCase(), "maria joaquina".toUpperCase());
		
		;
	}
	
	
}

