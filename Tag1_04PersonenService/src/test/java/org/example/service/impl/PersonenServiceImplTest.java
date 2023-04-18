package org.example.service.impl;

import org.example.persistence.Person;
import org.example.persistence.PersonenRepository;
import org.example.service.BlacklistService;
import org.example.service.PersonenServiceException;
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
    private PersonenRepository personenRepositoryMock;

    @Mock()
    private BlacklistService blacklistServiceMock;

    @Test
    void speichern_personNull_throwsPersonenServiceException() {
        PersonenServiceException ex = assertThrows(PersonenServiceException.class, ()->objectUnderTest.speichern(null));
        assertEquals("Person darf nicht null sein.",ex.getMessage());
    }


    @Nested
    @DisplayName("Tests  für den Vornamen")
    class Vorname {
        @Test
        void speichern_firstnameNull_throwsPersonenServiceException() {
            var invalidPerson = Person.builder().id("123").vorname(null).nachname("Doe").build();
            PersonenServiceException ex = assertThrows(PersonenServiceException.class, () -> objectUnderTest.speichern(invalidPerson));
            assertEquals("Vorname zu kurz.", ex.getMessage());
        }

        @Test
        void speichern_firstnametooShort_throwsPersonenServiceException() {
            var invalidPerson = Person.builder().id("123").vorname("J").nachname("Doe").build();
            PersonenServiceException ex = assertThrows(PersonenServiceException.class, () -> objectUnderTest.speichern(invalidPerson));
            assertEquals("Vorname zu kurz.", ex.getMessage());
        }
    }

    @Test
    void speichern_lastnameNull_throwsPersonenServiceException() {
        var invalidPerson = Person.builder().id("123").vorname("John").nachname(null).build();
        PersonenServiceException ex = assertThrows(PersonenServiceException.class, ()->objectUnderTest.speichern(invalidPerson));
        assertEquals("Nachname zu kurz.",ex.getMessage());
    }

    @Test
    void speichern_lastnameTooShort_throwsPersonenServiceException() {
        var invalidPerson = Person.builder().id("123").vorname("John").nachname("D").build();
        PersonenServiceException ex = assertThrows(PersonenServiceException.class, ()->objectUnderTest.speichern(invalidPerson));
        assertEquals("Nachname zu kurz.",ex.getMessage());
    }

    @Test
    void speichern_PersonaNonGrata_throwsPersonenServiceException() {
        // Arrange
        var invalidPerson = Person.builder().id("123").vorname("John").nachname("Doe").build();

        // Recordmode
        when(blacklistServiceMock.isBlacklisted(any())).thenReturn(true);
        //Act + Assert
        PersonenServiceException ex = assertThrows(PersonenServiceException.class, ()->objectUnderTest.speichern(invalidPerson));
        assertEquals("Antipath",ex.getMessage());
        verify(blacklistServiceMock).isBlacklisted(invalidPerson);// Parameter
    }

    @Test
    void speichern_unexpectedExceptionInUnderlyingService_throwsPersonenServiceException() {
        // Arrange
        var validPerson = Person.builder().id("123").vorname("John").nachname("Doe").build();
        when(blacklistServiceMock.isBlacklisted(any())).thenReturn(false);
        doThrow(ArithmeticException.class).when(personenRepositoryMock).save(any(Person.class));
        //Act
        PersonenServiceException ex = assertThrows(PersonenServiceException.class, ()->objectUnderTest.speichern(validPerson));
        assertEquals("Ein Fehler ist aufgetreten",ex.getMessage());
        assertEquals(ArithmeticException.class, ex.getCause().getClass());
    }

    @Test
    void speichern_HappyDay_personPassedToRepo() throws Exception{

        InOrder inOrder = inOrder(blacklistServiceMock, personenRepositoryMock);
        // Arrange
        var validPerson = Person.builder().id("123").vorname("John").nachname("Doe").build();

        doNothing().when(personenRepositoryMock).save(any(Person.class));
        when(blacklistServiceMock.isBlacklisted(any())).thenReturn(false);
        //Act
        objectUnderTest.speichern(validPerson);
        inOrder.verify(blacklistServiceMock).isBlacklisted(validPerson);// Parameter
        inOrder.verify(personenRepositoryMock, times(1)).save(validPerson);

    }

    @Test
    void speichern_overloaded_HappyDay_personPassedToRepo() throws Exception{


        // Arrange
        ArgumentCaptor<Person> peopleCaptor = ArgumentCaptor.forClass(Person.class);


        doNothing().when(personenRepositoryMock).save(any(Person.class));
        when(blacklistServiceMock.isBlacklisted(any())).thenReturn(false);
        //Act
        objectUnderTest.speichern("John", "Doe");

        verify(personenRepositoryMock, times(1)).save(peopleCaptor.capture());
        List<Person> capturedPeople = peopleCaptor.getAllValues();
        assertEquals("John", capturedPeople.get(0).getVorname());

    }

    @Test
    void speichern_overloaded_alternative_HappyDay_personPassedToRepo() throws Exception{


        // Arrange



        doAnswer(invocationOnMock -> {
            Person capturedPerson = invocationOnMock.getArgument(0);
            assertNotNull(capturedPerson.getId());
            assertEquals(36, capturedPerson.getId().length());
            assertEquals("John", capturedPerson.getVorname());
            assertEquals("Doe", capturedPerson.getNachname());
            return null;
        }).when(personenRepositoryMock).save(any(Person.class));

        when(blacklistServiceMock.isBlacklisted(any())).thenAnswer(invocationOnMock -> {
            return false;
        });
        //Act
        objectUnderTest.speichern("John", "Doe");



    }

    @Test
    void mockdemo() {
        List<String> listMock = mock(List.class);
        listMock.add("Eins");
        listMock.add("Zwei");
        System.out.println(listMock.size());
    }
    //@Spy List<String> spyList = new ArrayList<>();
    @Test
    void spydemo() {

        List<String> concreteList = new ArrayList<>();
        List<String> spyList = spy(concreteList);
        spyList.add("Eins");
        spyList.add("Zwei");

        when(spyList.size()).thenReturn(100);

        System.out.println(spyList.size());
        verify(spyList).add("Eins");
    }

}