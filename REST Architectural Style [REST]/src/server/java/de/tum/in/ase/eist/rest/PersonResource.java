package de.tum.in.ase.eist.rest;

import de.tum.in.ase.eist.model.Person;
import de.tum.in.ase.eist.service.PersonService;
import de.tum.in.ase.eist.util.PersonSortingOptions;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
public class PersonResource {

    private final PersonService personService;

    public PersonResource(PersonService personService) {
        this.personService = personService;
    }

    // TODO Part 1: Implement the specified endpoints here

    // Endpoint 1: CREATE a new person
    // This handles POST requests to /persons
    @PostMapping("/persons")
    public ResponseEntity<Person> createPerson(@RequestBody Person person) {
        // Check if the person already has an ID (it shouldn't for a new person)
        if (person.getId() != null) {
            // Return 400 Bad Request if person already has an ID
            return ResponseEntity.badRequest().build();
        }

        // Save the person using the service (this will generate a new UUID)
        Person savedPerson = personService.savePerson(person);

        // Return 200 Created status with the saved person in the response body
        return ResponseEntity.status(200).body(savedPerson);
    }

    // Endpoint 2: UPDATE an existing person
    // This handles PUT requests to /persons/{personId}
    @PutMapping("/persons/{personId}")
    public ResponseEntity<Person> updatePerson(@PathVariable UUID personId, @RequestBody Person person) {
        // Validate: if person has an ID and it doesn't match the URL path ID, return 400 Bad Request
        if (person.getId() != null && !person.getId().equals(personId)) {
            return ResponseEntity.badRequest().build();
        }

        // Set the person's ID to the one from the URL path
        // This ensures we're updating the correct person
        person.setId(personId);

        // Save the person (if ID exists, it updates; PersonService handles this)
        Person updatedPerson = personService.savePerson(person);

        // Return 200 OK status with the updated person in the response body
        return ResponseEntity.ok(updatedPerson);
    }

    // Endpoint 3: DELETE a person
    // This handles DELETE requests to /persons/{personId}
    @DeleteMapping("/persons/{personId}")
    public ResponseEntity<Void> deletePerson(@PathVariable UUID personId) {
        // Delete the person with the given ID
        personService.deletePerson(personId);

        // Return 204 No Content status (successful deletion, no body needed)
        return ResponseEntity.noContent().build();
    }

    // Endpoint 4: GET all persons with optional sorting
    // This handles GET requests to /persons?sortField=FIRST_NAME&sortingOrder=ASCENDING
    @GetMapping("/persons")
    public ResponseEntity<List<Person>> getAllPersons(
            // @RequestParam extracts query parameters from URL
            // defaultValue ensures it works even if parameters are not provided
            @RequestParam(defaultValue = "ID") PersonSortingOptions.SortField sortField,
            @RequestParam(defaultValue = "ASCENDING") PersonSortingOptions.SortingOrder sortingOrder
    ) {
        // Create PersonSortingOptions from the query parameters
        PersonSortingOptions sortingOptions = new PersonSortingOptions(sortingOrder, sortField);

        // Get all persons from the service with sorting applied
        List<Person> persons = personService.getAllPersons(sortingOptions);

        // Return 200 OK status with the sorted list of persons in the response body
        return ResponseEntity.ok(persons);
    }
}
