package com.paypal.test.dao;

import com.paypal.test.entities.Account;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;

/**
 * Created by rahulaw on 28/01/17.
 */
public interface AccountDAO {

    String tableName = "accounts";

    @SqlUpdate("CREATE TABLE `accounts` (\n" +
            "  `id` bigint(10) NOT NULL AUTO_INCREMENT,\n" +
            "  `user_id` bigint(10) NOT NULL,\n" +
            "  `balance` double NOT NULL,\n" +
            "  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,\n" +
            "  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,\n" +
            "  PRIMARY KEY (`id`),\n" +
            "  UNIQUE KEY `user` (`user_id`)\n" +
            ") ENGINE=InnoDB")
    void createTable();

    @SqlUpdate("insert into " + tableName + " (user_id, balance) values (:userId, :balance)")
    void createAccount(@Bind("userId") long userId, @Bind("balance") double balance);

    @RegisterMapper(Account.AccountMapper.class)
    @SqlQuery("SELECT * from accounts where user_id = :userId  ")
    Account getAccountDetails(@Bind("userId") long userId);

    @RegisterMapper(Account.AccountMapper.class)
    @SqlQuery("SELECT a.* from accounts as a join users u on u.id = a.user_id where u.email = :email  ")
    Account getAccountDetailsByEmail(@Bind("email") String email);

    @SqlUpdate("update " + tableName + " set balance = :balance where id = :id")
    void updateAmount(@Bind("id")long id, @Bind("balance") double balance);
}
