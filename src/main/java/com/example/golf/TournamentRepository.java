package com.example.golf;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.List;

@Repository
public class TournamentRepository {

    private final JdbcTemplate jdbc;

    public TournamentRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public Long create(Tournament t) {
        String sql = "INSERT INTO tournaments(start_date,end_date,location,entry_fee,prize_amount) VALUES(?,?,?,?,?)";
        KeyHolder kh = new GeneratedKeyHolder();
        jdbc.update(con -> {
            PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setDate(1, t.getStartDate() != null ? Date.valueOf(t.getStartDate()) : null);
            ps.setDate(2, t.getEndDate() != null ? Date.valueOf(t.getEndDate()) : null);
            ps.setString(3, t.getLocation());
            ps.setBigDecimal(4, t.getEntryFee());
            ps.setBigDecimal(5, t.getPrizeAmount());
            return ps;
        }, kh);
        return kh.getKey().longValue();
    }

    public Tournament findById(Long id) {
        return jdbc.queryForObject("SELECT * FROM tournaments WHERE id = ?", this::map, id);
    }

    public List<Tournament> findAll() {
        return jdbc.query("SELECT * FROM tournaments", this::map);
    }

    public List<Tournament> findAllFiltered(LocalDate startDate, String location) {
        StringBuilder sql = new StringBuilder("SELECT * FROM tournaments WHERE 1=1");
        List<Object> params = new java.util.ArrayList<>();
        
        if (startDate != null) {
            sql.append(" AND start_date = ?");
            params.add(Date.valueOf(startDate));
        }
        
        if (location != null && !location.isEmpty()) {
            sql.append(" AND location LIKE ?");
            params.add("%" + location + "%");
        }
        
        return jdbc.query(sql.toString(), this::map, params.toArray());
    }

    private Tournament map(java.sql.ResultSet rs, int rowNum) throws java.sql.SQLException {
        Tournament t = new Tournament();
        t.setId(rs.getLong("id"));
        Date startDate = rs.getDate("start_date");
        t.setStartDate(startDate != null ? startDate.toLocalDate() : null);
        Date endDate = rs.getDate("end_date");
        t.setEndDate(endDate != null ? endDate.toLocalDate() : null);
        t.setLocation(rs.getString("location"));
        t.setEntryFee(rs.getBigDecimal("entry_fee"));
        t.setPrizeAmount(rs.getBigDecimal("prize_amount"));
        return t;
    }

    public void addMemberToTournament(Long tournamentId, Long memberId) {
        String sql = "INSERT IGNORE INTO member_tournament(tournament_id, member_id) VALUES(?,?)";
        jdbc.update(sql, tournamentId, memberId);
    }

    public void removeMemberFromTournament(Long tournamentId, Long memberId) {
        String sql = "DELETE FROM member_tournament WHERE tournament_id = ? AND member_id = ?";
        jdbc.update(sql, tournamentId, memberId);
    }

    public List<Member> findMembersInTournament(Long tournamentId) {
        String sql = "SELECT m.* FROM members m " +
                     "JOIN member_tournament mt ON m.id = mt.member_id " +
                     "WHERE mt.tournament_id = ?";
        return jdbc.query(sql, this::mapMember, tournamentId);
    }

    private Member mapMember(java.sql.ResultSet rs, int rowNum) throws java.sql.SQLException {
        Member m = new Member();
        m.setId(rs.getLong("id"));
        m.setName(rs.getString("name"));
        m.setAddress(rs.getString("address"));
        m.setEmail(rs.getString("email"));
        m.setPhone(rs.getString("phone"));
        java.sql.Date d = rs.getDate("start_date");
        m.setStartDate(d != null ? d.toLocalDate() : null);
        m.setDuration((Integer) rs.getObject("duration"));
        m.setMembershipType(rs.getString("membership_type"));
        return m;
    }
}