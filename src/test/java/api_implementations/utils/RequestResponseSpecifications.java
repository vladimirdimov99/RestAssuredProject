package api_implementations.utils;

import api_implementations.endpoints.UriPaths;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;

public class RequestResponseSpecifications {

    public void buildRequestSpecification() {
        RequestSpecBuilder builder = new RequestSpecBuilder()
                .setBaseUri(UriPaths.ROOT_DEFAULT)
                .setContentType(ContentType.JSON)
                .setAccept(ContentType.JSON);

        RestAssured.requestSpecification = builder.build();
    }
}
