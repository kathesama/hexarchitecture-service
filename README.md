# hexarchitecture-service

La arquitectura hexagonal, también conocida como arquitectura de puertos y adaptadores, se basa en la idea de que las 
aplicaciones deben ser independientes de la tecnología subyacente y, por lo tanto, fácilmente intercambiables.
En esta arquitectura, el núcleo del sistema contiene toda la lógica y las reglas de negocio; mientras que los puertos y 
adaptadores se encargan de la entrada y salida de datos.

Aquí hay un ejemplo de cómo podría verse la estructura de un proyecto que sigue la arquitectura hexagonal:

```
com
  └── myproject
      ├── application
      │   ├── port
      │   │   ├── in (Input Ports)
      │   │   └── out (Output Ports)
      │   └── service
      ├── domain
      │   ├── model
      │   └── exception
      └── infrastructure
      │   ├── adapter
      │   │   ├── rest (Input Adapter)
      │   │   │   ├── controller
      │   │   │   ├── dto/model
      │   │   │   └── mapper
      │   │   └── persistence (Output Adapter)
      │   │       ├── entity
      │   │       ├── repository
      │   │       └── mapper
      │   ├── security
      │   │   └── JwtTokenProvider
      │   ├── interceptor/aspect
      │   └── configuration
      │   └── logging
      │       ├── LoggerService
      │       └── LoggerFactory
      └── util/common
```
Aquí hay una explicación de cada uno de los elementos que integran esta estructura de carpetas en un proyecto Java con Spring Boot y la metodología hexagonal:

- `application`: Este paquete contiene la lógica de negocio de tu aplicación.
    - `port`: Define los puertos.
        - `in (Input Ports)`: Define las operaciones que puede realizar.
        - `out (Output Ports)`: Define las operaciones que necesita que se realicen en la infraestructura.
    - `service`: Implementa los puertos de entrada (Input Ports).
    
- `domain`: Este paquete contiene todas las clases que representan el dominio.
    - `model`: Define las entidades del dominio.
    - `exception`: Define las excepciones personalizadas del dominio.

- `infrastructure`: Este paquete contiene todo el código que no pertenece al dominio.
    - `adapter`: Implementa los adaptadores.
        - `rest (Input Adapter)`: Implementa los controladores REST.
            - `controller`: Define los controladores REST.
            - `dto/model`: Define los objetos de transferencia de datos (DTO).
            - `mapper`: Define los mapeadores.
        - `persistence (Output Adapter)`: Implementa la persistencia de .
            - `entity`: Define las entidades de persistencia.
            - `repository`: Define los repositorios.
            - `mapper`: Define los mapeadores.
    - `security`: Implementa la seguridad.
        - `JwtTokenProvider`: Proporciona tokens JWT para la autenticación.
    - `interceptor/aspects`: Define los interceptores.
    - `configuration`: Define la configuración.
    - `logging`: Este paquete contendría todas las clases relacionadas con el logging.
      - `LoggerService`: Esta clase sería un servicio que proporciona métodos para registrar eventos de diferentes niveles (INFO, DEBUG, WARN, ERROR).
      - `LoggerFactory`: Esta clase sería una fábrica que crea instancias de LoggerService para diferentes clases.

- `util/common`: Este paquete contiene todas las clases de utilidad que se utilizan en toda la aplicación.

## Antes de empezar:

1. Puertos:
   1. Los modelos de dominios deber ser solo POJO
   2. Los puertos son interfaces, es lo que se utiliza para comunicarse con el mundo exterior:
      3. input: métodos relacionados al modelo de dominio
      4. output: métodos necesarios para la persistencia y operar con el modelo de dominio.
5. El service implementa el puerto de entrada y hace uso del puerto de salida.
6. **En Spring boot no es una buena práctica hacer la inyección de dependencia mediante @Autowired
sino mediante un constructor de la clase, usando lombok se realiza mediante la anotación @RequiredArgsConstructor**
7. En la infraestructura se crea el adapter, permite persistir los datos en la base de datos, requiere un repositorio y un mapper.
8. Para el output se crean las entidades y en mapper
9. El dominio se desacopla de las tablas / base de datos / data
10. Si se quisiera cambiar de base de datos sólo se deberá cambiar entity y repository
11. Las validaciones de campos deben ir en la infraestructura a y su vez en su respectivo DTO/model del input
12. Usar DTO para los request del input.

