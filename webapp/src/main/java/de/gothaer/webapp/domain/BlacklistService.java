package de.gothaer.webapp.domain;


import de.gothaer.webapp.domain.model.Person;

public interface BlacklistService {

    boolean isBlacklisted(Person person);
}
