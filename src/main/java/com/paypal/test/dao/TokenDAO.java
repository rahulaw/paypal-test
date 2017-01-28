package com.paypal.test.dao;

import com.paypal.test.entities.Tokens;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;

/**
 * Created by rahulaw on 28/01/17.
 */
public interface TokenDAO {

    String tableName = "tokens";

    @SqlUpdate("CREATE TABLE IF NOT EXISTS " + tableName + " (" +
            "  `id` bigint(20) NOT NULL AUTO_INCREMENT," +
            "  `user_id` bigint(20) NOT NULL ," +
            "  `token` varchar(100) NOT NULL," +
            "  `expiry_time` bigint(20) NOT NULL ," +
            "  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP," +
            "  PRIMARY KEY (`id`)," +
            "  UNIQUE KEY `user` (`user_id`)," +
            "  KEY `token` (`token`)" +
            ") ENGINE=InnoDB;")
    void createTokensTable();

    @RegisterMapper(Tokens.TokenMapper.class)
    @SqlQuery("SELECT * from tokens where user_id = :userId  ")
    Tokens getTokenForUser(@Bind("userId") long userId);

    @SqlUpdate("insert into " + tableName + " (user_id, token, expiry_time) values (:userId, :token, :expiryTime) " +
            "ON DUPLICATE KEY UPDATE token = :token, expiry_time = :expiryTime ")
    void storeToken(@Bind("userId") long userId,@Bind("token")  String token,@Bind("expiryTime")  long expiryTime);

    @RegisterMapper(Tokens.TokenMapper.class)
    @SqlQuery("SELECT * from tokens where token = :token  ")
    Tokens getToken(@Bind("token") String token);
}
