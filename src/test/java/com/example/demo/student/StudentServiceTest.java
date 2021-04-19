package com.example.demo.student;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.Month;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

    @Mock
    private StudentRepository studentRepository;
    private StudentService underTest;

    @BeforeEach
    void setUp() {
        underTest= new StudentService(studentRepository);
    }


    @Test
    void canGetStudents() {
        //when
        underTest.getStudents();
        //then
        verify(studentRepository).findAll();
    }

    @Test
    void CanAddNewStudent() {
        //given
        Student student= new Student(
                "Guilherme B",
                "Guilherme@gmail.com",
                LocalDate.of(1999, Month.JUNE,11)

        );

        //when
        underTest.addNewStudent(student);

        //then
        ArgumentCaptor<Student> studentArgumentCaptor=
                ArgumentCaptor.forClass(Student.class);

        verify(studentRepository)
                .save(studentArgumentCaptor.capture());

        Student capturedStudent = studentArgumentCaptor.getValue();
        assertThat(capturedStudent).isEqualTo(student);
    }


    @Test
    void ErrorWhenEmailIsTaken() {
        //given
        Student student= new Student(
                "Guilherme B",
                "Guilherme@gmail.com",
                LocalDate.of(1999, Month.JUNE,11)

        );

        given(studentRepository.findStudentByEmail(student.getEmail()))
                .willReturn(Optional.of(student));

        //when
        //then
        assertThatThrownBy(()->underTest.addNewStudent(student))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Email"+student.getEmail()+" Taken");

        verify(studentRepository, never()).save(any());

    }


    @Test
    void CanDeleteStudent() {
            // given
            Student student = new Student(
                    "Rodrigo",
                    "rodrigovalori@hotmail.com",
                    LocalDate.of(1999, Month.AUGUST, 6)
            );
            student.setId(1l);
            studentRepository.save(student);


            given(studentRepository.existsById(student.getId())).willReturn(true);

            // when
            underTest.deleteStudent(student.getId());

            // then
            verify(studentRepository).deleteById(student.getId());




    }

    @Test
    @Disabled
    void updateStudent() {
    }
}