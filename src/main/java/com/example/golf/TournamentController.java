package com.example.golf;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
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
    public List<Tournament> list(
            @RequestParam(name = "startDate", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(name = "location", required = false) String location
    ) {
        return repo.findAllFiltered(startDate, location);
    }

    @PostMapping("/{tournamentId}/members/{memberId}")
    public void addMemberToTournament(
            @PathVariable("tournamentId") Long tournamentId,
            @PathVariable("memberId") Long memberId) {
        repo.addMemberToTournament(tournamentId, memberId);
    }

    @DeleteMapping("/{tournamentId}/members/{memberId}")
    public void removeMemberFromTournament(
            @PathVariable("tournamentId") Long tournamentId,
            @PathVariable("memberId") Long memberId) {
        repo.removeMemberFromTournament(tournamentId, memberId);
    }

    @GetMapping("/{tournamentId}/members")
    public List<Member> getMembersInTournament(@PathVariable("tournamentId") Long tournamentId) {
        return repo.findMembersInTournament(tournamentId);
    }
}