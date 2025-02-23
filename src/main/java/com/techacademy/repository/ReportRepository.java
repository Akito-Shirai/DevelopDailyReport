package com.techacademy.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.techacademy.entity.Employee;
import com.techacademy.entity.Report;

public interface ReportRepository extends JpaRepository<Report, Integer> {
    // ユーザ名で検索
//    List<Report> findByEmployee(Employee employee);
//    List<Report> findByEmployee_Name(String name);
    List<Report> findByEmployee_Code(String code);

}
