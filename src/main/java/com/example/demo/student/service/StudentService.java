package com.example.demo.student.service;

import com.example.demo.student.repository.StudentRepository;
import com.example.demo.student.student.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;
import java.util.Optional;


@Service
public class StudentService {

    private final StudentRepository studentRepository;

    @Autowired
    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public List<Student> getStudents(){
        return studentRepository.findAll();
    }

    public Optional<Student> getStudentById(Long studentId) {
           Optional<Student> existsById = studentRepository.findById(studentId);
           if(existsById.isEmpty()) {
               throw new IllegalStateException("Student with id " + studentId + " does not exists!");
           }
           return studentRepository.findById(studentId);
    }


    public Student addNewStudent(Student student) {
        Optional<Student> StudentOptional = studentRepository
                .findStudentByEmail(student.getEmail());
        if(StudentOptional.isPresent()){
            throw new IllegalStateException("Email"+student.getEmail()+" Taken");
        }


        studentRepository.save(student);
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
        return(studentId);
    }

    @Transactional
    public List<Student> updateStudent(Long studentId, String name, String email) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new IllegalStateException("Student with id"+studentId+" does not exists"));

        if (name != null &&
            name.length() > 0 &&
                !Objects.equals(student.getName(), name)){
            student.setName(name);
        }

        if (email != null &&
            email.length() > 0 ){
            Optional<Student> studentOptional = studentRepository
                    .findStudentByEmail(email);
            if(studentOptional.isPresent()){
                throw new IllegalStateException("email taken");
            }

            student.setEmail(email);
        }

        return (null);
    }
}
