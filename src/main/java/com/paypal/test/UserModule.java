package com.paypal.test;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.paypal.test.dao.AccountDAO;
import com.paypal.test.dao.TokenDAO;
import com.paypal.test.dao.TransactionHistoryDAO;
import com.paypal.test.dao.UserDAO;
import com.paypal.test.dao.core.SessionFactoryInitializer;
import org.hibernate.SessionFactory;
import org.skife.jdbi.v2.DBI;

/**
 * Created by rahulaw on 28/01/17.
 */
public class UserModule extends AbstractModule {

    private UserConfiguration config;
    private DBI masterJdbi ;
    private DBI slaveJdbi ;

    public UserModule(UserConfiguration config, DBI masterJdbi, DBI slaveJdbi) {
        this.config = config;
        this.masterJdbi = masterJdbi;
        this.slaveJdbi = slaveJdbi;
    }

    @Override
    protected void configure() {

    }

    @Provides
    @Singleton
    SessionFactory makeSessionFactory(SessionFactoryInitializer sessionFactoryInitializer) {
        SessionFactory sessionFactory = sessionFactoryInitializer.getSessionFactory();
        return sessionFactory;
    }

    @Provides
    @Singleton
    SessionFactoryInitializer makeSessionFactoryInitializer(UserConfiguration databaseConfig) {
        return new SessionFactoryInitializer(databaseConfig);
    }


    @Provides
    @Singleton
    UserDAO provideUserDAO() {
        return masterJdbi.onDemand(UserDAO.class);
    }

    @Provides
    @Singleton
    TokenDAO provideTokenDAO() {
        return masterJdbi.onDemand(TokenDAO.class);
    }

    @Provides
    @Singleton
    AccountDAO provideAccountDAO() {
        return masterJdbi.onDemand(AccountDAO.class);
    }

    @Provides
    @Singleton
    TransactionHistoryDAO provideTransactionHistoryDAO() {
        return masterJdbi.onDemand(TransactionHistoryDAO.class);
    }

}