package com.zorvyn.zorvynfinanceapi.service;

import com.zorvyn.zorvynfinanceapi.dto.DashboardSummaryDTO;
import com.zorvyn.zorvynfinanceapi.entity.FinancialRecord;
import com.zorvyn.zorvynfinanceapi.entity.TransactionType;
import com.zorvyn.zorvynfinanceapi.entity.User;
import com.zorvyn.zorvynfinanceapi.exception.ResourceNotFoundException;
import com.zorvyn.zorvynfinanceapi.repository.FinancialRecordRepository;
import com.zorvyn.zorvynfinanceapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FinancialRecordService {

    private final FinancialRecordRepository recordRepository;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public List<FinancialRecord> getAllRecordsForUser(Long userId, String type) {
        if (type != null) {
            return recordRepository.findAllByUserIdAndType(userId, TransactionType.valueOf(type.toUpperCase()));
        }
        return recordRepository.findAllByUserId(userId);
    }

    @Transactional(readOnly = true)
    public DashboardSummaryDTO getDashboardSummary(Long userId) {

        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("User not found with id: " + userId);
        }

        BigDecimal income = recordRepository.getTotalIncomeByUserId(userId);
        BigDecimal expense = recordRepository.getTotalExpenseByUserId(userId);
        BigDecimal net = recordRepository.getNetBalanceByUserId(userId);

        return new DashboardSummaryDTO(income, expense, net);
    }

    @Transactional
    public FinancialRecord createRecord(Long userId, FinancialRecord record) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        record.setUser(user);
        return recordRepository.save(record);
    }

    @Transactional
    public FinancialRecord updateRecord(Long recordId, FinancialRecord updatedRecord) {
        FinancialRecord existingRecord = recordRepository.findById(recordId)
                .orElseThrow(() -> new ResourceNotFoundException("Record not found with id: " + recordId));

        existingRecord.setAmount(updatedRecord.getAmount());
        existingRecord.setType(updatedRecord.getType());
        existingRecord.setCategory(updatedRecord.getCategory());
        existingRecord.setDate(updatedRecord.getDate());
        existingRecord.setNotes(updatedRecord.getNotes());

        return recordRepository.save(existingRecord);
    }

    @Transactional
    public void deleteRecord(Long recordId) {
        if (!recordRepository.existsById(recordId)) {
            throw new ResourceNotFoundException("Record not found with id: " + recordId);
        }

        recordRepository.deleteById(recordId);
    }
}