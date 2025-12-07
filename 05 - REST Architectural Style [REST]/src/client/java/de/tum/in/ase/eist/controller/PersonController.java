package de.tum.in.ase.eist.controller;

import de.tum.in.ase.eist.model.Person;
import de.tum.in.ase.eist.util.PersonSortingOptions;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class PersonController {
    // WebClient is used to send HTTP requests to the server
    private final WebClient webClient;

    // Internal list to keep track of all persons on the client side
    private final List<Person> persons;

    // Constructor: Initialize the WebClient and the persons list
    public PersonController() {
        // Build the WebClient with base URL and default headers
        this.webClient = WebClient.builder()
                .baseUrl("http://localhost:8080/")  // Server address
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)  // We accept JSON
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)  // We send JSON
                .build();
        this.persons = new ArrayList<>();
    }

    // Method 1: Add a new person
    public void addPerson(Person person, Consumer<List<Person>> personsConsumer) {
        // TODO Part 2: Make an http post request to the server

        // Send POST request to /persons endpoint
        webClient.post()
                .uri("persons")  // Endpoint path (baseUrl + "persons" = "http://localhost:8080/persons")
                .bodyValue(person)  // Send the person object as JSON in the request body
                .retrieve()  // Execute the request and get the response
                .bodyToMono(Person.class)  // Convert the JSON response to a Person object (async)
                .onErrorStop()  // Stop if there's an error
                .subscribe(newPerson -> {  // When response arrives, execute this code
                    // Add the newly created person (with ID) to our local list
                    persons.add(newPerson);
                    // Notify the UI by calling the consumer with updated list
                    personsConsumer.accept(persons);
                });
    }

    // Method 2: Update an existing person
    public void updatePerson(Person person, Consumer<List<Person>> personsConsumer) {
        // TODO Part 2: Make an http put request to the server

        // Send PUT request to /persons/{id} endpoint
        webClient.put()
                .uri("persons/" + person.getId())  // Include the person's ID in the URL
                .bodyValue(person)  // Send the updated person data as JSON
                .retrieve()  // Execute the request
                .bodyToMono(Person.class)  // Convert response to Person object
                .onErrorStop()  // Stop on error
                .subscribe(updatedPerson -> {  // When response arrives
                    // Replace the old person with the updated one in our local list
                    persons.replaceAll(oldPerson ->
                        oldPerson.getId().equals(updatedPerson.getId()) ? updatedPerson : oldPerson
                    );
                    // Notify the UI with the updated list
                    personsConsumer.accept(persons);
                });
    }

    // Method 3: Delete a person
    public void deletePerson(Person person, Consumer<List<Person>> personsConsumer) {
        // TODO Part 2: Make an http delete request to the server

        // Send DELETE request to /persons/{id} endpoint
        webClient.delete()
                .uri("persons/" + person.getId())  // Include the person's ID in the URL
                .retrieve()  // Execute the request
                .toBodilessEntity()  // No response body expected (204 No Content)
                .onErrorStop()  // Stop on error
                .subscribe(v -> {  // When response arrives (v is ignored, no body)
                    // Remove the person from our local list
                    persons.remove(person);
                    // Notify the UI with the updated list
                    personsConsumer.accept(persons);
                });
    }

    // Method 4: Get all persons from the server with sorting
    public void getAllPersons(PersonSortingOptions sortingOptions, Consumer<List<Person>> personsConsumer) {
        // TODO Part 2: Make an https get request to the server

        // Send GET request to /persons endpoint with query parameters
        webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("persons")  // Base path
                        // Add query parameters for sorting
                        .queryParam("sortField", sortingOptions.getSortField())  // e.g., sortField=FIRST_NAME
                        .queryParam("sortingOrder", sortingOptions.getSortingOrder())  // e.g., sortingOrder=ASCENDING
                        .build())  // Build the complete URI: /persons?sortField=FIRST_NAME&sortingOrder=ASCENDING
                .retrieve()  // Execute the request
                .bodyToMono(new ParameterizedTypeReference<List<Person>>() {})  // Convert JSON array to List<Person>
                .onErrorStop()  // Stop on error
                .subscribe(newPersons -> {  // When response arrives
                    // Clear the local list and replace with server data (already sorted by server)
                    persons.clear();
                    persons.addAll(newPersons);
                    // Notify the UI with the updated list
                    personsConsumer.accept(persons);
                });
    }
}