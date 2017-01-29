package com.paypal.test.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by rahulaw on 28/01/17.
 */
@Getter
public class User {
    private long id ;
    private String userName ;
    private String email;
    private boolean isAdmin ;
    @JsonIgnore
    private String password ;

    public User(long id, String userName, String email, boolean isAdmin, String password) {
        this.id = id;
        this.userName = userName;
        this.email = email;
        this.isAdmin = isAdmin;
        this.password = password;
    }

    public static class UserMapper implements ResultSetMapper<User> {
        public User map(int index, ResultSet r, StatementContext ctx) throws SQLException {
            return new User(r.getLong("id"), r.getString("user_name"), r.getString("email") , r.getBoolean("is_admin") , r.getString("password"));
        }
    }
}
