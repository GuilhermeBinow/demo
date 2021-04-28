package com.example.demo.student.config;

import com.example.demo.student.repository.StudentRepository;
import com.example.demo.student.student.Student;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.util.List;

import static java.time.Month.*;

@Configuration
public class StudentConfig {
    @Bean
    CommandLineRunner commandLineRunner(StudentRepository repository){
        return args -> {
            Student Sussa = new Student(
                    "Sussa",
                    "Yo",
                    LocalDate.of(1999, JUNE, 11)
            );

            Student Sassy = new Student(
                    "Sassy",
                    "Yo",
                    LocalDate.of(1998, JUNE, 11)
            );

            repository.saveAll(
                    List.of(Sussa, Sassy)
            );

        };
    }
}
