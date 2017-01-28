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
public class Account {
    private long id ;
    private long userId ;
    private double amount = 100.0;

    public static class AccountMapper implements ResultSetMapper<Account> {
        public Account map(int index, ResultSet r, StatementContext ctx) throws SQLException {
            return new Account(r.getLong("id"), r.getLong("user_id"), r.getDouble("balance"));
        }
    }
}
