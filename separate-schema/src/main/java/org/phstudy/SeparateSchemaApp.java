package org.phstudy;

import org.eclipse.persistence.config.BatchWriting;
import org.eclipse.persistence.config.PersistenceUnitProperties;
import org.phstudy.model.Product;
import org.phstudy.model.ProductServiceBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.AbstractJpaVendorAdapter;
import org.springframework.orm.jpa.vendor.EclipseLinkJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Created by study on 11/14/14.
 */
@SpringBootApplication
public class SeparateSchemaApp implements CommandLineRunner {
    private static String PU = "pu-eclipselink";

    protected AbstractJpaVendorAdapter createJpaVendorAdapter() {
        return new EclipseLinkJpaVendorAdapter();
    }

    @Bean
    public EntityManagerFactory entityManagerFactory(DataSource dataSource) {
        LocalContainerEntityManagerFactoryBean ret = new LocalContainerEntityManagerFactoryBean();
        ret.setDataSource(dataSource);
        ret.setJpaVendorAdapter(createJpaVendorAdapter());
        ret.setPackagesToScan("org.phstudy");
        ret.getJpaPropertyMap().putAll(initJpaProperties());
        ret.afterPropertiesSet();
        return ret.getObject();
        // return new TenantAwareEntityManagerFactory(ret.getObject());
    }

    private final Map<String, ?> initJpaProperties() {
        final Map<String, Object> ret = new HashMap<>();
        // Add any JpaProperty you are interested in and is supported by your Database and JPA implementation
        ret.put(PersistenceUnitProperties.BATCH_WRITING, BatchWriting.JDBC);
        ret.put(PersistenceUnitProperties.WEAVING, "false");
        ret.put(PersistenceUnitProperties.MULTITENANT_PROPERTY_DEFAULT, "default_tenant");
        return ret;
    }

    @Bean
    public PlatformTransactionManager transactionManager() {
        final JpaTransactionManager transactionManager = new TenantAwareJpaTransactionManager();
        return transactionManager;
    }

    public static void main(String[] args) {
        // # 1. create a postgres docker container and run on port 7432:
        // docker container rm mycont -f
        // docker run --name mycont -p 7432:5432 --detach postgres
        // docker exec -i -u postgres mycont createdb mydb
        // docker exec -i -u postgres mycont psql -c "alter user postgres with encrypted password 'mypass';"
        // docker exec -i -u postgres mycont psql -c "grant all privileges on database mydb to postgres;"

        // # 2. view contents of database
        // docker exec -it mycont psql -U postgres -d mydb
        // select nspname from pg_catalog.pg_namespace

        SpringApplication.run(SeparateSchemaApp.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

        try {
            System.out.println("======================================");

            TenantSchemaHolder.setTenantSchema("JCConf");
            Product p1 = productServiceBean.getMyProduct(1000);
            System.out.println(p1);
            System.out.println(p1.getName().contains("JCConf") ? "PASS" : "FAIL");

            TenantSchemaHolder.setTenantSchema("default_tenant");
            Product p2 = productServiceBean.getMyProduct(1000);
            System.out.println(p2);
            System.out.println(p2.getName().contains("default_tenant") ? "PASS" : "FAIL");
        } finally {
            System.out.println("======================================");
        }
    }

    @Autowired
    ProductServiceBean productServiceBean;
}
