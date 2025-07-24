package com.example.golf;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

@Repository
public class MemberRepository {

    private final JdbcTemplate jdbc;

    public MemberRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public Long create(Member m) {
        String sql = "INSERT INTO members(name,address,email,phone,start_date,duration,membership_type) VALUES(?,?,?,?,?,?,?)";
        KeyHolder kh = new GeneratedKeyHolder();
        jdbc.update(con -> {
            PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, m.getName());
            ps.setString(2, m.getAddress());
            ps.setString(3, m.getEmail());
            ps.setString(4, m.getPhone());
            ps.setDate(5, m.getStartDate() != null ? Date.valueOf(m.getStartDate()) : null);
            ps.setObject(6, m.getDuration());
            ps.setString(7, m.getMembershipType());
            return ps;
        }, kh);
        return kh.getKey().longValue();
    }

    public Member findById(Long id) {
        return jdbc.queryForObject("SELECT * FROM members WHERE id = ?", this::map, id);
    }

    public List<Member> findAll() {
        return jdbc.query("SELECT * FROM members", this::map);
    }

    public List<Member> findAllFiltered(String name, String email, String membershipType, String phone) {
        StringBuilder sql = new StringBuilder("SELECT * FROM members WHERE 1=1");
        List<Object> params = new java.util.ArrayList<>();
        
        if (name != null && !name.isEmpty()) {
            sql.append(" AND name LIKE ?");
            params.add("%" + name + "%");
        }
        
        if (email != null && !email.isEmpty()) {
            sql.append(" AND email LIKE ?");
            params.add("%" + email + "%");
        }
        
        if (membershipType != null && !membershipType.isEmpty()) {
            sql.append(" AND membership_type = ?");
            params.add(membershipType);
        }
        
        if (phone != null && !phone.isEmpty()) {
            sql.append(" AND phone LIKE ?");
            params.add("%" + phone + "%");
        }
        
        return jdbc.query(sql.toString(), this::map, params.toArray());
    }

    private Member map(java.sql.ResultSet rs, int rowNum) throws java.sql.SQLException {
        Member m = new Member();
        m.setId(rs.getLong("id"));
        m.setName(rs.getString("name"));
        m.setAddress(rs.getString("address"));
        m.setEmail(rs.getString("email"));
        m.setPhone(rs.getString("phone"));
        Date d = rs.getDate("start_date");
        m.setStartDate(d != null ? d.toLocalDate() : null);
        m.setDuration((Integer) rs.getObject("duration"));
        m.setMembershipType(rs.getString("membership_type"));
        return m;
    }

    public List<Tournament> findTournamentsForMember(Long memberId) {
        String sql = 
            "SELECT t.* FROM tournaments t " +
            "JOIN member_tournament mt ON t.id = mt.tournament_id " +
            "WHERE mt.member_id = ?";
        return jdbc.query(sql, this::mapTournament, memberId);
    }

    private Tournament mapTournament(java.sql.ResultSet rs, int rowNum) throws java.sql.SQLException {
        Tournament t = new Tournament();
        t.setId(rs.getLong("id"));
        java.sql.Date startDate = rs.getDate("start_date");
        t.setStartDate(startDate != null ? startDate.toLocalDate() : null);
        java.sql.Date endDate = rs.getDate("end_date");
        t.setEndDate(endDate != null ? endDate.toLocalDate() : null);
        t.setLocation(rs.getString("location"));
        t.setEntryFee(rs.getBigDecimal("entry_fee"));
        t.setPrizeAmount(rs.getBigDecimal("prize_amount"));
        return t;
    }
}
