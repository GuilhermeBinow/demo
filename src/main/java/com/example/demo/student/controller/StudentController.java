package com.example.demo.student.controller;

import com.example.demo.student.student.Student;
import com.example.demo.student.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "api/v1/student")
public class StudentController {

    private final StudentService studentService;

    @Autowired
    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping(path = "{studentId}")
    public Optional<Student> getStudentById(
            @PathVariable("studentId") Long studentId) {
        return studentService.getStudentById(studentId);
    }

    @GetMapping
    public List<Student> getStudents(){
        return studentService.getStudents();

    }

    @PostMapping
    public String registerNewStudent(@RequestBody Student student){

        studentService.addNewStudent(student);
        return ("Student " + student.getName() + " added!");
    }

    @DeleteMapping(path = "{studentId}")
    public String deleteStudent(
            @PathVariable("studentId") Long studentId) {

                studentService.deleteStudent(studentId);

                return("Student with id " + studentId + " deleted!");
    }

    @PutMapping(path= "{studentId}")
    public String updateStudent(
            @PathVariable("studentId") Long studentId,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String email) {
        studentService.updateStudent(studentId, name, email);


        return("Student with id " + studentId + " updated!");
    }

}

