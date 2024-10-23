package org.example.ecommerce;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableJpaRepositories
@EnableTransactionManagement
@EnableMongoRepositories
public class EcommerceApplication {
    public static void main(String[] args) {
        Dotenv dotenv = Dotenv.load();
        System.setProperty("SQL_DB_URL", dotenv.get("SQL_DB_URL"));
        System.setProperty("SQL_DB_USERNAME", dotenv.get("SQL_DB_USERNAME"));
        System.setProperty("SQL_DB_PASSWORD", dotenv.get("SQL_DB_PASSWORD"));
        SpringApplication.run(EcommerceApplication.class, args);
        System.setProperty("MONGO_DATABASE", dotenv.get("MONGO_DATABASE"));
        System.setProperty("MONGO_USER", dotenv.get("MONGO_USER"));
        System.setProperty("MONGO_PASSWORD", dotenv.get("MONGO_PASSWORD"));
        System.setProperty("MONGO_CLUSTER", dotenv.get("MONGO_CLUSTER"));
    }

}
