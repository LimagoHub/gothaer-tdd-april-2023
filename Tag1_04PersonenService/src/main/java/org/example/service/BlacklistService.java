package org.example.service;

import org.example.persistence.Person;

public interface BlacklistService {

    boolean isBlacklisted(Person person);
}
