package com.zorvyn.zorvynfinanceapi.repository;

import com.zorvyn.zorvynfinanceapi.entity.FinancialRecord;
import com.zorvyn.zorvynfinanceapi.entity.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;


@Repository
public interface FinancialRecordRepository extends JpaRepository<FinancialRecord, Long> {

    List<FinancialRecord> findAllByUserId(Long userId);


    @Query("SELECT SUM(f.amount) FROM FinancialRecord f WHERE f.user.id = :userId AND f.type = 'INCOME'")
    BigDecimal getTotalIncomeByUserId(@Param("userId") Long userId);

    @Query("SELECT SUM(f.amount) FROM FinancialRecord f WHERE f.user.id = :userId AND f.type = 'EXPENSE'")
    BigDecimal getTotalExpenseByUserId(@Param("userId") Long userId);

    // Offloading aggregation to the database layer for better performance instead of processing in Java.
    @Query("SELECT (COALESCE(SUM(CASE WHEN f.type = 'INCOME' THEN f.amount ELSE 0 END), 0) - " +
            "COALESCE(SUM(CASE WHEN f.type = 'EXPENSE' THEN f.amount ELSE 0 END), 0)) " +
            "FROM FinancialRecord f WHERE f.user.id = :userId")
    BigDecimal getNetBalanceByUserId(@Param("userId") Long userId);
    List<FinancialRecord> findAllByUserIdAndType(Long userId, TransactionType type);

}