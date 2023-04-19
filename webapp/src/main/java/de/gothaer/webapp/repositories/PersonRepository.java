package de.gothaer.webapp.repositories;


import de.gothaer.webapp.repositories.entities.PersonEntity;
import org.springframework.data.repository.CrudRepository;

public interface PersonRepository extends CrudRepository<PersonEntity, String> {

    Iterable<PersonEntity> findByVorname(String vorname);
}
