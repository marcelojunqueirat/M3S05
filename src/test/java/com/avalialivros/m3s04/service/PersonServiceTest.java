package com.avalialivros.m3s04.service;

import com.avalialivros.m3s04.exceptions.PersonNotFoundException;
import com.avalialivros.m3s04.model.Person;
import com.avalialivros.m3s04.model.transport.operations.CreatePersonDTO;
import com.avalialivros.m3s04.repository.PersonRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class PersonServiceTest {

    @InjectMocks
    private PersonService personService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private PersonRepository personRepository;

    @Captor
    private ArgumentCaptor<Person> personCaptor;


    @Test
    void createUserWithNoFail() {
        CreatePersonDTO person =
                new CreatePersonDTO("Teste 01", "teste01@example.com", "UmaSenhaForte");
        String passwordEncoded = this.passwordEncoder.encode(person.password());

        this.personService.create(person);
        verify(this.personRepository).save(this.personCaptor.capture());
        Person createdPerson = this.personCaptor.getValue();

        Assertions.assertEquals(person.name(), createdPerson.getName());
        Assertions.assertEquals(person.email(), createdPerson.getEmail());
        Assertions.assertNotNull(createdPerson.getGuid());
        Assertions.assertEquals(36, createdPerson.getGuid().length());
        Assertions.assertEquals(passwordEncoded, createdPerson.getPassword());
    }

    @Test
    void loadUserByEmailWhenUserIsFound() {
        String name = "Teste 01";
        String email = "teste01@example.com.br";
        String exceptionMessage = "Usuário não encontrado";
        Person person = new Person();
        person.setName(name);
        person.setEmail(email);
        person.setPassword(this.passwordEncoder.encode("UmaSenhaForte"));

        BDDMockito.given(this.personRepository.findByEmail(email))
                .willReturn(Optional.of(person));

        Assertions.assertDoesNotThrow(
                () -> this.personService.findByEmail(email), exceptionMessage);
    }

    @Test
    void loadUserByEmailWhenUserIsNotFound() {
        String email = "teste01@example.com.br";
        String exceptionMessage = "Usuário não encontrado";

        BDDMockito.given(this.personRepository.findByEmail(email))
                .willReturn(Optional.empty());

        Assertions.assertThrows(PersonNotFoundException.class,
                () -> this.personService.findByEmail(email), exceptionMessage);
    }
}
