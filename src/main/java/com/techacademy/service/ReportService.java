package com.techacademy.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Service;

import com.techacademy.constants.ErrorKinds;
import com.techacademy.entity.Employee;
import com.techacademy.entity.Report;
import com.techacademy.repository.ReportRepository;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ReportService {

    private final ReportRepository reportRepository;

    public ReportService(ReportRepository reportRepository) {
        this.reportRepository = reportRepository;
    }

    // レポート一覧表示処理
    // employeeを渡すように修正
    public List<Report> findReports(Employee employee) {
        String code = employee.getCode();
        Employee.Role role = employee.getRole();

        if(role == Employee.Role.ADMIN) {
            return reportRepository.findAll();
        }else {
            return reportRepository.findByDeleteFlgFalseAndEmployee_Code(code);
        }
    }

    // 1件を検索
    public Report findById(Integer id) {
        // findByIdで検索
        Optional<Report> option = reportRepository.findById(id);
        // 取得できなかった場合はnullを返す
        Report report = option.orElse(null);
        return report;
    }

    // レポート保存
    @Transactional
    public ErrorKinds save(Report report) {
        reportRepository.save(report);
        return ErrorKinds.SUCCESS;
    }

    // 日報更新処理
    public ErrorKinds update(Report report) {
        // 日報の情報を取得
        Report existingReport = findById(report.getId());
        if(existingReport == null) {
            return ErrorKinds.NOT_FOUND;
        }

        LocalDateTime now = LocalDateTime.now();
        existingReport.setReportDate(report.getReportDate());
        existingReport.setTitle(report.getTitle());
        existingReport.setContent(report.getContent());

        reportRepository.save(existingReport);
        return ErrorKinds.SUCCESS;
    }

    // 日報削除
    @Transactional
    public ErrorKinds delete(Integer id) {


        Report report = findById(id);
        LocalDateTime now = LocalDateTime.now();
        report.setUpdatedAt(now);
        report.setDeleteFlg(true);

        return ErrorKinds.SUCCESS;
    }

}
