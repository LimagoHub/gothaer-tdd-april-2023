package de.gothaer.webapp.presentation.controller;

import de.gothaer.webapp.domain.PersonService;
import de.gothaer.webapp.domain.model.Person;
import de.gothaer.webapp.presentation.dto.PersonDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;


import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//@Sql({"/create.sql", "/insert.sql"})
@ExtendWith(SpringExtension.class)
class PersonenControllerTest {


    @Autowired
    private TestRestTemplate restTemplate;

    @MockBean
    private PersonService serviceMock;

    @Test
    void test_1() throws Exception{

        // Arrange
        final Optional<Person> optionalPerson = Optional.of(Person.builder().id("1").vorname("John").nachname("Doe").build());
        when(serviceMock.findeNachId(anyString())).thenReturn(optionalPerson);


        // Action
        PersonDTO dto = restTemplate.getForObject("/v1/personen/b2e24e74-8686-43ea-baff-d9396b4202e0", PersonDTO.class);

        // Assert
        assertEquals("John", dto.getVorname());
    }
    @Test
    void test_2() throws Exception{

        // Arrange
        final Optional<Person> optionalPerson = Optional.of(Person.builder().id("1").vorname("John").nachname("Doe").build());
        when(serviceMock.findeNachId(anyString())).thenReturn(optionalPerson);


        // Action
        String dto = restTemplate.getForObject("/v1/personen/b2e24e74-8686-43ea-baff-d9396b4202e0", String.class);

        // Assert
        //assertEquals("John", dto.getVorname());
        System.out.println(dto);
    }

    @Test
    void test_3() throws Exception{

        // Arrange
        final Optional<Person> optionalPerson = Optional.of(Person.builder().id("1").vorname("John").nachname("Doe").build());
        when(serviceMock.findeNachId(anyString())).thenReturn(optionalPerson);


        // Action
        ResponseEntity<PersonDTO> entity = restTemplate.getForEntity("/v1/personen/b2e24e74-8686-43ea-baff-d9396b4202e0", PersonDTO.class);

        PersonDTO dto = entity.getBody();
        // Assert
        assertEquals("John", dto.getVorname());
        assertEquals(HttpStatus.OK, entity.getStatusCode());
    }

    @Test
    void test_4() throws Exception{

        // Arrange
        final Optional<Person> optionalPerson = Optional.empty();
        when(serviceMock.findeNachId(anyString())).thenReturn(optionalPerson);


        // Action
        ResponseEntity<PersonDTO> entity = restTemplate.getForEntity("/v1/personen/b2e24e74-8686-43ea-baff-d9396b4202e0", PersonDTO.class);

        PersonDTO dto = entity.getBody();
        // Assert

        assertEquals(HttpStatus.NOT_FOUND, entity.getStatusCode());
    }

    @Test
    void test_5() throws Exception{

        // Arrange
        var personen = List.of(
                Person.builder().id("1").vorname("John").nachname("Doe").build(),
                Person.builder().id("2").vorname("John").nachname("Rambo").build(),
                Person.builder().id("3").vorname("John").nachname("Wayne").build()
        );
        when(serviceMock.findeAlle()).thenReturn(personen);


        // Action
        ResponseEntity<List<PersonDTO>> entity = restTemplate.exchange("/v1/personen", HttpMethod.GET,null,new ParameterizedTypeReference<List<PersonDTO>>() { });

        List<PersonDTO> dto = entity.getBody();
        // Assert
        assertEquals(3, dto.size());
        assertEquals(HttpStatus.OK, entity.getStatusCode());
    }
}