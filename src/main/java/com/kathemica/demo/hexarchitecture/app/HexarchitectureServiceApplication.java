package com.kathemica.demo.hexarchitecture.app;

import com.kathemica.demo.hexarchitecture.app.infraestructure.adapters.output.persistence.entity.StudentEntity;
import com.kathemica.demo.hexarchitecture.app.infraestructure.adapters.output.persistence.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Arrays;
import java.util.List;

@SpringBootApplication(scanBasePackages = "com.kathemica.demo.hexarchitecture.app")
@RequiredArgsConstructor
public class HexarchitectureServiceApplication implements CommandLineRunner {
	private final StudentRepository repository;

	public static void main(String[] args) {
		SpringApplication.run(HexarchitectureServiceApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		List<StudentEntity> entities = Arrays.asList(
				new StudentEntity(null, "Juan", "Mendoza", 28, "Calle 1"),
				new StudentEntity(null, "Carlos", "Rodriguez", 30, "Calle 2"),
				new StudentEntity(null, "Julio", "Perez", 10, "Calle 3"),
				new StudentEntity(null, "Roman", "Ramirez", 40, "Calle 4")
		);
		repository.saveAll(entities);
	}

}
