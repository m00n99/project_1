package ru.zakirov.project1.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.zakirov.project1.dao.BookDAO;
import ru.zakirov.project1.dao.PersonDAO;
import ru.zakirov.project1.models.Book;
import ru.zakirov.project1.models.Person;

import javax.validation.Valid;
import java.util.Optional;

@Controller
@RequestMapping("/books")
public class BooksController {
    private final BookDAO bookDAO;
    private final PersonDAO personDAO;

    @Autowired
    public BooksController(BookDAO bookDAO, PersonDAO personDAO) {
        this.bookDAO = bookDAO;
        this.personDAO = personDAO;
    }

    @GetMapping()
    public String allBooks(Model model){
        model.addAttribute("books", bookDAO.allBooks());
        return "books/all-books";
    }

    @GetMapping("/new")
    public String newBook(@ModelAttribute("book") Book book){
        return "books/new-book";
    }

    @PostMapping()
    public String crateBook(@ModelAttribute("book") @Valid Book book,
                            BindingResult bindingResult){
        if (bindingResult.hasErrors())
            return "books/new-book";

        bookDAO.save(book);
        return "redirect:/books";
    }

    @GetMapping("/{id}")
    public String bookInfo(@PathVariable int id, Model model, @ModelAttribute("person") Person person){
        model.addAttribute("book", bookDAO.findById(id));

        Optional<Person> bookOwner = bookDAO.getBookOwner(id);

        if (bookOwner.isPresent())
            model.addAttribute("owner", bookOwner.get());
        else
            model.addAttribute("people", personDAO.allPeople());
        return "books/book-info";
    }

    @GetMapping("/{id}/edit")
    public String editBook(@PathVariable int id, Model model){
        model.addAttribute("book", bookDAO.findById(id));
        return "books/edit-book";
    }

    @PatchMapping("/{id}")
    public String updateBook(@PathVariable int id, @ModelAttribute("book") @Valid Book book,
                             BindingResult bindingResult){
        if (bindingResult.hasErrors())
            return "books/edit-book";

        bookDAO.updateBook(id, book);
        return "redirect:/books";
    }

    @DeleteMapping("/{id}")
    public String deleteBook(@PathVariable int id){
        bookDAO.deleteBook(id);
        return "redirect:/books";
    }

    @PatchMapping("/{id}/assign")
    public String assignBook(@PathVariable int id, @ModelAttribute("person") Person selectedPerson){
        bookDAO.updateAssignmentBook(id, selectedPerson);
        return "redirect:/books/" + id;
    }

    @PatchMapping("/{id}/release")
    public String releaseBook(@PathVariable int id){
        bookDAO.releaseBook(id);
        return "redirect:/books/" + id;
    }
}
