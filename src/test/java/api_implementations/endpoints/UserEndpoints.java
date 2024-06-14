package api_implementations.endpoints;

import api_implementations.user_pojo.User;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import java.util.List;

import static api_implementations.endpoints.UriPaths.*;
import static io.restassured.RestAssured.given;

public class UserEndpoints {

    public static Response createUser(User userPojo) {

        Response response = given()
                .body(userPojo)
                .when()
                .post(new UriBuilder().addPath(USER).buildPath());

        return response;
    }

    public static Response createListOfUsers(List<User> userPojo) {

        Response response = given()
                .body(userPojo)
                .when()
                .post(new UriBuilder().addPaths(USER, CREATE_WITH_LIST).buildPath());

        return response;
    }

    public static Response loginUser(String username, String password) {

        Response response = given()
                .queryParam("username", username)
                .queryParam("password", password)
                .when()
                .get(new UriBuilder().addPaths(USER, LOGIN).buildPath());

        return response;
    }

    public static Response logoutUser() {
        Response response = given()
                .when()
                .get(new UriBuilder().addPaths(USER, LOGOUT).buildPath());

        return response;
    }

    public static Response readUser(String username) {

        Response response = given()
                .pathParam("username", username)
                .when()
                .get(new UriBuilder().addPath(USER).addPathParam("username").buildPath());

        return response;
    }

    public static Response updateUser(String username, User userPojo) {

        Response response = given()
                .pathParam("username", username)
                .body(userPojo)
                .when()
                .put(new UriBuilder().addPath(USER).addPathParam("username").buildPath());

        return response;
    }

    public static Response deleteUser(String username) {

        Response response = given()
                .pathParam("username", username)
                .when()
                .delete(new UriBuilder().addPath(USER).addPathParam("username").buildPath());

        return response;
    }
}
