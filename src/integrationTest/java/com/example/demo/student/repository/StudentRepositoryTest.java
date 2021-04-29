package com.example.demo.student.repository;

import com.example.demo.student.repository.StudentRepository;
import com.example.demo.student.student.Student;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.time.Month;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
class StudentRepositoryTest {

    @Autowired
    StudentRepository underTest;

    @BeforeEach
    void setUp() {

    }

    @AfterEach
    void tearDown() {
        underTest.deleteAll();
    }

    @Test
    void ItShouldCheckIfStudentExistsByEmail() {
        //given
        Student student= new Student(
          "Guilherme B",
                "Guilherme@gmail.com",
                LocalDate.of(1999, Month.JUNE,11)

        );
        underTest.save(student);
        //when
        Optional<Student> studentByEmail = underTest.findStudentByEmail(student.getEmail());
        //then

        assertTrue(studentByEmail.isPresent());
        assertThat(studentByEmail.get()).isEqualTo(student);
    }

    @Test
    void ItShouldCheckIfStudentDoesNotExistsByEmail() {
        //given
        Student student= new Student(
                "Guilherme B",
                "Guilherme@gmail.com",
                LocalDate.of(1999, Month.JUNE,11)

        );
        //when
        Optional<Student> studentByEmail = underTest.findStudentByEmail(student.getEmail());
        //then

        //assertTrue(studentByEmail.isEmpty());
        assertThat(studentByEmail).isEmpty();
    }

}

