package de.gothaer.service;

import de.gothaer.dependency.Dependency;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.internal.verification.Times;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class MyServiceUsingDependencyImplTest {

    @InjectMocks
    private MyServiceUsingDependencyImpl objectUnderTest;

    @Mock
    private Dependency dependencyMock;



    @Test
    void demo() {
        // RecordMode
        //when(dependencyMock.foobar(anyString())).thenReturn(10);
        //Mockito.when(dependencyMock.foobar("Welt")).thenReturn(47);
        // Replay

        doThrow(NullPointerException.class).when(dependencyMock).foo(anyString());
       objectUnderTest.myFirstMethod();

        verify(dependencyMock, atLeast(1)).foo("HALLO");
    }

    @Test
    void demo2() {
        // RecordMode
        when(dependencyMock.bar()).thenReturn(10);
        //when(dependencyMock.bar()).thenThrow(NullPointerException.class);
        //Mockito.when(dependencyMock.foobar("Welt")).thenReturn(47);
        // Replay

        var result = objectUnderTest.mySecondMethod();
        assertEquals(15, result);
        verify(dependencyMock).bar();
    }
    @Test
    void demo3() {
        // RecordMode
        when(dependencyMock.foobar(anyString())).thenReturn(4711);
        //Mockito.when(dependencyMock.foobar("Welt")).thenReturn(47);
        // Replay

        var result = objectUnderTest.myThirdMethod("Irgendwas");

        assertEquals(4716, result);
        verify(dependencyMock).foobar("IRGENDWAS");

    }

}