package com.avalialivros.m3s04.model.transport;

import com.avalialivros.m3s04.model.Person;

public record PersonDTO(String guid, String name, String email) {

    public PersonDTO(Person person) {
        this(person.getGuid(), person.getName(), person.getEmail());
    }
}
