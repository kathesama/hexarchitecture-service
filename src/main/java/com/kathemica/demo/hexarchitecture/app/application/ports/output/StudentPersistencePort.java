package com.kathemica.demo.hexarchitecture.app.application.ports.output;

import com.kathemica.demo.hexarchitecture.app.domain.model.Student;

import java.util.List;
import java.util.Optional;

public interface StudentPersistencePort{
    Optional<Student> findById(Long id);
    List<Student> findAll();
    Student save(Student student);
    void deleteById(Long id);
}
