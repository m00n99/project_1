package ru.zakirov.project1.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.zakirov.project1.dao.BookDAO;
import ru.zakirov.project1.models.Person;
import ru.zakirov.project1.services.BooksService;
import ru.zakirov.project1.services.PeopleService;
import ru.zakirov.project1.util.PersonValidator;

import javax.validation.Valid;

@Controller
@RequestMapping("/people")
public class PeopleController {

    private final PersonValidator personValidator;
    private final PeopleService peopleService;
    private final BooksService booksService;

    @Autowired
    public PeopleController(BookDAO bookDAO, PersonValidator personValidator, PeopleService peopleService, BooksService booksService) {
        this.personValidator = personValidator;
        this.peopleService = peopleService;
        this.booksService = booksService;
    }

    @GetMapping()
    public String allPeople(Model model){
        model.addAttribute("people", peopleService.findAll());
        return "people/all-people";
    }

    @GetMapping("/new")
    public String newPeople(@ModelAttribute("person") Person person){
        return "people/new-person";
    }

    @PostMapping()
    public String createPeople(@ModelAttribute("person") @Valid Person person,
                               BindingResult bindingResult){
        personValidator.validate(person, bindingResult);

        if(bindingResult.hasErrors())
            return "people/new-person";

        peopleService.save(person);
        return "redirect:/people";
    }

    @GetMapping("/{id}")
    public String personInfo(@PathVariable int id, Model model){
        model.addAttribute("person", peopleService.findOne(id));
        model.addAttribute("books", booksService.findBooksByOwnerId(id));
        System.out.println(booksService.findBooksByOwnerId(id).get(0).isOverdue());
        return "people/person-info";
    }

    @GetMapping("/{id}/edit")
    public String editPerson(@PathVariable int id, Model model){
        model.addAttribute("person", peopleService.findOne(id));
        return "people/edit-person";
    }

    @PatchMapping("/{id}")
    public String updatePerson(@PathVariable int id, @ModelAttribute("person") @Valid Person person,
                               BindingResult bindingResult){

        if (bindingResult.hasErrors())
            return "people/edit-person";

        peopleService.update(id, person);
        return "redirect:/people";
    }

    @DeleteMapping("/{id}")
    public String deletePerson(@PathVariable int id){
        peopleService.delete(id);
        return "redirect:/people";
    }
}
