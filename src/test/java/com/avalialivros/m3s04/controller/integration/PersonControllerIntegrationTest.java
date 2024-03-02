package com.avalialivros.m3s04.controller.integration;

import com.avalialivros.m3s04.configuration.TestContainersDatabaseConfiguration;
import com.avalialivros.m3s04.model.transport.PersonDTO;
import com.avalialivros.m3s04.model.transport.operations.CreatePersonDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class PersonControllerIntegrationTest extends TestContainersDatabaseConfiguration {
    private static RequestSpecification specification;
    private static ObjectMapper objectMapper;

    @BeforeAll
    static void setup() {
        objectMapper = new ObjectMapper()
                .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        specification = new RequestSpecBuilder()
                .setBasePath("/person")
                .setPort(8888)
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();
    }

    @Test
    void createPersonShouldReturnSuccess() throws JsonProcessingException {
        CreatePersonDTO mockPerson = new CreatePersonDTO("Marcelo Teste", "marcelo.teste@gmail.com", "UmaSenhaFraca");
        String responseAsJson = RestAssured.given()
                .spec(specification)
                .contentType("application/json")
                .auth().basic("marcelo@example.com", "UmaSenhaForte")
                .body(mockPerson)
                .when().post().then().statusCode(201).extract().body().asString();

        PersonDTO personDTO = objectMapper.readValue(responseAsJson, PersonDTO.class);

        Assertions.assertNotNull(personDTO);
        Assertions.assertNotNull(personDTO.guid());
        Assertions.assertNotNull(personDTO.name());
        Assertions.assertNotNull(personDTO.email());
        Assertions.assertEquals(mockPerson.name(), personDTO.name());
        Assertions.assertEquals(mockPerson.email(), personDTO.email());
    }
}