## Descripción de la configuración
Este proyecto usa dos librerías que permiten simplificar el código las cuales son Lombok y MapStruct, en el archivo **pom** está el siguiente plugin:

```xml
<properties>
    <java.version>21</java.version>
    <org.projectlombok.version>1.18.30</org.projectlombok.version>
    <org.mapstruct.version>1.5.5.Final</org.mapstruct.version>
    <lombok-mapstruct-binding.version>0.2.0</lombok-mapstruct-binding.version>
    <maven-compiler-plugin.version>3.11.0</maven-compiler-plugin.version>
</properties>

.
.
.

<build>
    <plugins>
        .
        .
        .
        <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-compiler-plugin</artifactId>
            <version>${maven-compiler-plugin.version}</version>
            <configuration>
                <source>${java.version}</source>
                <target>${java.version}</target>
                <annotationProcessorPaths>
                    <path>
                        <groupId>org.projectlombok</groupId>
                        <artifactId>lombok</artifactId>
                        <version>${org.projectlombok.version}</version>
                    </path>
                    <path>
                        <groupId>org.mapstruct</groupId>
                        <artifactId>mapstruct-processor</artifactId>
                        <version>${org.mapstruct.version}</version>
                    </path>
                    <path>
                        <groupId>org.projectlombok</groupId>
                        <artifactId>lombok-mapstruct-binding</artifactId>
                        <version>${lombok-mapstruct-binding.version}</version>
                    </path>
                </annotationProcessorPaths>
            </configuration>
        </plugin>
    </plugins>
</build>
```
Este plugin te permite utilizar en conjunto _Lombok_ con _MapStruct_, ampliando **MapStruct** agrego lo siguiente:

Observemos el siguiente código de ejemplo de uno de los mappers:
```java
@Mapper(componentModel = "spring") //permite que luego se pueda inyectar como componente de spring
public interface StudentPersistenceMapper {
    // cuando los campos son iguales se obvia esta anotación sino se requiere: @Mapping(target = "age", source = "age")
    StudentEntity toStudentEntity(Student student);

    Student toStudent(StudentEntity entity);

    List<Student> toStudentList(List<StudentEntity> entityList);
}
```

Este código es usado luego en el controller de la siguiente manera:

```java
    @GetMapping("/v1/api/{id}")
    public StudentResponse findById(@PathVariable Long id) {
        return restMapper.toStudentResponse(servicePort.findById(id));
    }
```

El código que compartí es un ejemplo de un patrón común en la arquitectura de software llamado **Data Mapper**¹².
En este caso, estás utilizando una biblioteca llamada MapStruct para implementar este patrón en tu aplicación Spring Boot⁴.

MapStruct es una biblioteca de mapeo de objetos que simplifica la implementación de mapeos entre tipos de objetos⁴. Con MapStruct, se puede definir una interfaz de mapeo, como `StudentPersistenceMapper`, y la biblioteca generará automáticamente una implementación en tiempo de compilación⁴.

En la interfaz `StudentPersistenceMapper`, se definen tres métodos:

1. `toStudentEntity(Student student)`: convierte un objeto `Student` a un objeto `StudentEntity`.
2. `toStudent(StudentEntity entity)`: convierte un objeto `StudentEntity` a un objeto `Student`.
3. `toStudentList(List<StudentEntity> entityList)`: convierte una lista de objetos `StudentEntity` a una lista de objetos `Student`.

La anotación `@Mapper(componentModel = "spring")` le indica a MapStruct que genere un componente Spring, lo que significa que se puede inyectar la implementación del mapper en otros beans de Spring⁴.

En cuanto al controlador, `restMapper.toStudentResponse(servicePort.findById(id))` es una llamada a un método que convierte el resultado de `servicePort.findById(id)` a un `StudentResponse`. Aquí, `servicePort.findById(id)` es una llamada a la capa de servicio para buscar un estudiante por su ID. El resultado de esta llamada (un `Student` o `StudentEntity`) se pasa al método `toStudentResponse` para convertirlo a un `StudentResponse`.

