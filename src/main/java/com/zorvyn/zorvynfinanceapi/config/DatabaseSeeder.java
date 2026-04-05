package com.zorvyn.zorvynfinanceapi.config;

import com.zorvyn.zorvynfinanceapi.entity.FinancialRecord;
import com.zorvyn.zorvynfinanceapi.entity.Role;
import com.zorvyn.zorvynfinanceapi.entity.TransactionType;
import com.zorvyn.zorvynfinanceapi.entity.User;
import com.zorvyn.zorvynfinanceapi.repository.FinancialRecordRepository;
import com.zorvyn.zorvynfinanceapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DatabaseSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final FinancialRecordRepository recordRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        if (userRepository.count() == 0) {

            // Create Users
            User admin = User.builder().username("admin").password(passwordEncoder.encode("password")).role(Role.ADMIN).active(true).build();
            User analyst = User.builder().username("analyst").password(passwordEncoder.encode("password")).role(Role.ANALYST).active(true).build();
            User viewer = User.builder().username("viewer").password(passwordEncoder.encode("password")).role(Role.VIEWER).active(true).build();

            userRepository.saveAll(List.of(admin, analyst, viewer));

            // Create Financial Records for Admin
            FinancialRecord income1 = FinancialRecord.builder().amount(new BigDecimal("5000.00")).type(TransactionType.INCOME).category("Freelance API Development").date(LocalDate.now().minusDays(5)).user(admin).deleted(false).build();
            FinancialRecord expense1 = FinancialRecord.builder().amount(new BigDecimal("1200.00")).type(TransactionType.EXPENSE).category("Server Hosting Fees").date(LocalDate.now().minusDays(2)).user(admin).deleted(false).build();

            // Create Financial Records for Analyst
            FinancialRecord income2 = FinancialRecord.builder().amount(new BigDecimal("3000.00")).type(TransactionType.INCOME).category("Freelance").date(LocalDate.now().minusDays(10)).user(analyst).deleted(false).build();

            recordRepository.saveAll(List.of(income1, expense1, income2));

            System.out.println("DATABASE SEEDED: Use username 'admin', 'analyst', or 'viewer' with password 'password' to login.");
        }
    }
}