package ru.zakirov.project1.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.zakirov.project1.dao.BookDAO;
import ru.zakirov.project1.dao.PersonDAO;
import ru.zakirov.project1.models.Person;
import ru.zakirov.project1.unit.PersonValidator;

import javax.validation.Valid;

@Controller
@RequestMapping("/people")
public class PeopleController {

    private final PersonDAO personDAO;
    private final PersonValidator personValidator;

    @Autowired
    public PeopleController(PersonDAO personDAO, BookDAO bookDAO, PersonValidator personValidator) {
        this.personDAO = personDAO;
        this.personValidator = personValidator;
    }

    @GetMapping()
    public String allPeople(Model model){
        model.addAttribute("people", personDAO.allPeople());
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

        personDAO.save(person);
        return "redirect:/people";
    }

    @GetMapping("/{id}")
    public String personInfo(@PathVariable int id, Model model){
        model.addAttribute("person", personDAO.findById(id));
        model.addAttribute("books", personDAO.findBooksByPersonId(id));
        return "people/person-info";
    }

    @GetMapping("/{id}/edit")
    public String editPerson(@PathVariable int id, Model model){
        model.addAttribute("person", personDAO.findById(id));
        return "people/edit-person";
    }

    @PatchMapping("/{id}")
    public String updatePerson(@PathVariable int id, @ModelAttribute("person") @Valid Person person,
                               BindingResult bindingResult){
        personValidator.validate(person, bindingResult);

        if (bindingResult.hasErrors())
            return "people/edit-person";

        personDAO.updatePerson(id, person);
        return "redirect:/people";
    }

    @DeleteMapping("/{id}")
    public String deletePerson(@PathVariable int id){
        personDAO.deletePerson(id);
        return "redirect:/people";
    }
}
