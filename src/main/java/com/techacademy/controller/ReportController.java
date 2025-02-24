package com.techacademy.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
//import org.springframework.dao.DataIntegrityViolationException;
//import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
//import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.ModelAttribute;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.techacademy.entity.Employee;
import com.techacademy.entity.Report;

//import com.techacademy.constants.ErrorKinds;
//import com.techacademy.constants.ErrorMessage;

//import com.techacademy.entity.Employee;
//import com.techacademy.service.EmployeeService;
import com.techacademy.service.ReportService;
//import com.techacademy.service.UserDetail;
import com.techacademy.service.UserDetail;

@Controller
@RequestMapping("reports")
public class ReportController {

    private final ReportService reportService;

    @Autowired
    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping
    public String showList(Model model, @AuthenticationPrincipal UserDetail userDetail) {
        // 現在ログイン中のユーザー情報から Employee を取得し、Report の検索条件に設定する
        List<Report> reportList = reportService.findReports(userDetail.getEmployee());
        model.addAttribute("listSize", reportList.size());
        model.addAttribute("reportList", reportList);

        return "reports/list";
    }

    @GetMapping(value = {"/{ID}", "/{ID}/"})
    // 日報詳細画面(detail)
    public String showDetail(@PathVariable("ID") Integer id, Model model) {
        model.addAttribute("report", reportService.findById(id));
        return "reports/detail";
    }

    // 日報新規登録画面(create)
    public String showRegister() {
        return "";
    }
    // 日報更新画面(edit)
    public String showEdit() {
        return "";
    }

    // 日報新規追加処理(add)
    public String procAdd() {
        return "";
    }
    // 日報更新処理(update)
    public String procUpdate() {
        return "";
    }
    // 日報削除処理(delete)
     public String procDelete() {
        return "";
    }
}