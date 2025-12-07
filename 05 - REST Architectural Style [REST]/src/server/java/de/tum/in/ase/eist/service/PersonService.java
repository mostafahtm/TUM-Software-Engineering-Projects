package de.tum.in.ase.eist.service;

import de.tum.in.ase.eist.model.Person;
import de.tum.in.ase.eist.util.PersonSortingOptions;
import org.springframework.stereotype.Service;

import java.util.*;

import static de.tum.in.ase.eist.util.PersonSortingOptions.SortingOrder.ASCENDING;

@Service
public class PersonService {
  	// do not change this
    private final List<Person> persons;

    public PersonService() {
        this.persons = new ArrayList<>();
    }

    public Person savePerson(Person person) {
        var optionalPerson = persons.stream().filter(existingPerson -> existingPerson.getId().equals(person.getId())).findFirst();
        if (optionalPerson.isEmpty()) {
            person.setId(UUID.randomUUID());
            persons.add(person);
            return person;
        } else {
            var existingPerson = optionalPerson.get();
            existingPerson.setFirstName(person.getFirstName());
            existingPerson.setLastName(person.getLastName());
            existingPerson.setBirthday(person.getBirthday());
            return existingPerson;
        }
    }

    public void deletePerson(UUID personId) {
        this.persons.removeIf(person -> person.getId().equals(personId));
    }

    public List<Person> getAllPersons(PersonSortingOptions sortingOptions) {
        // TODO Part 3: Add sorting here

        // Create a copy of the persons list to avoid modifying the original
        List<Person> sortedPersons = new ArrayList<>(this.persons);

        // If no sorting options provided, use default (ID, ASCENDING)
        if (sortingOptions == null) {
            sortingOptions = new PersonSortingOptions();
        }

        // Create a comparator based on the sort field
        Comparator<Person> comparator;

        switch (sortingOptions.getSortField()) {
            case ID:
                // Sort by ID (UUID as string)
                comparator = Comparator.comparing(person -> person.getId().toString());
                break;
            case FIRST_NAME:
                // Sort by first name
                comparator = Comparator.comparing(Person::getFirstName);
                break;
            case LAST_NAME:
                // Sort by last name
                comparator = Comparator.comparing(Person::getLastName);
                break;
            case BIRTHDAY:
                // Sort by birthday (LocalDate is naturally comparable)
                comparator = Comparator.comparing(Person::getBirthday);
                break;
            default:
                // Default to ID sorting
                comparator = Comparator.comparing(person -> person.getId().toString());
                break;
        }

        // Apply descending order if specified
        if (sortingOptions.getSortingOrder() == PersonSortingOptions.SortingOrder.DESCENDING) {
            comparator = comparator.reversed();
        }

        // Sort the list using the comparator
        sortedPersons.sort(comparator);

        return sortedPersons;
    }
}
