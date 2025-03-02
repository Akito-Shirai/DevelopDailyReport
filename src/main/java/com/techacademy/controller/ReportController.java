package com.techacademy.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
//import org.springframework.dao.DataIntegrityViolationException;
//import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
//import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.ModelAttribute;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.techacademy.constants.ErrorKinds;
import com.techacademy.constants.ErrorMessage;
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
    @GetMapping(value = "/add")
    public String showRegister(Model model, @ModelAttribute Report report, @AuthenticationPrincipal UserDetail userDetail) {
        String name = userDetail.getEmployee().getName();
        model.addAttribute("name", name);
        return "reports/new";
    }
    // 日報更新画面(edit)
    @GetMapping(value = "/{ID}/update")
    public String showEdit(@PathVariable("ID") Integer id, Model model) {
        model.addAttribute("report", reportService.findById(id));
        return "reports/update";
    }

    // 日報新規追加処理(add)
    @PostMapping(value = "/add")
    public String procAdd(@Validated Report report, BindingResult res, Model model, @AuthenticationPrincipal UserDetail userDetail) {
        // employeeフィールドに認証ユーザーのEmployeeをセット
        Employee currentEmployee = userDetail.getEmployee();
        report.setEmployee(currentEmployee);

        // バリデーションエラーチェック
        String name = currentEmployee.getName();
        if(res.hasErrors()) {
            model.addAttribute("name", name);
            return "reports/new";
        }

        // 更新処理でもsaveを使用するため、日付チェックをコントローラ上に実装
        boolean isRegisteredDate = reportService.hasReportDate(currentEmployee, report.getReportDate());
        if(isRegisteredDate) {
            res.rejectValue(
                "reportDate",
                ErrorMessage.getErrorName(ErrorKinds.DATECHECK_ERROR),
                ErrorMessage.getErrorValue(ErrorKinds.DATECHECK_ERROR)
            );
            model.addAttribute("name", name);
            return "reports/new";
        }

        // 保存処理
        ErrorKinds result = reportService.save(report);
        // result確認
        if (ErrorMessage.contains(result)) {
//            String name = userDetail.getEmployee().getName();
            model.addAttribute("name", name);
            model.addAttribute(ErrorMessage.getErrorName(result), ErrorMessage.getErrorValue(result));
            return "reports/new";
        }
        return "redirect:/reports";
    }

    // 日報更新処理(update)
    @PostMapping(value = "/{ID}/update")
    public String procUpdate(@Validated @ModelAttribute("report") Report report, BindingResult res, @PathVariable("ID") Integer id, Model model) {
        // バリデーションエラーチェック
        if (res.hasErrors()) {
            return "reports/update";
        }

        // 更新処理実行
        ErrorKinds result = reportService.update(report);
        if(ErrorMessage.contains(result)) {
            model.addAttribute(ErrorMessage.getErrorName(result), ErrorMessage.getErrorValue(result));
            return "reports/update";
        }
        return "redirect:/reports";
    }
    // 日報削除処理(delete)
    @PostMapping(value = "/{ID}/delete")
     public String procDelete(@PathVariable("ID") Integer id, @AuthenticationPrincipal UserDetail userDetail, Model model) {
        ErrorKinds result = reportService.delete(id);
        if(ErrorMessage.contains(result)) {
            model.addAttribute(ErrorMessage.getErrorName(result), ErrorMessage.getErrorValue(result));
            model.addAttribute("report", reportService.findById(id));
            return showDetail(id, model);
        }
        return "redirect:/reports";
    }
}