package de.gothaer.webapp.domain.mapper;


import de.gothaer.webapp.domain.model.Person;
import de.gothaer.webapp.repositories.entities.PersonEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PersonMapper {

    Person convert(PersonEntity entity);
    PersonEntity convert(Person person);
    Iterable<Person> convert(Iterable<PersonEntity> entities);
}
