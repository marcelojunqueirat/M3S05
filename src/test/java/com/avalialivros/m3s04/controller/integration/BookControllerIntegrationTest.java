package com.avalialivros.m3s04.controller.integration;

import com.avalialivros.m3s04.configuration.TestContainersDatabaseConfiguration;
import com.avalialivros.m3s04.model.transport.BookDTO;
import com.avalialivros.m3s04.model.transport.BookRatedGuidDTO;
import com.avalialivros.m3s04.model.transport.RatingDTO;
import com.avalialivros.m3s04.model.transport.operations.CreateBookDTO;
import com.avalialivros.m3s04.model.transport.operations.CreateRatingDTO;
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
public class BookControllerIntegrationTest extends TestContainersDatabaseConfiguration {
    private static RequestSpecification specification;
    private static ObjectMapper objectMapper;

    @BeforeAll
    static void setup() {
        objectMapper = new ObjectMapper()
                .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        specification = new RequestSpecBuilder()
                .setBasePath("/book")
                .setPort(8888)
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();
    }

    @Test
    void createBookShouldReturnSuccess() throws JsonProcessingException {
        CreateBookDTO mockBook = new CreateBookDTO("Clean Code", 2008);
        String responseAsJson = RestAssured.given()
                .spec(specification)
                .contentType("application/json")
                .auth().basic("marcelo@example.com", "UmaSenhaForte")
                .body(mockBook)
                .when().post().then().statusCode(201).extract().body().asString();

        BookDTO bookDTO = objectMapper.readValue(responseAsJson, BookDTO.class);

        Assertions.assertNotNull(bookDTO);
        Assertions.assertNotNull(bookDTO.guid());
        Assertions.assertNotNull(bookDTO.title());
        Assertions.assertNotNull(bookDTO.yearOfPublication());
        Assertions.assertNotNull(bookDTO.createdBy());
        Assertions.assertEquals(mockBook.title(), bookDTO.title());
        Assertions.assertEquals(mockBook.yearOfPublication(), bookDTO.yearOfPublication());
    }

    @Test
    void createRatingBookShouldReturnSuccess() throws JsonProcessingException {
        CreateRatingDTO mockRatingDTO = new CreateRatingDTO(5);
        String responseAsJson = RestAssured.given()
                .spec(specification)
                .basePath("/book/{id}/rate")
                .pathParam("id", "700aa2ed-da4c-4438-8263-b89e73432359")
                .contentType("application/json")
                .auth().basic("teste@example.com", "UmaSenhaForte")
                .body(mockRatingDTO)
                .when().post().then().statusCode(200).extract().body().asString();

        RatingDTO ratingDTO = objectMapper.readValue(responseAsJson, RatingDTO.class);

        Assertions.assertNotNull(ratingDTO);
        Assertions.assertNotNull(ratingDTO.guid());
        Assertions.assertNotNull(ratingDTO.grade());
        Assertions.assertNotNull(ratingDTO.ratedBy());
        Assertions.assertEquals(mockRatingDTO.grade(), ratingDTO.grade());
    }

    @Test
    void getBookByGuidShouldReturnSucess() throws JsonProcessingException {
        String responseAsString = RestAssured.given()
                .spec(specification)
                .pathParam("id", "700aa2ed-da4c-4438-8263-b89e73432359")
                .auth().basic("marcelo@example.com", "UmaSenhaForte")
                .when().get("{id}").then().statusCode(200).extract().body().asString();

        BookRatedGuidDTO bookRatedGuidDTO = objectMapper.readValue(responseAsString, BookRatedGuidDTO.class);

        Assertions.assertNotNull(bookRatedGuidDTO);
        Assertions.assertNotNull(bookRatedGuidDTO.guid());
        Assertions.assertNotNull(bookRatedGuidDTO.rating());
        Assertions.assertNotNull(bookRatedGuidDTO.title());
        Assertions.assertNotNull(bookRatedGuidDTO.yearOfPublication());
        Assertions.assertNotNull(bookRatedGuidDTO.averageGrade());
    }
}
