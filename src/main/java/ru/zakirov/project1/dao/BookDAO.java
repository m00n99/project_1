package ru.zakirov.project1.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.zakirov.project1.models.Book;
import ru.zakirov.project1.models.Person;

import java.util.List;
import java.util.Optional;

@Component
public class BookDAO {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public BookDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Book> allBooks(){
        return jdbcTemplate.query("SELECT * FROM Book", new BookMapper());
    }

    public void save(Book newBook){
        jdbcTemplate.update("INSERT INTO Book (title, author, year) VALUES (?, ?, ?)",
                newBook.getTitle(), newBook.getAuthor(), newBook.getYear());
    }

    public Book findById(int id) {
        return jdbcTemplate.query("SELECT * FROM Book WHERE book_id=?", new Object[]{id}, new BookMapper())
                .stream().findAny().orElse(null);
    }

    public void updateBook(int id, Book updatedBook){
        jdbcTemplate.update("UPDATE Book SET title=?, author=?, year=? WHERE book_id=?",
                updatedBook.getTitle(), updatedBook.getAuthor(), updatedBook.getYear(), updatedBook.getId());
    }

    public void deleteBook(int id) {
        jdbcTemplate.update("DELETE FROM Book WHERE book_id=?", id);
    }

    public void updateAssignmentBook(int id, Person person) {
        jdbcTemplate.update("UPDATE Book SET person_id=? WHERE book_id=?", person.getId(), id);
    }

    public void releaseBook(int id) {
        jdbcTemplate.update("UPDATE Book SET person_id=null WHERE book_id=?", id);
    }

    public Optional<Person> getBookOwner(int id) {
        return jdbcTemplate.query("SELECT Person.* FROM Book JOIN Person ON Person.person_id=Book.person_id WHERE book_id=?",
                new Object[]{id}, new PersonMapper()).stream().findAny();
    }
}
