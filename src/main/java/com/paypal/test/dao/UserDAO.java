package com.paypal.test.dao;

import com.paypal.test.entities.User;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;

import java.util.List;

/**
 * Created by rahulaw on 28/01/17.
 */
public interface UserDAO {

    String tableName = "users";

    @SqlUpdate("CREATE TABLE IF NOT EXISTS " + tableName + " (" +
            "  `id` bigint(20) NOT NULL AUTO_INCREMENT," +
            "  `nick_name` varchar(100) NOT NULL," +
            "  `email` varchar(100) NOT NULL," +
            "  `password` varchar(100) NOT NULL," +
            "  `is_admin` tinyint(4) NOT NULL default 0 ," +
            "  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP," +
            "  PRIMARY KEY (`id`)," +
            "  UNIQUE KEY `nick_name` (`nick_name`)," +
            "  UNIQUE KEY `email` (`email`)" +
            ") ENGINE=InnoDB;")
    void createUsersTable();

    @RegisterMapper(User.UserMapper.class)
    @SqlQuery("select * from users limit :limit offset :offset")
    List<User> getAllUsers(@Bind("limit") int limit,@Bind("offset") int offset);

    @RegisterMapper(User.UserMapper.class)
    @SqlQuery("select * from users where nick_name = :userName and password = :password")
    User validateUser(@Bind("userName") String userName,@Bind("password") String password);


    @SqlUpdate("insert into " + tableName + " (nick_name, email, password) values (:nickName, :email, :password)")
    void createNewUser(@Bind("nickName") String nickName, @Bind("email") String email, @Bind("password") String password);

    @RegisterMapper(User.UserMapper.class)
    @SqlQuery("select * from users where email = :email")
    User getUserByEmail(@Bind("email") String email);
}
