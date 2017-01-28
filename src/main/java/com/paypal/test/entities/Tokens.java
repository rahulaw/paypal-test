package com.paypal.test.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by rahulaw on 28/01/17.
 */
@AllArgsConstructor
@Getter
public class Tokens {
    private long id ;
    private long userId ;
    private String token ;
    private long expiryTime ;

    public static class TokenMapper implements ResultSetMapper<Tokens> {
        public Tokens map(int index, ResultSet r, StatementContext ctx) throws SQLException {
            return new Tokens(r.getLong("id"), r.getLong("user_id"), r.getString("token"), r.getLong("expiry_time"));
        }
    }
}