* (1) Guide to Using ModelMapper | Baeldung. https://www.baeldung.com/java-modelmapper.
* (2) Spring Boot ModelMapper Example - Map Entity to DTO - Java Guides. https://www.javaguides.net/2022/12/spring-boot-modelmapper-example-map.html.
* (3) Guide to MapStruct with Spring Boot - Refactorizando. https://refactorizando.com/en/guide-to-mapstruct-with-spring-boot/.
* (4) How to Use ModelMapper in Spring Boot with Example Project?. https://www.geeksforgeeks.org/how-to-use-modelmapper-in-spring-boot-with-example-project/.
* (5) http://modelmapper.org/getting-started/.
* (6) https://mvnrepository.com/artifact/org.modelmapper/modelmapper.

### Pero *¿Qué pasa sino se usa MapStruct? ¿Cómo quedaría?*

Si no se está utilizando la biblioteca MapStruct, se tendría que implementar manualmente el mapeo entre los objetos. 

Aquí dejo un ejemplo de cómo podría verse la interfaz `StudentPersistenceMapper` y su implementación:

```java
public interface StudentPersistenceMapper {
    StudentEntity toStudentEntity(Student student);
    Student toStudent(StudentEntity entity);
    List<Student> toStudentList(List<StudentEntity> entityList);
}
```

Y aquí está la implementación de esa interfaz:

```java
@Service
public class StudentPersistenceMapperImpl implements StudentPersistenceMapper {

    @Override
    public StudentEntity toStudentEntity(Student student) {
        // Aquí va la lógica para convertir un Student a un StudentEntity
    }

    @Override
    public Student toStudent(StudentEntity entity) {
        // Aquí va la lógica para convertir un StudentEntity a un Student
    }

    @Override
    public List<Student> toStudentList(List<StudentEntity> entityList) {
        // Aquí va la lógica para convertir una lista de StudentEntity a una lista de Student
    }
}
```

En este ejemplo, `@Service` es una anotación de Spring que indica que la clase es un bean de servicio¹. Esto permite que Spring maneje la vida de este bean y se pueda inyectar donde se necesite¹.

Hay que tener en cuenta que se debe escribir la lógica para convertir entre `Student` y `StudentEntity` en los métodos `toStudentEntity` y `toStudent`¹.
Esto puede ser un poco tedioso, especialmente si se tienen muchos campos para mapear, y es una de las razones por las que las bibliotecas de mapeo como MapStruct son tan útiles¹.

* (1) Quick Guide to MapStruct | Baeldung. https://www.baeldung.com/mapstruct.
* (2) Mapstruct - How can I inject a spring dependency in the Generated .... https://stackoverflow.com/questions/38807415/mapstruct-how-can-i-inject-a-spring-dependency-in-the-generated-mapper-class.
* (3) Deep dive into Mapstruct @ Spring | UpHill Health - Medium. https://medium.com/uphill-engineering-design/deep-dive-into-mapstruct-spring-7ddd8dac3d6d.
* (4) spring boot - How to inject a value to a MapStruct interface - java .... https://stackoverflow.com/questions/71485100/how-to-inject-a-value-to-a-mapstruct-interface-java.

### Hablando de los beans, Spring busca en las interfaces cuáles están implementadas y las inyecta para usarlas. 
¿Cómo funciona?

Un bean es un objeto que es instanciado, ensamblado y administrado por un contenedor de IoC (Inversion of Control) de Spring. Los beans son creados con las configuraciones suministradas a Spring, que están en forma de anotaciones (@Component, @Service, @Repository, @Controller, etc.) o XML.

Cuando arranca la aplicación, Spring crea una instancia de cada clase anotada y las guarda en su contenedor de IoC. Luego, cuando se necesita usar una de estas instancias (por ejemplo, en tu controlador), Spring la inyecta automáticamente. Esto se llama Inyección de Dependencias.

Por lo tanto, Spring busca las implementaciones de las interfaces y las inyecta donde se necesiten, siempre y cuando estén anotadas correctamente y se encuentren en un paquete que Spring esté escaneando.

Por ejemplo, en el controller está servicePort que es una instancia de la interface *StudentServicePort*, pero el servicio es este: 

