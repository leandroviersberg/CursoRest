package br.com.laviersberg;

import static io.restassured.RestAssured.*;
import static org.junit.Assert.assertThat;
import static org.hamcrest.Matchers.*;
import java.util.Arrays;
import java.util.List;

import org.hamcrest.Matchers;

import org.junit.Assert;
import org.junit.Test;

import io.restassured.http.Method;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;

public class OlaMundoTest {
	
	@Test
	public void testeOlaMundo() {
		
				Response response = request(Method.GET,"https://restapi.wcaquino.me/ola");
				Assert.assertTrue(response.getBody().asString().equals("Ola Mundo!")); 
				Assert.assertTrue(response.statusCode() == 200);
				Assert.assertTrue(" Status code deveria ser 200", response.statusCode() == 200);
				Assert.assertEquals(200, response.statusCode());
				//System.out.println(response.getBody().asString().equals("Ola Mundo!")); 
				//System.out.println(response.getBody().asString());
				//System.out.println(response.statusCode() == 201); 
				
				//throw new RuntimeException();
				
				//recurso de validacao 
				ValidatableResponse validacao = response.then(); 
				validacao.statusCode(200);
			}
			
			@Test
			public void devoConhecerOutrasFormas() {
				Response response = request(Method.GET,"https://restapi.wcaquino.me/ola");
				ValidatableResponse validacao = response.then(); 
				validacao.statusCode(200);
				
				get("https://restapi.wcaquino.me/ola").then().statusCode(200);
				
				//Modo Fluente
				
				given() //pre condições
				.when() //ação
					.get("https://restapi.wcaquino.me/ola")
				.then() //assertivas
					.statusCode(200);
			}
			
			@Test
			public void ConhecendoMatchersHamcrest() {
				
				//Exemplos das validações Hamcrest igualdade
				
				Assert.assertThat("Maria", Matchers.is("Maria"));
				Assert.assertThat(128, Matchers.is(128)); //Verifica a igualdade apenas do numero 
				Assert.assertThat(128, Matchers.isA(Integer.class)); //Tipagem, neste caso se o número é inteiro
				Assert.assertThat(128d, Matchers.isA(Double.class)); //Faz a checagem se é um Double
				Assert.assertThat(128d, Matchers.greaterThan(120d)); // 128 é maior que 120?
				Assert.assertThat(128d, Matchers.lessThan(130d)); //128 é menor que 130?
				
				//Exemplo de lista, será validado a quantidade de elementos
				
				List<Integer> impares = Arrays.asList(1,3,5,7,9);
				assertThat(impares, hasSize(5));
				assertThat(impares, contains(1,3,5,7,9));
				assertThat(impares, containsInAnyOrder(1,3,5,9,7)); // Não importa a ordem dos números, o resultado será de OK.
				
				assertThat("Maria", is(not("João")));
				assertThat("Maria", not("João"));
				assertThat("Joaquina", anyOf(is("Maria"), is("Joaquina")));
				assertThat("Joaquina", allOf(startsWith("Joa"), endsWith("ina"), containsString("qui")));
	}

			@Test
			public void DevoValidarBody() {
				
				given() 
				.when() 
					.get("https://restapi.wcaquino.me/ola")
				.then() //
					.statusCode(200)
					.body(is("Ola Mundo!"))
					.body(containsString("Mundo"))
					.body(is(not(nullValue())));		
					
	}



}		


