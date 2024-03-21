package com.kathemica.demo.hexarchitecture.app.infraestructure.adapters.input.rest;

import com.kathemica.demo.hexarchitecture.app.application.ports.input.StudentServicePort;
import com.kathemica.demo.hexarchitecture.app.infraestructure.adapters.input.rest.mapper.StudentRestMapper;
import com.kathemica.demo.hexarchitecture.app.infraestructure.adapters.input.rest.model.request.StudentCreateRequest;
import com.kathemica.demo.hexarchitecture.app.infraestructure.adapters.input.rest.model.response.StudentResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/students")
public class StudentRestController {
    private final StudentServicePort servicePort;
    private final StudentRestMapper restMapper;

    @GetMapping("/v1/api")
    public List<StudentResponse> findAll() {
        return restMapper.toStudentResponseList(servicePort.findAll());
    }

    @GetMapping("/v1/api/{id}")
    public StudentResponse findById(@PathVariable Long id) {
        return restMapper.toStudentResponse(servicePort.findById(id));
    }

    @PostMapping("/v1/api")
    public ResponseEntity<StudentResponse> save(@Valid @RequestBody StudentCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(restMapper.toStudentResponse(
                        servicePort.save(
                                restMapper.toStudent(request)
                        )
                )
              );
    }

    @PutMapping("/v1/api/{id}")
    public StudentResponse update(@PathVariable Long id, @Valid @RequestBody StudentCreateRequest request) {
        return restMapper.toStudentResponse(
                servicePort.update(id, restMapper.toStudent(request)));
    }

    @DeleteMapping("/v1/api/{id}")
    public void delete(@PathVariable Long id) {
        servicePort.deleteById(id);
    }

}
