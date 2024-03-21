package com.kathemica.demo.hexarchitecture.app.infraestructure.adapters.output.persistence.mapper;

import com.kathemica.demo.hexarchitecture.app.domain.model.Student;
import com.kathemica.demo.hexarchitecture.app.infraestructure.adapters.output.persistence.entity.StudentEntity;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring") //permite que luego se pueda inyectar como componente de spring
public interface StudentPersistenceMapper {

    // cuando los campos son iguales se obvia esta anotación sino se requiere: @Mapping(target = "age", source = "age")
    StudentEntity toStudentEntity(Student student);

    Student toStudent(StudentEntity entity);

    List<Student> toStudentList(List<StudentEntity> entityList);

}