package api_tests;

import api_implementations.endpoints.UserEndpoints;
import api_implementations.user_pojo.User;
import api_implementations.utils.RequestResponseSpecifications;
import api_implementations.utils.extentReport.ExtentReportManagerTestNG;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.github.javafaker.Faker;
import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.ITestListener;
import org.testng.annotations.*;

import java.util.ArrayList;
import java.util.List;

import static org.apache.http.HttpStatus.*;
import static org.testng.Assert.*;

public class UserTests implements ITestListener {

    Faker fakeData;

    User userPojo;

    int numOfUsers = 10;

    List<User> userList = new ArrayList<>();

    private final Logger log = LogManager.getLogger(UserTests.class);

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
        log.debug("Starting a test...");
    }

    @Test(description = "Create a User")
    public void createUser() {
        log.info("********** Creating User **********");

        Response response = UserEndpoints.createUser(userPojo);
        response.then().log().body();

        assertEquals(response.statusCode(), SC_OK);

        log.info("********** User successfully created a User!!! **********");
    }

    @Test(description = "Get a user by username")
    public void readUser() {
        log.info("********** Creating User **********");
        UserEndpoints.createUser(userPojo);
        log.info("********** User is created **********");

        log.info("********** Reading the User **********");
        Response response = UserEndpoints.readUser(userPojo.getUsername());
        response.then().log().body();

        assertEquals(response.statusCode(), SC_OK, "Response code is not 200!!!");

        log.info("********** Information about the user with the certain username is displayed!!! **********");
    }

    @Test(description = "Update a specific user")
    public void updateUser() {
        log.info("********** Creating User **********");
        UserEndpoints.createUser(userPojo);

        log.info("********** Logging the User **********");
        UserEndpoints.loginUser(userPojo.getUsername(), userPojo.getPassword());

        String previousPhoneNum = userPojo.getPhone();
        log.info("********** Updating a specific User **********");
        userPojo.setPhone(fakeData.phoneNumber().cellPhone());
        Response response = UserEndpoints.updateUser(userPojo.getUsername(), userPojo);
        response.then().log().body();

        assertEquals(response.statusCode(), SC_OK, "Response code is not 200!!!");
        assertNotEquals(previousPhoneNum, userPojo.getPhone(), "Previously entered Phone Number is still the same!!!");
        log.info("********** User has been updated successfully!!! **********");

        log.info("********** Reading the User **********");
        Response responseAfterUpdate = UserEndpoints.readUser(userPojo.getUsername());
        responseAfterUpdate.then().log().body();
    }

    @Test(description = "Create a List of Users")
    public void createListOfUsers() {
        log.info("********** Creating a List of Users **********");
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

        log.info("********** Successfully created a list of " + numOfUsers + " Users **********");
    }

    @Test(description = "Login a user to the website")
    public void loginUser() {
        log.info("********** Creating User **********");
        UserEndpoints.createUser(userPojo);

        log.info("********** Login User **********");
        Response response = UserEndpoints.loginUser(userPojo.getUsername(), userPojo.getPassword());
        response.then().log().body();

        String message = response.jsonPath().get("message");
        assertEquals(response.statusCode(), SC_OK, "The expected response code is not 200 for the Login User test!!!");
        assertTrue(message.contains("logged in user session"), "The message doesn't contain logged in user session!!!");
        log.info("********** User successfully logged in!!! **********");
    }

    @Test(description = "Logout a user from his account")
    public void logoutUser() {
        log.info("********** Creating User **********");
        UserEndpoints.createUser(userPojo);

        log.info("********** Logging User **********");

        UserEndpoints.loginUser(userPojo.getUsername(), userPojo.getPassword());

        log.info("********** Logging out User **********");
        Response response = UserEndpoints.logoutUser();
        response.then().log().body().extract().response();

        // Print logs in Extent Report
        //test.log(Status.INFO, response.prettyPrint());
        assertEquals(response.statusCode(), SC_OK, "The expected response code is not 200 for the Logout User test!!!");
        assertEquals(response.jsonPath().get("message"), "ok", "The expected message is not ok!!!");
        log.info("********** User successfully logged out!!! **********");
    }

    @Test(description = "Delete a user by providing his username")
    public void deleteUser() {
        log.info("********** Creating User **********");
        UserEndpoints.createUser(userPojo);

        log.info("********** Login User **********");
        UserEndpoints.loginUser(userPojo.getUsername(), userPojo.getPassword());

        log.info("********** Delete User **********");
        Response response = UserEndpoints.deleteUser(userPojo.getUsername());
        response.then().log().body().extract().response();

        // Print logs in Extent Report
        //        getTest().log(Status.INFO, response.prettyPrint());

        String message = response.jsonPath().get("message");
        assertEquals(response.statusCode(), SC_OK, "The expected response code is not 200 for the Delete User test!!!");
        assertEquals(message, userPojo.getUsername(), "The message doesn't match user's username!!!");

        log.info("********** User deleted successfully!!! **********");
    }
}
