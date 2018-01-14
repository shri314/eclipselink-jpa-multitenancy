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

        main_working(args);
    }

    @Override
    public void run(String... args) throws Exception {
    }


    public static void main_working(String[] args) {

        try {
            System.out.println("======================================");
        /*
            ----- Caveat -----
            Separate Schema do not support DDL generation, and it must be created manually.
            The schema definition is in resources/sql/init.sql file.
         */

            {
                // set tenant in EntityManagerFactory Level
                Properties properties = getPuProperties();
                properties.put(PersistenceUnitProperties.MULTITENANT_PROPERTY_DEFAULT, "JCConf");
                properties.put(PersistenceUnitProperties.SESSION_NAME, "multi-tenant-JCConf");
                EntityManagerFactory factory = Persistence.createEntityManagerFactory(PU, properties);
                EntityManager em = factory.createEntityManager();

                Product p = em.find(Product.class, 1000);
                System.out.println(p);
                System.out.println(p.getName().contains("JCConf") ? "PASS" : "FAIL");

                em.close();
            }

            {
                // set tenant in EntityManagerFactory Level
                Properties properties = getPuProperties();
                properties.put(PersistenceUnitProperties.MULTITENANT_PROPERTY_DEFAULT, "default_tenant");
                properties.put(PersistenceUnitProperties.SESSION_NAME, "multi-tenant-default_tenant");
                EntityManagerFactory factory = Persistence.createEntityManagerFactory(PU, properties);
                EntityManager em = factory.createEntityManager();

                Product p = em.find(Product.class, 1000);
                System.out.println(p);
                System.out.println(p.getName().contains("default_tenant") ? "PASS" : "FAIL");

                em.close();
            }
        } finally {
            System.out.println("======================================");
        }
    }

    private static Properties getPuProperties() {
        Properties properties = new Properties();

        properties.put("javax.persistence.jdbc.driver", "org.postgresql.Driver");
        properties.put("javax.persistence.jdbc.url", "jdbc:postgresql://postgres:7432/mydb");
        properties.put("javax.persistence.jdbc.user", "postgres");
        properties.put("javax.persistence.jdbc.password", "mypass");
        properties.put("javax.persistence.database-product-name", "POSTGRESQL");
        properties.put("javax.persistence.sql-load-script-source", "sql/init.sql");
        // properties.put(PersistenceUnitProperties.LOGGING_LEVEL, SessionLog.FINE_LABEL);

        return properties;
    }
}
