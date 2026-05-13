package com.example.attendance.controller;

import com.example.attendance.model.AttendanceRecord;
import com.example.attendance.model.Employee;
import com.example.attendance.repository.AttendanceRecordRepository;
import com.example.attendance.repository.EmployeeRepository;
import com.example.attendance.service.QRCodeService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.List;

@Controller
public class AttendanceController {

    private final EmployeeRepository employeeRepository;
    private final AttendanceRecordRepository attendanceRecordRepository;
    private final QRCodeService qrCodeService;

    public AttendanceController(EmployeeRepository employeeRepository,
                                AttendanceRecordRepository attendanceRecordRepository,
                                QRCodeService qrCodeService) {
        this.employeeRepository = employeeRepository;
        this.attendanceRecordRepository = attendanceRecordRepository;
        this.qrCodeService = qrCodeService;
    }

    @GetMapping({"/", "/index"})
    public String index(Model model) {
        List<Employee> employees = employeeRepository.findAll();
        model.addAttribute("employees", employees);
        return "index";
    }

    @GetMapping("/employees")
    public String employees(Model model) {
        model.addAttribute("employees", employeeRepository.findAll());
        return "employees";
    }

    @GetMapping("/attendance")
    public String attendancePage(@RequestParam(required = false) String code, Model model) {
        model.addAttribute("attendanceRecords", attendanceRecordRepository.findAll());
        model.addAttribute("code", code == null ? "" : code);
        return "attendance";
    }

    @GetMapping("/scan")
    public String scanPage(Model model) {
        return "scan";
    }

    @PostMapping("/employees/add")
    public String addEmployee(@ModelAttribute Employee employee, RedirectAttributes redirectAttributes) {
        if (employee.getEmployeeCode() == null || employee.getEmployeeCode().isBlank()) {
            redirectAttributes.addFlashAttribute("error", "Employee code is required.");
            return "redirect:/employees";
        }

        employeeRepository.save(employee);
        redirectAttributes.addFlashAttribute("success", "Employee added successfully.");
        return "redirect:/employees";
    }

    @GetMapping("/employees/qr")
    public String showEmployeeQr(@RequestParam String code, Model model) {
        Employee employee = employeeRepository.findByEmployeeCode(code).orElse(null);
        if (employee == null) {
            model.addAttribute("error", "Employee not found.");
            return "employees";
        }

        String qrValue = "ATTENDANCE:" + employee.getEmployeeCode();
        model.addAttribute("employee", employee);
        model.addAttribute("qrImage", qrCodeService.generateQRCode(qrValue, 280, 280));
        return "employees";
    }

    @PostMapping("/attendance/mark")
    public String markAttendance(@RequestParam String qrText, RedirectAttributes redirectAttributes, HttpServletRequest request) {
        String code = qrText;
        if (qrText.startsWith("ATTENDANCE:")) {
            code = qrText.substring("ATTENDANCE:".length());
        }

        Employee employee = employeeRepository.findByEmployeeCode(code).orElse(null);
        if (employee == null) {
            redirectAttributes.addFlashAttribute("error", "QR code is invalid or employee not found.");
            return "redirect:/attendance";
        }

        AttendanceRecord record = new AttendanceRecord(employee, LocalDateTime.now(), "Present");
        attendanceRecordRepository.save(record);
        redirectAttributes.addFlashAttribute("success", "Attendance marked for " + employee.getName() + ".");
        return "redirect:/attendance";
    }
}
