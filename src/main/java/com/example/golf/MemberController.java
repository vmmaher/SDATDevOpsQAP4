package com.example.golf;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/members")
public class MemberController {

    private final MemberRepository repo;

    public MemberController(MemberRepository repo) {
        this.repo = repo;
    }

    @PostMapping
    public Long create(@RequestBody Member m) {
        return repo.create(m);
    }

    @GetMapping("/{id}")
    public Member get(@PathVariable("id") Long id) {
        try {
            return repo.findById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Member not found");
        }
    }

    @GetMapping
    public List<Member> list(
            @RequestParam(name = "name", required = false) String name,
            @RequestParam(name = "email", required = false) String email,
            @RequestParam(name = "membershipType", required = false) String membershipType,
            @RequestParam(name = "phone", required = false) String phone,
            @RequestParam(name = "tournamentStartDate", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate tournamentStartDate
    ) {
        return repo.findAllFiltered(name, email, membershipType, phone, tournamentStartDate);
    }

    @GetMapping("/{memberId}/tournaments")
    public List<Tournament> getTournamentsForMember(@PathVariable("memberId") Long memberId) {
        return repo.findTournamentsForMember(memberId);
    }
}
