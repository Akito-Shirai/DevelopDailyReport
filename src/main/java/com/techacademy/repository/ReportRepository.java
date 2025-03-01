package com.techacademy.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.techacademy.entity.Employee;
import com.techacademy.entity.Report;

public interface ReportRepository extends JpaRepository<Report, Integer> {
    // ユーザ名で検索
    List<Report> findByEmployee_Code(String code);
    // 削除フラグの対応を実装する
    List<Report> findByDeleteFlgFalseAndEmployee_Code(String code);
    // ログイン中の従業員&入力された日付が既に存在するかどうかを確認
    boolean existsByEmployeeAndReportDate(Employee employee, LocalDate reportDate);

}
