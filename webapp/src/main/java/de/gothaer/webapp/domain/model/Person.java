package de.gothaer.webapp.domain.model;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.Setter;




@Data
@Builder
public class Person {

    @Setter(AccessLevel.PRIVATE)
    private String id;

    private String vorname;

    private String nachname;
}
