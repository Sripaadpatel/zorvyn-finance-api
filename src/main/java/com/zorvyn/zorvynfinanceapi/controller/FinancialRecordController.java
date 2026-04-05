package com.zorvyn.zorvynfinanceapi.controller;

import com.zorvyn.zorvynfinanceapi.entity.FinancialRecord;
import com.zorvyn.zorvynfinanceapi.service.FinancialRecordService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize; // IMPORT THIS
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/records")
@RequiredArgsConstructor
public class FinancialRecordController {

    private final FinancialRecordService recordService;

    // Viewers, Analysts, and Admins can all view records
    @PreAuthorize("hasAnyRole('VIEWER', 'ANALYST', 'ADMIN')")
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<FinancialRecord>> getAllRecords(
            @PathVariable Long userId,
            @RequestParam(required = false) String type) { // Added RequestParam
        return ResponseEntity.ok(recordService.getAllRecordsForUser(userId, type));
    }

    // Only Admins (and maybe Analysts if you choose) can create/update/delete
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/user/{userId}")
    public ResponseEntity<FinancialRecord> createRecord(
            @PathVariable Long userId,
            @Valid @RequestBody FinancialRecord record) {
        return new ResponseEntity<>(recordService.createRecord(userId, record), HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{recordId}")
    public ResponseEntity<FinancialRecord> updateRecord(
            @PathVariable Long recordId,
            @Valid @RequestBody FinancialRecord record) {
        return ResponseEntity.ok(recordService.updateRecord(recordId, record));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{recordId}")
    public ResponseEntity<Void> deleteRecord(@PathVariable Long recordId) {
        recordService.deleteRecord(recordId);
        return ResponseEntity.noContent().build();
    }
}