package com.example.demo.student.controller;

import com.example.demo.student.repository.StudentRepository;
import com.example.demo.student.service.StudentService;
import com.example.demo.student.student.Student;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import javax.annotation.PostConstruct;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;
import static java.time.Month.JUNE;
import static java.util.Calendar.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
class StudentControllerTest {

    @Mock
    private Student student;

    @MockBean
    StudentService studentService;

    private List<Student> studentList;

    @PostConstruct
    void setUp() throws Exception {
        student= new Student();
        student.setId(1L);
        student.setName("Bruce Wayne");
        student.setEmail("NotTheBatman@Batmail.com");
        student.setDob(LocalDate.of(1978, APRIL, 17));

        studentList = new ArrayList<>();
        studentList.add(new Student ("Peter Parker",
                "NotSpiderMan@gmail.com",
                LocalDate.of(2001, AUGUST, 10)));
        studentList.add(new Student ("Tony Stark",
                "ironMan@gmail.com",
                LocalDate.of(1970, MAY, 29)));
        baseURI = "http://localhost:" + port + "/";
    }



    @LocalServerPort private int port;

    @Test
    void getAllStudentsWhenUsernameAndPasswordAreIncorrectShouldReturnStatus401() {
        Mockito.when(studentService.getStudents()).thenReturn(studentList);

        Response response = given()
                .port(port)
                .auth()
                .basic("admin","colognotnow")
                .contentType(ContentType.JSON)
                .when()
                .get(baseURI + "api/v1/student")
                .then()
                .extract()
                .response();

        Assertions.assertEquals(401, response.statusCode());
    }

    @Test
    void whenPathIsWrongGiveError404() {
        Response response = given()
                .port(port)
                .auth()
                .basic("admin","colognow")
                .contentType(ContentType.JSON)
                .when()
                .get(baseURI + "api/v1/stud")
                .then()
                .extract()
                .response();

        Assertions.assertEquals(404, response.statusCode());
    }


    @Test
    void getStudentsShouldReturnListOfStudents() {
        Mockito.when(studentService.getStudents()).thenReturn(studentList);
        Response response = given()
            .port(port)
            .auth()
            .basic("admin","colognow")
            .contentType(ContentType.JSON)
            .when()
            .get(baseURI + "api/v1/student")
            .then()
            .extract()
            .response();


        Assertions.assertEquals(200, response.statusCode());

    }

    @Test
    void getStudentWithIDShouldReturnStudentByID() {
        Mockito.when(studentService.getStudentById(student.getId())).thenReturn(Optional.of(student));
        Response response = given()
                .port(port)
                .auth()
                .basic("admin","colognow")
                .contentType(ContentType.JSON)
                .when()
                .get(baseURI + "api/v1/student/"+ student.getId())
                .then()
                .extract()
                .response();


        Assertions.assertEquals(200, response.statusCode());
    }

    @Test
    void GetStudentByIDShouldReturn401WhenPasswordIsIncorrect() {
        Mockito.when(studentService.getStudentById(student.getId())).thenReturn(Optional.of(student));
        Response response = given()
                .port(port)
                .auth()
                .basic("admin","colognotnow")
                .contentType(ContentType.JSON)
                .when()
                .get(baseURI + "api/v1/student/"+ student.getId())
                .then()
                .extract()
                .response();


        Assertions.assertEquals(401, response.statusCode());
    }

    @Test
    void PostNewStudent() throws JSONException {

            JSONObject requestParams = new JSONObject();

            requestParams.put("id", student.getId());
            requestParams.put("name", student.getName());
            requestParams.put("email", student.getEmail());
            requestParams.put("dob", student.getDob());

            Mockito.when(studentService.addNewStudent(student)).thenReturn(student);

            Response response = given()
                    .port(port)
                    .auth()
                    .basic("admin","colognow")
                    .header("Content-Type","application/json")
                    .body(requestParams.toString())
                    .when()
                    .post("api/v1/student/")
                    .then()
                    .extract()
                    .response();

            Assertions.assertEquals(200, response.statusCode());
            Assertions.assertEquals("Student " + student.getName() + " added!", response.getBody().asString());
    }


    @Test
    void PostNewStudentError401() throws JSONException {

        JSONObject requestParams = new JSONObject();

        requestParams.put("id", student.getId());
        requestParams.put("name", student.getName());
        requestParams.put("email", student.getEmail());
        requestParams.put("dob", student.getDob());

        Mockito.when(studentService.addNewStudent(student)).thenReturn(student);

        Response response = given()
                .port(port)
                .auth()
                .basic("admin","colognotnow")
                .header("Content-Type","application/json")
                .body(requestParams.toString())
                .when()
                .post("api/v1/student/")
                .then()
                .extract()
                .response();

        Assertions.assertEquals(401, response.statusCode());
    }



    @Test
    void deleteStudent() {
        Mockito.when(studentService.deleteStudent(student.getId())).thenReturn(student.getId());
        Response response = given()
                .port(port)
                .auth()
                .basic("admin","colognow")
                .header("Content-Type","application/json")
                .when()
                .delete("api/v1/student/" + student.getId())
                .then()
                .extract()
                .response();

        Assertions.assertEquals(200, response.statusCode());
        Assertions.assertEquals("Student with id " + student.getId() + " deleted!", response.getBody().asString());
    }

    @Test
    void DeleteStudentError401() {
        Mockito.when(studentService.deleteStudent(student.getId())).thenReturn(student.getId());
        Response response = given()
                .port(port)
                .auth()
                .basic("admin","colognotnow")
                .header("Content-Type","application/json")
                .when()
                .delete("api/v1/student/" + student.getId())
                .then()
                .extract()
                .response();

        Assertions.assertEquals(401, response.statusCode());
    }

    @Test
    void updateStudent() {
        Mockito.when(studentService.updateStudent(student.getId(), student.getName(), student.getEmail())).thenReturn(studentList);

        Response response = given()
                .port(port)
                .auth()
                .basic("admin","colognow")
                .header("Content-Type","application/json")
                .when()
                .put("api/v1/student/" + student.getId() + "?name=" + student.getName() + "?email=" + student.getEmail())
                .then()
                .extract()
                .response();

        Assertions.assertEquals(200, response.statusCode());
        Assertions.assertEquals("Student with id " + student.getId() + " updated!", response.getBody().asString());
    }

    @Test
    void updateStudentError401() {
        Mockito.when(studentService.updateStudent(student.getId(), student.getName(), student.getEmail())).thenReturn(studentList);

        Response response = given()
                .port(port)
                .auth()
                .basic("admin","colognotnow")
                .header("Content-Type","application/json")
                .when()
                .put("api/v1/student/" + student.getId() + "?name=" + student.getName() + "?email=" + student.getEmail())
                .then()
                .extract()
                .response();

        Assertions.assertEquals(401, response.statusCode());
    }


}
