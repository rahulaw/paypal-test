package com.paypal.test.entities;

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
    private String nickName ;
    private String email;
    private boolean isAdmin ;
    private String password ;

    public User(long id, String nickName, String email, boolean isAdmin, String password) {
        this.id = id;
        this.nickName = nickName;
        this.email = email;
        this.isAdmin = isAdmin;
        this.password = password;
    }

    public static class UserMapper implements ResultSetMapper<User> {
        public User map(int index, ResultSet r, StatementContext ctx) throws SQLException {
            return new User(r.getLong("id"), r.getString("nick_name"), r.getString("email") , r.getBoolean("is_admin") , r.getString("password"));
        }
    }
}