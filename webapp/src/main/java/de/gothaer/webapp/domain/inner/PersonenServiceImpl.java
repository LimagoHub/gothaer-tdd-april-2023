package de.gothaer.webapp.domain.inner;


import de.gothaer.webapp.domain.BlacklistService;
import de.gothaer.webapp.domain.PersonService;
import de.gothaer.webapp.domain.PersonenServiceException;
import de.gothaer.webapp.domain.mapper.PersonMapper;
import de.gothaer.webapp.domain.model.Person;
import de.gothaer.webapp.repositories.PersonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(propagation = Propagation.REQUIRED, rollbackFor = PersonenServiceException.class, isolation = Isolation.READ_COMMITTED)
public class PersonenServiceImpl implements PersonService {

    private final PersonRepository repo;
    private final PersonMapper mapper;
    private final BlacklistService blacklistService;

    /*
            person null -> PSE
            vorname null vorname kleiner 2 -> PSE
            nachname null nachname kleiner 2 -> PSE

            Attila -> PSE

            technische Exception -> PSE

            happy Day -> person to repo
     */
    @Override
    public boolean speichernOderAendern(Person person) throws PersonenServiceException {
        try {
            if(person == null)
                throw new PersonenServiceException("Person darf nicht null sein.");

            if(person.getVorname() == null || person.getVorname().length() < 2)
                throw new PersonenServiceException("Vorname zu kurz.");

            if(person.getNachname() == null || person.getNachname().length() < 2)
                throw new PersonenServiceException("Nachname zu kurz.");

            if(blacklistService.isBlacklisted(person))
                throw new PersonenServiceException("Antipath");

            boolean exists = repo.existsById(person.getId());

            repo.save(mapper.convert(person));

            return exists;
        } catch (RuntimeException e) {
            throw new PersonenServiceException("Ein Fehler ist aufgetreten",e);
        }
    }

    @Override
    @Transactional(isolation = Isolation.READ_UNCOMMITTED)
    public Optional<Person> findeNachId(String id) throws PersonenServiceException {
        try {
           return repo.findById(id).map(mapper::convert);
        } catch (RuntimeException e) {
            throw new PersonenServiceException("Upps",e);
        }
    }

    @Override
    public Iterable<Person> findeAlle() throws PersonenServiceException {
        try {
            return mapper.convert( repo.findAll());
        } catch (RuntimeException e) {
            throw new PersonenServiceException("Upps",e);
        }
    }

    @Override
    public boolean loeschen(String id) throws PersonenServiceException {

        try {
            if(repo.existsById(id)) {
                repo.deleteById(id);
                return true;
            }
            return false;
        } catch (RuntimeException e) {
            throw new PersonenServiceException("Upps",e);
        }
    }
}
