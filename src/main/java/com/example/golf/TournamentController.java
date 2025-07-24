package com.example.golf;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/tournaments")
public class TournamentController {

    private final TournamentRepository repo;

    public TournamentController(TournamentRepository repo) {
        this.repo = repo;
    }

    @PostMapping
    public Long create(@RequestBody Tournament t) {
        return repo.create(t);
    }

    @GetMapping("/{id}")
    public Tournament get(@PathVariable("id") Long id) {
        try {
            return repo.findById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Tournament not found");
        }
    }

    @GetMapping
    public List<Tournament> list() {
        return repo.findAll();
    }
}