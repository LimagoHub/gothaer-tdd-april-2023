package de.gothaer.webapp.presentation.mapper;


import de.gothaer.webapp.domain.model.Person;
import de.gothaer.webapp.presentation.dto.PersonDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PersonDTOMapper {

    PersonDTO convert(Person person);
    Person convert(PersonDTO person);
    Iterable<PersonDTO> convert(Iterable<Person> personen);
}
