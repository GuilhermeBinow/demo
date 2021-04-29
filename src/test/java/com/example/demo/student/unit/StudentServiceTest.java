package com.example.demo.student.unit;

import com.example.demo.student.repository.StudentRepository;
import com.example.demo.student.service.StudentService;
import com.example.demo.student.student.Student;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.Month;
import java.util.Optional;

import static java.util.Calendar.AUGUST;
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
    void getStudentById() {
        // given
        Student student = new Student(
                "Peter Parker",
                "spiderMan@gmail.com",
                LocalDate.of(2001, AUGUST, 10)
        );

        student.setId(1L);

        studentRepository.save(student);

        // when
        given(studentRepository.findById(student.getId())).willReturn(Optional.of(student));

        underTest.getStudentById(student.getId());

        // then
        assertThat(studentRepository.findById(student.getId())).isEqualTo(Optional.of(student));
    }


    @Test
    void CanAddNewStudent() {
        //given
        Student student= new Student(
                1L,
                "Guilherme B",
                "Guilherme@gmail.com",
                LocalDate.of(1999, Month.JUNE,11),
                21

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
            student.setId(1L);
            studentRepository.save(student);


            given(studentRepository.existsById(student.getId())).willReturn(true);

            // when
            underTest.deleteStudent(student.getId());

            // then
            verify(studentRepository).deleteById(student.getId());




    }

    @Test
    void InvalidStudentToDelete() {
        // given
        Student student = new Student(
                "Rodrigo",
                "rodrigovalori@hotmail.com",
                LocalDate.of(1999, Month.AUGUST, 6)
        );
        student.setId(1L);
        studentRepository.save(student);



        // when
        given(studentRepository.existsById(student.getId())).willReturn(false);

        // then
        assertThatThrownBy(()->underTest.deleteStudent(student.getId()))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Student with Id" + student.getId() + " does not exists");




    }

    @Test
    void OnUpdateNoStudentExists() {
       //given
        Student student = new Student(
                "Rodrigo",
                "rodrigovalori@hotmail.com",
                LocalDate.of(1999, Month.AUGUST, 6)
        );
        student.setId(1L);
        studentRepository.save(student);

        //given(studentRepository.findById(student.getId())).willReturn(null);

        //when
        //then
        assertThatThrownBy(()->underTest.updateStudent(student.getId(),student.getName(),student.getEmail()))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Student with id"+student.getId()+" does not exists");


    }

    @Test
    void OnUpdateEmailIsTaken() {
        //given
        Student student = new Student(
                "Rodrigo",
                "rodrigovalori@hotmail.com",
                LocalDate.of(1999, Month.AUGUST, 6)
        );
        student.setId(1L);
        studentRepository.save(student);

        //when
        given(studentRepository.findById(student.getId())).willReturn(Optional.of(student));
        given(studentRepository.findStudentByEmail(student.getEmail())).willReturn(Optional.of(student));


        //then
        assertThatThrownBy(()->underTest.updateStudent(student.getId(),student.getName(),student.getEmail()))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("email taken");


    }

    @Test
    void OnUpdateEmailIsNotTaken() {
        //given
        Student student = new Student(
                "Rodrigo",
                "rodrigovalori@hotmail.com",
                LocalDate.of(1999, Month.AUGUST, 6)
        );
        student.setId(1L);
        studentRepository.save(student);

        //when
        given(studentRepository.findById(student.getId())).willReturn(Optional.of(student));
        underTest.updateStudent(student.getId(), "PEPPA PIG", "APORCANOJENTA@gmail.com");
        //then
        assertThat(student.getName()).isEqualTo("PEPPA PIG");
        assertThat((student.getEmail())).isEqualTo("APORCANOJENTA@gmail.com");
    }


}