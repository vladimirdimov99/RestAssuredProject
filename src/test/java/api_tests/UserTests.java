package api_tests;

import api_implementations.endpoints.UserEndpoints;
import api_implementations.user_pojo.User;
import api_implementations.utils.RequestResponseSpecifications;
import com.github.javafaker.Faker;
import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.*;

import java.util.ArrayList;
import java.util.List;

import static org.apache.http.HttpStatus.*;
import static org.testng.Assert.*;

public class UserTests {

    Faker fakeData;
    User userPojo;
    int numOfUsers = 10;
    List<User> userList = new ArrayList<>();
    private final Logger log = LogManager.getLogger(UserTests.class);

    public final String testData = """
            {
              "id": 0,
              "username": "string",
              "firstName": "string",
              "lastName": "string",
              "email": "string",
              "password": "string",
              "phone": "string",
              "userStatus": 0
            }
            """;

    public final String testDataArray = """
            [
                {
                  "id": 0,
                  "username": "string",
                  "firstName": "string",
                  "lastName": "string",
                  "email": "string",
                  "password": "string",
                  "phone": "string",
                  "userStatus": 0
                }
            ]
            """;

    @BeforeMethod
    public void setupData() {
        fakeData = new Faker();
        userPojo = new User();
        new RequestResponseSpecifications().buildRequestSpecification();

        userPojo.setId(fakeData.idNumber().hashCode());
        userPojo.setUsername(fakeData.name().username());
        userPojo.setFirstname(fakeData.name().firstName());
        userPojo.setLastname(fakeData.name().lastName());
        userPojo.setEmail(fakeData.internet().safeEmailAddress());
        userPojo.setPassword(fakeData.internet().password(5, 10));
        userPojo.setPhone(fakeData.phoneNumber().cellPhone());
    }

    @Test(description = "Create a User")
    public void createUser() {
        System.out.println("Create User:");

        Response response = UserEndpoints.createUser(userPojo);
        response.then().log().body();

        assertEquals(response.statusCode(), SC_OK);
    }

    @Test(description = "Get a user by username")
    public void readUser() {
        System.out.println("Read User:");

        //Create a User
        UserEndpoints.createUser(userPojo);

        //Read the created user
        Response response = UserEndpoints.readUser(userPojo.getUsername());
        response.then().log().body();

        assertEquals(response.statusCode(), SC_OK, "Response code is not 200!!!");
    }

    @Test(description = "Update a specific user")
    public void updateUser() {
        System.out.println("Update User and then Read it again:");

        //Create a User
        UserEndpoints.createUser(userPojo);

        //Login a User
        UserEndpoints.loginUser(userPojo.getUsername(), userPojo.getPassword());

        String previousPhoneNum = userPojo.getPhone();
        //Update the specific user's phone number
        userPojo.setPhone(fakeData.phoneNumber().cellPhone());
        Response response = UserEndpoints.updateUser(userPojo.getUsername(), userPojo);
        response.then().log().body();

        assertEquals(response.statusCode(), SC_OK, "Response code is not 200!!!");
        assertNotEquals(previousPhoneNum, userPojo.getPhone(), "Previously entered Phone Number is still the same!!!");

        // Read once again the user
        Response responseAfterUpdate = UserEndpoints.readUser(userPojo.getUsername());
        responseAfterUpdate.then().log().body();
    }

    @Test(description = "Create a List of Users")
    public void createListOfUsers() {
        System.out.println("Create list of users:");
        userPojo = new User();

        // Add a specific number of Users to a List
        for (int i = 0; i < numOfUsers; i++) {
            setupData();
            userList.add(userPojo);
        }

        Response response = UserEndpoints.createListOfUsers(userList);
        response.then().log().body().extract().response();

        assertEquals(response.statusCode(), SC_OK);
        assertEquals(response.jsonPath().get("message"), "ok", "The expected message is not ok!!!");
    }

    @Test(description = "Login a user to the website")
    public void loginUser() {
        System.out.println("Login User:");

        //Create a User
        UserEndpoints.createUser(userPojo);

        // Login the User
        Response response = UserEndpoints.loginUser(userPojo.getUsername(), userPojo.getPassword());
        response.then().log().body();

        // Check the status code and check for an "ok" message
        String message = response.jsonPath().get("message");
        assertEquals(response.statusCode(), SC_OK, "The expected response code is not 200 for the Login User test!!!");
        assertTrue(message.contains("logged in user session"), "The message doesn't contain logged in user session!!!");
    }

    @Test(description = "Logout a user from his account")
    public void logoutUser() {
        System.out.println("Logout User:");

        //Create a User
        UserEndpoints.createUser(userPojo);

        //Login a User
        UserEndpoints.loginUser(userPojo.getUsername(), userPojo.getPassword());

        //Logout from the account
        Response response = UserEndpoints.logoutUser();
        response.then().log().body().extract().response();

        assertEquals(response.statusCode(), SC_OK, "The expected response code is not 200 for the Logout User test!!!");
        assertEquals(response.jsonPath().get("message"), "ok", "The expected message is not ok!!!");
    }

    @Test(description = "Delete a user by providing his username")
    public void deleteUser() {
        System.out.println("Delete User:");

        //Create a User
        UserEndpoints.createUser(userPojo);

        //Login a User
        UserEndpoints.loginUser(userPojo.getUsername(), userPojo.getPassword());

        //Delete the User
        Response response = UserEndpoints.deleteUser(userPojo.getUsername());
        response.then().log().body().extract().response();

        String message = response.jsonPath().get("message");

        assertEquals(response.statusCode(), SC_OK, "The expected response code is not 200 for the Delete User test!!!");
        assertEquals(message, userPojo.getUsername(), "The message doesn't match user's username!!!");
    }
}
