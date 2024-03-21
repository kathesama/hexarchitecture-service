package com.kathemica.demo.hexarchitecture.app.application.ports.input;

import com.kathemica.demo.hexarchitecture.app.domain.model.Student;

import java.util.List;

public interface StudentServicePort {
    Student findById(Long id);
    List<Student> findAll();

    Student save(Student student);

    Student update(Long id, Student student);

    void deleteById(Long id);
}
