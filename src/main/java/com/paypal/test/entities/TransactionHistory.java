package com.paypal.test.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;

/**
 * Created by rahulaw on 28/01/17.
 */
@AllArgsConstructor
@Getter
public class TransactionHistory {
    private long id ;
    private long accountId ;
    private String message ;
    private String createdAt ;

    public static class TransactionHistoryMapper implements ResultSetMapper<TransactionHistory> {
        public TransactionHistory map(int index, ResultSet r, StatementContext ctx) throws SQLException {
            return new TransactionHistory(r.getLong("id"), r.getLong("account_id"), r.getString("message"), new SimpleDateFormat("HH:mm:ss dd/MM/yyyy").format(r.getTimestamp("created_at")));
        }
    }
}
