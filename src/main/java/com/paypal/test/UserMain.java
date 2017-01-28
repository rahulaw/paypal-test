package com.paypal.test;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Stage;
import com.paypal.test.filters.AuthenticationFilter;
import com.paypal.test.resources.AccountResource;
import io.dropwizard.Application;
import io.dropwizard.assets.AssetsBundle;
import io.dropwizard.jdbi.DBIFactory;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.skife.jdbi.v2.DBI;
import com.paypal.test.resources.AuthResource;
import com.paypal.test.resources.UserResource;

/**
 * Created by rahulaw on 28/01/17.
 */
public class UserMain extends Application<UserConfiguration> {

    @Override
    public void initialize(Bootstrap<UserConfiguration> bootstrap) {
        bootstrap.addBundle(new AssetsBundle("/public/", "/" , "index.html"));
        bootstrap.addBundle(new AssetsBundle("/public/assets/css", "/css", null, "css"));
    }

    @Override
    public void run(UserConfiguration config, Environment environment) throws Exception {
        final DBIFactory factory = new DBIFactory();

        final DBI masterJdbi = factory.build(environment, config.getMasterDataSourceFactory(), "test-master");
        final DBI slaveJdbi = factory.build(environment, config.getSlaveDataSourceFactory(), "test-slave");

        Injector injector = Guice.createInjector(Stage.DEVELOPMENT, new UserModule(config, masterJdbi, slaveJdbi));

        environment.jersey().register(injector.getInstance(UserResource.class));
        environment.jersey().register(injector.getInstance(AuthResource.class));
        environment.jersey().register(injector.getInstance(AccountResource.class));
        environment.jersey().register(injector.getInstance(AuthenticationFilter.class));

    }

    public static void main(String args[]) throws Exception {
        new UserMain().run(args);
    }
}