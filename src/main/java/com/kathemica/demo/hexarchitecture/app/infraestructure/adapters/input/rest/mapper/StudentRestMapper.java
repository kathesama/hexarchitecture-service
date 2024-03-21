package com.kathemica.demo.hexarchitecture.app.infraestructure.adapters.input.rest.mapper;

import com.kathemica.demo.hexarchitecture.app.domain.model.Student;
import com.kathemica.demo.hexarchitecture.app.infraestructure.adapters.input.rest.model.request.StudentCreateRequest;
import com.kathemica.demo.hexarchitecture.app.infraestructure.adapters.input.rest.model.response.StudentResponse;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE //ignorar los campos que no se mapean
)
public interface StudentRestMapper {

    Student toStudent(StudentCreateRequest request);

    StudentResponse toStudentResponse(Student student);

    List<StudentResponse> toStudentResponseList(List<Student> studentList);

}
