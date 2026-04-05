package com.zorvyn.zorvynfinanceapi.dto;

import java.math.BigDecimal;

public record DashboardSummaryDTO(
        BigDecimal totalIncome,
        BigDecimal totalExpense,
        BigDecimal netBalance
) {
    // We handle nulls in case a user has no records yet
    public DashboardSummaryDTO {
        if (totalIncome == null) totalIncome = BigDecimal.ZERO;
        if (totalExpense == null) totalExpense = BigDecimal.ZERO;
        if (netBalance == null) netBalance = BigDecimal.ZERO;
    }
}