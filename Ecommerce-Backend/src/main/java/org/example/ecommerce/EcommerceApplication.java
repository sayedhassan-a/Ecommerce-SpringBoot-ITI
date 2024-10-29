package org.example.ecommerce;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableJpaRepositories
@EnableTransactionManagement
@EnableMongoRepositories
@EnableScheduling
@EnableWebSecurity
public class EcommerceApplication {

    public static void main(String[] args) {
        Dotenv dotenv = Dotenv.load(); // Load .env file
        System.setProperty("SQL_DB_URL", dotenv.get("SQL_DB_URL"));
        System.setProperty("SQL_DB_USERNAME", dotenv.get("SQL_DB_USERNAME"));
        System.setProperty("SQL_DB_PASSWORD", dotenv.get("SQL_DB_PASSWORD"));
        System.setProperty("PAYMENT_SECRET_KEY", dotenv.get("PAYMENT_SECRET_KEY"));
        System.setProperty("PAYMENT_PUBLIC_KEY", dotenv.get("PAYMENT_PUBLIC_KEY"));
        System.setProperty("PAYMENT_INTENTION_URL", dotenv.get("PAYMENT_INTENTION_URL"));
        System.setProperty("PAYMENT_CHECKOUT_URL", dotenv.get("PAYMENT_CHECKOUT_URL"));
        System.setProperty("MONGO_DATABASE", dotenv.get("MONGO_DATABASE"));
        System.setProperty("MONGO_USER", dotenv.get("MONGO_USER"));
        System.setProperty("MONGO_PASSWORD", dotenv.get("MONGO_PASSWORD"));
        System.setProperty("MONGO_CLUSTER", dotenv.get("MONGO_CLUSTER"));
        System.setProperty("UI_BASE_URL", dotenv.get("UI_BASE_URL"));
        System.setProperty("TRANSACTION_PROCESSED_CALLBACK", dotenv.get("TRANSACTION_PROCESSED_CALLBACK"));
        SpringApplication.run(EcommerceApplication.class, args);
    }
}
