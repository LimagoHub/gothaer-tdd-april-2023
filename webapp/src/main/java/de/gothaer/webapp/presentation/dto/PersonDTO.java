package de.gothaer.webapp.presentation.dto;


import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

@XmlRootElement
public class PersonDTO {

    @NotNull
    @Size(min=36, max=36)
    private String id;

    @NotNull
    @Size(min=2, max=30)
    private String vorname;

    @NotNull
    @Size(min=2, max=30)
    private String nachname;
}
