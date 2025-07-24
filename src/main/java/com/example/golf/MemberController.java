package com.example.golf;

import org.springframework.web.bind.annotation.*;

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
        return repo.findById(id);
    }

    @GetMapping
    public List<Member> list(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String membershipType
    ) {
        return repo.findAllFiltered(name, email, membershipType);
    }
}
