package de.gothaer.webapp.domain.inner;

import de.gothaer.webapp.domain.BlacklistService;
import de.gothaer.webapp.domain.model.Person;
import org.springframework.stereotype.Service;


import java.util.List;

@Service
public class BlacklistServiceImpl implements BlacklistService {
    @Override
    public boolean isBlacklisted(final Person person) {
        var antipathen = List.of("Attila", "Peter","Paul","Mary");
        return antipathen.contains(person.getVorname());
    }
}
