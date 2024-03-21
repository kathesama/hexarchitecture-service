package com.kathemica.demo.hexarchitecture.app.infraestructure.adapters.output.persistence.repository;

import com.kathemica.demo.hexarchitecture.app.infraestructure.adapters.output.persistence.entity.StudentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

// no se anota @Repository porque JpaRepository ya lo implementa y entonces redunda
public interface StudentRepository extends JpaRepository<StudentEntity, Long> {
}
