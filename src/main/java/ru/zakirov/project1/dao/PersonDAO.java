package ru.zakirov.project1.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.zakirov.project1.models.Book;
import ru.zakirov.project1.models.Person;

import java.util.List;
import java.util.Optional;

@Component
public class PersonDAO {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public PersonDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Person> allPeople(){
        return jdbcTemplate.query("SELECT * FROM Person", new PersonMapper());
    }

    public void save(Person newPerson){
        jdbcTemplate.update("INSERT INTO Person (fio, year_of_birth) VALUES (?, ?)",
                newPerson.getFio(), newPerson.getYearOfBirth());
    }

    public Person findById(int id){
        return jdbcTemplate.query("SELECT * FROM Person WHERE person_id=?",
                new Object[]{id}, new PersonMapper()).stream().findAny().orElse(null);
    }

    public Optional<Person> findByFio(String fio){
        return jdbcTemplate.query("SELECT * FROM Person WHERE fio=?", new Object[]{fio}, new PersonMapper())
                .stream().findAny();
    }

    public void updatePerson(int id, Person updatedPerson){
        jdbcTemplate.update("UPDATE Person SET fio=?, year_of_birth=? WHERE person_id=?",
                updatedPerson.getFio(), updatedPerson.getYearOfBirth(), id);
    }

    public void deletePerson(int id){
        jdbcTemplate.update("DELETE FROM Person WHERE person_id=?", id);
    }

    public List<Book> findBooksByPersonId(int id) {
        return jdbcTemplate.query("SELECT * FROM Book WHERE person_id=?", new Object[]{id}, new BookMapper());
    }
}
