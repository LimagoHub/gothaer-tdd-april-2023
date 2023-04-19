package de.gothaer.webapp.domain.inner;


import de.gothaer.webapp.domain.BlacklistService;
import de.gothaer.webapp.domain.PersonenServiceException;
import de.gothaer.webapp.domain.mapper.PersonMapper;
import de.gothaer.webapp.domain.model.Person;
import de.gothaer.webapp.repositories.PersonRepository;
import de.gothaer.webapp.repositories.entities.PersonEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@MockitoSettings(strictness = Strictness.STRICT_STUBS)
@ExtendWith(MockitoExtension.class)
class PersonenServiceImplTest {

    @InjectMocks
    private PersonenServiceImpl objectUnderTest;

    @Mock
    private PersonRepository personenRepositoryMock;

    @Mock
    private BlacklistService blacklistServiceMock;

    @Mock
    private PersonMapper mapperMock;

    @Test
    void speichern_personNull_throwsPersonenServiceException() {
        PersonenServiceException ex = assertThrows(PersonenServiceException.class, () -> objectUnderTest.speichernOderAendern(null));
        assertEquals("Person darf nicht null sein.", ex.getMessage());
    }


    @Nested
    @DisplayName("Tests  für den Vornamen")
    class Vorname {
        @Test
        void speichern_firstnameNull_throwsPersonenServiceException() {
            var invalidPerson = Person.builder().id("123").vorname(null).nachname("Doe").build();
            PersonenServiceException ex = assertThrows(PersonenServiceException.class, () -> objectUnderTest.speichernOderAendern(invalidPerson));
            assertEquals("Vorname zu kurz.", ex.getMessage());
        }

        @Test
        void speichern_firstnametooShort_throwsPersonenServiceException() {
            var invalidPerson = Person.builder().id("123").vorname("J").nachname("Doe").build();
            PersonenServiceException ex = assertThrows(PersonenServiceException.class, () -> objectUnderTest.speichernOderAendern(invalidPerson));
            assertEquals("Vorname zu kurz.", ex.getMessage());
        }
    }

    @Test
    void speichern_lastnameNull_throwsPersonenServiceException() {
        var invalidPerson = Person.builder().id("123").vorname("John").nachname(null).build();
        PersonenServiceException ex = assertThrows(PersonenServiceException.class, () -> objectUnderTest.speichernOderAendern(invalidPerson));
        assertEquals("Nachname zu kurz.", ex.getMessage());
    }

    @Test
    void speichern_lastnameTooShort_throwsPersonenServiceException() {
        var invalidPerson = Person.builder().id("123").vorname("John").nachname("D").build();
        PersonenServiceException ex = assertThrows(PersonenServiceException.class, () -> objectUnderTest.speichernOderAendern(invalidPerson));
        assertEquals("Nachname zu kurz.", ex.getMessage());
    }

    @Test
    void speichern_PersonaNonGrata_throwsPersonenServiceException() {
        // Arrange
        var invalidPerson = Person.builder().id("123").vorname("John").nachname("Doe").build();

        // Recordmode
        when(blacklistServiceMock.isBlacklisted(any())).thenReturn(true);
        //Act + Assert
        PersonenServiceException ex = assertThrows(PersonenServiceException.class, () -> objectUnderTest.speichernOderAendern(invalidPerson));
        assertEquals("Antipath", ex.getMessage());
        verify(blacklistServiceMock).isBlacklisted(invalidPerson);// Parameter
    }

    @Test
    void speichern_unexpectedExceptionInUnderlyingService_throwsPersonenServiceException() {
        // Arrange
        var validPerson = Person.builder().id("123").vorname("John").nachname("Doe").build();
        var validPersonEntity = PersonEntity.builder().id("123").vorname("John").nachname("Doe").build();

        when(mapperMock.convert(any(Person.class))).thenReturn(validPersonEntity);
        when(blacklistServiceMock.isBlacklisted(any())).thenReturn(false);
        doThrow(ArithmeticException.class).when(personenRepositoryMock).save(any(PersonEntity.class));
        //Act
        PersonenServiceException ex = assertThrows(PersonenServiceException.class, () -> objectUnderTest.speichernOderAendern(validPerson));
        assertEquals("Ein Fehler ist aufgetreten", ex.getMessage());
        assertEquals(ArithmeticException.class, ex.getCause().getClass());
    }

    @Test
    void speichern_HappyDay_personPassedToRepo() throws Exception {

        InOrder inOrder = inOrder(blacklistServiceMock, personenRepositoryMock);
        // Arrange
        var validPerson = Person.builder().id("123").vorname("John").nachname("Doe").build();
        var validPersonEntity = PersonEntity.builder().id("123").vorname("John").nachname("Doe").build();

        when(mapperMock.convert(any(Person.class))).thenReturn(validPersonEntity);
        when(personenRepositoryMock.save(validPersonEntity)).thenReturn(validPersonEntity);
        when(blacklistServiceMock.isBlacklisted(any())).thenReturn(false);
        //Act
        objectUnderTest.speichernOderAendern(validPerson);
        inOrder.verify(blacklistServiceMock).isBlacklisted(validPerson);// Parameter
        inOrder.verify(personenRepositoryMock, times(1)).save(validPersonEntity);

    }
}

