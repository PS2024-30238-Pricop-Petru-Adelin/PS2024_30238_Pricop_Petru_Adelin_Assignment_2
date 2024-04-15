package com.olxapplication.controller;

import com.olxapplication.service.CsvReportService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@CrossOrigin
@RequestMapping(value = "/report")
@Setter
@Getter
@AllArgsConstructor
@Validated
@Slf4j
public class CsvReportController {
    private final CsvReportService csvReportService;

    @GetMapping("/generate")
    public ModelAndView generateReport(RedirectAttributes redirectAttributes){
        ModelAndView modelAndView = new ModelAndView("redirect:/announcement/get");
        String msg = csvReportService.writeCSV();
        redirectAttributes.addFlashAttribute("message", msg);
        return modelAndView;
    }
}
