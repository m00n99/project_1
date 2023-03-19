package ru.zakirov.project1.models;

import javax.validation.constraints.*;

public class Person {
    private int id;
    @NotEmpty(message = "ФИО не должно быть пустым")
    @Size(min = 2, max = 100, message = "ФИО должно быть от 2 до 100 символов длиной")
    private String fio;

    @Min(value = 1900, message = "Год рождения должен быть больше, чем 1900")
    private int yearOfBirth;

    public Person(String fio, int yearOfBirth) {
        this.fio = fio;
        this.yearOfBirth = yearOfBirth;
    }

    public Person(){}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFio() {
        return fio;
    }

    public void setFio(String fio) {
        this.fio = fio;
    }

    public int getYearOfBirth() {
        return yearOfBirth;
    }

    public void setYearOfBirth(int yearOfBirth) {
        this.yearOfBirth = yearOfBirth;
    }
}
