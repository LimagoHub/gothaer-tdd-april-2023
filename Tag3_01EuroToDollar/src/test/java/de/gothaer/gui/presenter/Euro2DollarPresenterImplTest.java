package de.gothaer.gui.presenter;

import de.gothaer.gui.Euro2DollarRechnerView;
import de.gothaer.model.Euro2DollarRechner;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class Euro2DollarPresenterImplTest {

    @InjectMocks
    private Euro2DollarPresenterImpl objectUnderTest;

    @Mock
    private Euro2DollarRechnerView viewMock;

    @Mock
    private Euro2DollarRechner modelMock;

    private static final String VALID_EURO_VALUE = "10.0";

    @Test
    void onBeenden_HappyDay_maskClosed() {
        objectUnderTest.onBeenden();
        verify(viewMock,times(1)).close();
    }

    @Test
    void onPopulateItems_happyDay_maskInitialized() {
        objectUnderTest.onPopulateItems();
        verify(viewMock,times(1)).setEuro("0");
        verify(viewMock,times(1)).setDollar("0");
        verify(viewMock,times(1)).setRechnenEnabled(true);

    }
    @Test
    void onRechnen_EuroWertAusMaskeHolen_EuroWertAusDerMaskeGeholt() {
       //when(viewMock.getEuro()).thenReturn("25");
       objectUnderTest.onRechnen();
       verify(viewMock).getEuro();

    }

    @Test
    void onRechnen_nullValueInEuroField_errorMessageInDollarField() {
        when(viewMock.getEuro()).thenReturn(null);
        objectUnderTest.onRechnen();
        verify(viewMock).setDollar("Euro darf nicht null sein");

    }

    @Test
    void onRechnen_NanValueInEuroField_errorMessageInDollarField() {
        when(viewMock.getEuro()).thenReturn("Not a Number");
        objectUnderTest.onRechnen();
        verify(viewMock).setDollar("Keine Zahl");

    }

    @Test
    void onRechnen_unexpectedExceptionInUnderlyingService_errorMessageInDollarField() {
        when(viewMock.getEuro()).thenReturn(VALID_EURO_VALUE);
        when(modelMock.calculateEuro2Dollar(anyDouble())).thenThrow(ArithmeticException.class);
        objectUnderTest.onRechnen();
        verify(modelMock).calculateEuro2Dollar(anyDouble());
        verify(viewMock).setDollar("Es ist ein Fehler aufgetreten");

    }
    @Test
    void onRechnen_ValidEuroValue_EuroPassedToModel() {
        when(viewMock.getEuro()).thenReturn(VALID_EURO_VALUE);
        objectUnderTest.onRechnen();
        verify(modelMock).calculateEuro2Dollar(10.0);
    }

    @Test
    void onRechnen_HappyDay_resultInDollarField() {
        when(viewMock.getEuro()).thenReturn(VALID_EURO_VALUE);
        when(modelMock.calculateEuro2Dollar(anyDouble())).thenReturn(4711.0);
        objectUnderTest.onRechnen();
        verify(viewMock).setDollar("4711,00");
    }

    @Test
    void updateRechnenActionState_nullValueInEuroField_rechnenActionDisabled() {
        when(viewMock.getEuro()).thenReturn(null);
        objectUnderTest.updateRechnenActionState();
        verify(viewMock,times(1)).setRechnenEnabled(false);
    }

    @Test
    void updateRechnenActionState_NanValueInEuroField_rechnenActionDisabled() {
        when(viewMock.getEuro()).thenReturn("Not a Number");
        objectUnderTest.updateRechnenActionState();
        verify(viewMock,times(1)).setRechnenEnabled(false);
    }

    @Test
    void updateRechnenActionState_trailingNumbersValueInEuroField_rechnenActionDisabled() {
        when(viewMock.getEuro()).thenReturn("100x");
        objectUnderTest.updateRechnenActionState();
        verify(viewMock,times(1)).setRechnenEnabled(false);
    }

    @Test
    void updateRechnenActionState_validValueInEuroField_rechnenActionEnabled() {
        when(viewMock.getEuro()).thenReturn(VALID_EURO_VALUE);
        objectUnderTest.updateRechnenActionState();
        verify(viewMock,times(1)).setRechnenEnabled(true);
    }
}