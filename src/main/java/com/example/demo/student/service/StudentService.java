package com.example.demo.student.service;

import com.example.demo.student.repository.StudentRepository;
import com.example.demo.student.student.Student;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;
import java.util.Optional;


@Service
@Slf4j
public class StudentService {

    private final StudentRepository studentRepository;

    @Autowired
    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public List<Student> getStudents(){
        log.info("Returning getStudents");
        return studentRepository.findAll();
    }

    public Optional<Student> getStudentById(Long studentId) {
           Optional<Student> existsById = studentRepository.findById(studentId);
           if(existsById.isEmpty()) {
               throw new IllegalStateException("Student with id " + studentId + " does not exists!");
           }
        log.info("Returning getStudentById");
           return studentRepository.findById(studentId);
    }


    public Student addNewStudent(Student student) {
        Optional<Student> StudentOptional = studentRepository
                .findStudentByEmail(student.getEmail());
        if(StudentOptional.isPresent()){
            throw new IllegalStateException("Email"+student.getEmail()+" Taken");
        }

        log.info("saving addNewStudent");
        studentRepository.save(student);
        log.info("Returning addNewStudent");
        return student;
    }

    public Long deleteStudent(Long studentId) {
        Optional<Student> StudentOptional = studentRepository
                .findById(studentId);
        boolean exists = studentRepository.existsById(studentId);
        if(!exists){
            throw new IllegalStateException(
                    "Student with Id" + studentId + " does not exists");
        }
        studentRepository.deleteById(studentId);
        log.info("Returning deleteStudent");
        return(studentId);
    }

    @Transactional
    public List<Student> updateStudent(Long studentId, String name, String email) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new IllegalStateException("Student with id"+studentId+" does not exists"));

        if (name != null &&
            name.length() > 0 &&
                !Objects.equals(student.getName(), name)){
            log.info("updating name");
            student.setName(name);
        }

        if (email != null &&
            email.length() > 0 ){
            Optional<Student> studentOptional = studentRepository
                    .findStudentByEmail(email);
            if(studentOptional.isPresent()){
                throw new IllegalStateException("email taken");
            }
            log.info("updating email");
            student.setEmail(email);
        }
        log.info("Returning updateStudent");
        return List.of(student);
    }
}