```Java
public class StudentService implements StudentServicePort {
    private final StudentPersistencePort persistencePort;
    
    @Override
    public Student findById(Long id) {
        return persistencePort.findById(id)
                .orElseThrow(StudentNotFoundException::new);
    }

    @Override
    public List<Student> findAll() {
        return persistencePort.findAll();
    }

    @Override
    public Student save(Student student) {
        return persistencePort.save(student);
    }

    @Override
    public Student update(Long id, Student student) {
        return persistencePort.findById(id)
                .map((Student savedStudent) -> {                    
                    savedStudent.setFirstname(student.getFirstname());
                    savedStudent.setLastname(student.getLastname());
                    savedStudent.setAge(student.getAge());
                    savedStudent.setAddress(student.getAddress());
                    return persistencePort.save(savedStudent);
                })
                .orElseThrow(StudentNotFoundException::new);
    }

    @Override
    public void deleteById(Long id) {
        if (persistencePort.findById(id).isEmpty()) {
            throw new StudentNotFoundException();
        }

        persistencePort.deleteById(id);
    }
}
```

Entonces, al tener un **getMapping** como este:

```Java
@GetMapping("/v1/api/{id}")
public StudentResponse findById(@PathVariable Long id) {
    return restMapper.toStudentResponse(servicePort.findById(id));
}
```
Spring asocia la interfaz con el servicio y hace la llamada al repositorio, *en pocas palabras*: 

**La inyección de dependencias se maneja automáticamente, lo que significa que cuando tienes un campo en una clase anotada con @Autowired (o cuando se inyecta a través de un constructor),
Spring buscará un bean que coincida con esa dependencia y lo inyectará automáticamente.**

**En este caso, servicePort es una instancia de la interfaz StudentServicePort, y Spring inyectará la implementación de esa interfaz, que es StudentService en este caso.**

**Entonces, cuando se llama a servicePort.findById(id) en el controlador, se está llamando al método findById de la clase StudentService.
Este método a su vez llama a persistencePort.findById(id), que es un método de la capa de persistencia.**

**Por lo tanto, Spring asocia la interfaz con el servicio y hace la llamada al repositorio.**

## Nota adicional:

Las variables `DB_HOST`, `DB_USER` y `DB_PASSWORD` son variables de entorno que se utilizan para configurar la conexión a la base de datos. Estas variables no deben ser seteadas directamente en el archivo `application.yml` por razones de seguridad. En su lugar, se deberían establecer en el entorno donde se ejecuta la aplicación.

Hay varias formas de establecer variables de entorno:

1. **En la línea de comandos**: Puedes establecer variables de entorno en la línea de comandos antes de iniciar tu aplicación. Por ejemplo, en un sistema Unix, se podría hacer algo como esto:

```bash
export DB_HOST=localhost
export DB_USER=username
export DB_PASSWORD=password
java -jar myapp.jar
```

2. **En un archivo `.env`**: Algunas herramientas permiten definir variables de entorno en un archivo `.env`. Este archivo debe ser ignorado en el control de versiones para evitar que las credenciales de la base de datos se compartan accidentalmente.

3. **En el IDE**: Si se está ejecutando la aplicación desde un IDE, como IntelliJ IDEA o Eclipse, generalmente se pueden establecer variables de entorno en la configuración del proyecto.

4. **En la plataforma de despliegue**: Si se está desplegando la aplicación en una plataforma como Heroku, AWS o Google Cloud, generalmente proporcionan una forma de establecer variables de entorno a través de su interfaz de usuario o su CLI.

Para el caso específico de IntelliJ IDEA, se pueden establecer las variables de entorno para la aplicación de la siguiente manera:

1. Ve a la configuración del proyecto (Run/Debug Configurations).
2. En la configuración de la aplicación Spring Boot, busca la sección Environment variables.
3. Haz clic en el icono de editar (el icono del lápiz) y aparecerá una ventana para agregar nuevas variables de entorno.
4. Aquí se pueden agregar las variables DB_HOST, DB_USER y DB_PASSWORD con sus respectivos valores.

Importante, hay que recordar que estas variables de entorno son sensibles y no deben ser compartidas ni comprometidas. Por lo tanto, no deben ser incluidas en el control de versiones.