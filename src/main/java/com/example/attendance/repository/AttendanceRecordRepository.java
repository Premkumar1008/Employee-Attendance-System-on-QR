package com.example.attendance.repository;

import com.example.attendance.model.AttendanceRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AttendanceRecordRepository extends JpaRepository<AttendanceRecord, Long> {
    List<AttendanceRecord> findByEmployeeEmployeeCodeOrderByTimestampDesc(String employeeCode);
}
