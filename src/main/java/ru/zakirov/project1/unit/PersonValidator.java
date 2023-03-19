package ru.zakirov.project1.unit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.zakirov.project1.dao.PersonDAO;
import ru.zakirov.project1.models.Person;

@Component
public class PersonValidator implements Validator {

    private PersonDAO personDAO;

    @Autowired
    public PersonValidator(PersonDAO personDAO) {
        this.personDAO = personDAO;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return Person.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Person person = (Person) target;

        if (personDAO.findByFio(person.getFio()).isPresent())
            errors.rejectValue("fio", "", "Человек с таким ФИО уже существует");
    }
}
