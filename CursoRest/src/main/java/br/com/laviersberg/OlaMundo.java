package br.com.laviersberg;

import io.restassured.RestAssured;
import io.restassured.http.Method;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;

public class OlaMundo {
	public static void main(String[] args) {
		//Estrutura básica para testar o response 
		Response response = RestAssured.request(Method.GET,"https://restapi.wcaquino.me/ola");
		System.out.println(response.getBody().asString().equals("Ola Mundo!")); //irá me trazer o resultado se é verdadeiro ou falso quando coloco qual informação deve aparecer
		//System.out.println(response.getBody().asString());
		System.out.println(response.statusCode() == 200); // No status code irá me trazer True ou False de acordo com status informado
		
		//recurso de validacao 
		ValidatableResponse validacao = response.then(); 
		validacao.statusCode(200);
	}
}
