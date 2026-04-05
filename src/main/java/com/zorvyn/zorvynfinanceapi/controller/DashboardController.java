package com.zorvyn.zorvynfinanceapi.controller;

import com.zorvyn.zorvynfinanceapi.dto.DashboardSummaryDTO;
import com.zorvyn.zorvynfinanceapi.service.FinancialRecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final FinancialRecordService recordService;

    // We will secure this later so only ANALYST and ADMIN can view it, or the user themselves
    @PreAuthorize("hasAnyRole('VIEWER', 'ANALYST', 'ADMIN')")
    @GetMapping("/user/{userId}")
    public ResponseEntity<DashboardSummaryDTO> getDashboardSummary(@PathVariable Long userId) {
        return ResponseEntity.ok(recordService.getDashboardSummary(userId));
    }
}