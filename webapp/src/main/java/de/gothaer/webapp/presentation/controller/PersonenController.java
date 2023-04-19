package de.gothaer.webapp.presentation.controller;


import de.gothaer.webapp.domain.PersonService;
import de.gothaer.webapp.domain.PersonenServiceException;
import de.gothaer.webapp.presentation.dto.PersonDTO;
import de.gothaer.webapp.presentation.mapper.PersonDTOMapper;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/v1/personen")
@RequiredArgsConstructor
public class PersonenController {

    private final PersonService service;
    private final PersonDTOMapper mapper;

    @ApiResponse(responseCode = "200", description = "Person wurde gefunden")
    @ApiResponse(responseCode = "404", description = "Person wurde nicht gefunden")
    @ApiResponse(responseCode = "400", description = "Falsches Format")
    @ApiResponse(responseCode = "500", description = "interner Serverfehler")

    @GetMapping(path="/{id}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<PersonDTO> findPersonById(@PathVariable String id) throws PersonenServiceException {
        return ResponseEntity.of(service.findeNachId(id).map(mapper::convert));
    }

    @GetMapping(path="", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Iterable<PersonDTO>> findAll(
            @RequestParam (required = false, defaultValue = "") String vorname,
            @RequestParam(required = false, defaultValue = "") String nachname
    ) throws PersonenServiceException{
        System.out.printf("Vorname = %s, nachname = %s\n",vorname, nachname);
        return ResponseEntity.ok(mapper.convert(service.findeAlle()));
    }

    @DeleteMapping(path="/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) throws PersonenServiceException{
        if(service.loeschen(id))
            return ResponseEntity.ok().build();
        return ResponseEntity.notFound().build();
    }

    @PutMapping(path="", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> saveOrUpdate(@Valid @RequestBody PersonDTO person, UriComponentsBuilder builder) throws PersonenServiceException{
        if(service.speichernOderAendern(mapper.convert(person))) {
            return ResponseEntity.ok().build();
        }
        var uri = builder.path("/v1/personen/{id}").buildAndExpand(person.getId());
        return ResponseEntity.created(uri.toUri()).build();
    }

}
