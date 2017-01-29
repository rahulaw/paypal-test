package com.paypal.test.dao;

import com.paypal.test.entities.TransactionHistory;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;

import java.util.List;

/**
 * Created by rahulaw on 28/01/17.
 */
public interface TransactionHistoryDAO {

    String tableName = "transactions_history";

    @SqlUpdate("CREATE TABLE `transactions_history` (\n" +
            "  `id` bigint(10) NOT NULL AUTO_INCREMENT,\n" +
            "  `account_id` bigint(10) NOT NULL,\n" +
            "  `message` varchar(255) DEFAULT NULL,\n" +
            "  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,\n" +
            "  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,\n" +
            "  PRIMARY KEY (`id`)\n" +
            ") ENGINE=InnoDB")
    void createTable();


    @RegisterMapper(TransactionHistory.TransactionHistoryMapper.class)
    @SqlQuery("SELECT t.* from transactions_history as t join accounts a on a.id = t.account_id where a.user_id = :userId  ")
    List<TransactionHistory> getHistoryForUser(@Bind("userId") long userId);

    @SqlUpdate("insert into " + tableName + " (account_id, message) values (:accountId, :message) ")
    void add(@Bind("accountId") long accountId, @Bind("message") String message);

}
