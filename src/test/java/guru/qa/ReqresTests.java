package guru.qa;

import guru.qa.Lambok.*;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static guru.qa.models.Specs.*;
import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ReqresTests {
    @BeforeAll
    static void setUp() {
        RestAssured.baseURI = "https://reqres.in/";
    }



    @Test
    void singleUserTest() {
        given()
                .when()
                .get("api/users/2")
                .then()
                .statusCode(200)
                .body("data.id", is(2))
                .body("data.email", is("janet.weaver@reqres.in"))
                .body("data.first_name", is("Janet"))
                .body("data.last_name", is("Weaver"))
                .body("support.text", is("To keep ReqRes free, contributions towards server costs are appreciated!"));
    }

    @Test
    void singleUserWithModelTest() {
        given()
                .when()
                .get("api/users/2")
                .then()
                .spec(responseSpec)
                .body("data.id", is(2))
                .body("data.email", is("janet.weaver@reqres.in"))
                .body("data.first_name", is("Janet"))
                .body("data.last_name", is("Weaver"))
                .body("support.text", is("To keep ReqRes free, contributions towards server costs are appreciated!"));
    }

    @Test
    void singleUserWithModelAndLambokTest() {
        LombokSingleUserData singleUserData =
        given()
                .when()
                .get("api/users/2")
                .then()
                .spec(responseSpec)
                .extract().as(LombokSingleUserData.class);
        //janet.weaver@reqres.in

        assertEquals(2, singleUserData.getSingleUser().getId());
        assertEquals("janet.weaver@reqres.in", singleUserData.getSingleUser().getEmail());
        assertEquals("Janet", singleUserData.getSingleUser().getFirstName());
        assertEquals("Weaver", singleUserData.getSingleUser().getLastName());
    }



    @Test
    void listResourceWithModelsTest() {
        given()
                .when()
                .get("api/unknown")
                .then()
                .spec(responseSpec)
                .body("page", is(1))
                .body("total", is(12))
                .body("data[0].id", is(1))
                .body("data[1].name", is("fuchsia rose"))
                .body("data[2].year", is(2002));
    }

    @Test
    void listResourceTest2() {
        given()
                .when()
                .get("api/unknown")
                .then()
                .body("data.name.flatten()",
                        hasItems(
                                "cerulean",
                                "fuchsia rose",
                                "true red",
                                "aqua sky",
                                "tigerlily",
                                "blue turquoise")
                )
                .body("data.findAll{it.name =~/.*?ulean/}.name.flatten()",
                        hasItem("cerulean"));
        }



    @Test
    void singleResourceTest() {

        String data = "{ \"name\": \"morpheus\", \"job\": \"leader\" }";

        given()
                .contentType(JSON)
                .body(data)
                .when()
                .post("api/users")
                .then()
                .statusCode(201)
                .body("name", is("morpheus"));
    }

    @Test
    void singleResourceWithModelsTest() {

        String data = "{ \"name\": \"morpheus\", \"job\": \"leader\" }";

        given()
                .spec(request)
                .body(data)
                .when()
                .post("api/users")
                .then()
                .statusCode(201)
                .body("name", is("morpheus"));
    }

    @Test
    void singleResourceWithModelsAndLambokTest() {

        LambokSingleResource singleResource = new LambokSingleResource("morpheus", "leader");
        LambokSingleResourceResponse singleResourceResponse =
        given()
                .spec(request)
                .body(singleResource)
                .when()
                .post("api/users")
                .then()
                .statusCode(201)
                .extract().as(LambokSingleResourceResponse.class);

        assertEquals(singleResource.getName(),singleResourceResponse.getName());
        assertEquals(singleResource.getJob(),singleResourceResponse.getJob());
    }




    @Test
    void updateTest() {

        String data = "{ \"name\": \"morpheus\", \"job\": \"zion resident\" }";

        given()
                .contentType(JSON)
                .body(data)
                .when()
                .put("api/users?page=2")
                .then()
                .statusCode(200)
                .body("name", is("morpheus"))
                .body("job", is("zion resident"))
                .body("updatedAt", notNullValue());
    }

    @Test
    void updateWithModelsTest() {

        String data = "{ \"name\": \"morpheus\", \"job\": \"zion resident\" }";

        given()
                .spec(request)
                .body(data)
                .when()
                .put("api/users?page=2")
                .then()
                .spec(responseSpec)
                .body("name", is("morpheus"))
                .body("job", is("zion resident"))
                .body("updatedAt", notNullValue());
    }

    @Test
    void updateWithModelsAndLambokTest() {
        LombokUpdate data = new LombokUpdate("morpheus", "zion resident");

        //String data = "{ \"name\": \"morpheus\", \"job\": \"zion resident\" }";
        LambokUpdateResponse lombokUpdate =
        given()
                .spec(request)
                .body(data)
                .when()
                .put("api/users?page=2")
                .then()
                .spec(responseSpec)
                .extract().as(LambokUpdateResponse.class);

        assertEquals(data.getName(),lombokUpdate.getName());
        assertEquals(data.getJob(),lombokUpdate.getJob());
//                .body("name", is("morpheus"))
//                .body("job", is("zion resident"))
//                .body("updatedAt", notNullValue());
    }

}
