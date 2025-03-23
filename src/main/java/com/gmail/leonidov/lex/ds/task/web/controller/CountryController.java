package com.gmail.leonidov.lex.ds.task.web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.gmail.leonidov.lex.ds.task.web.model.CountryResponse;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.gmail.leonidov.lex.ds.task.domain.service.PhoneNumberService;

@Controller
@RequiredArgsConstructor
public class CountryController {

    private final PhoneNumberService phoneNumberService;

    @GetMapping
    public String index() {
        return "index";
    }

    @GetMapping("/search")
    public String search(@RequestParam String phone, RedirectAttributes redirectAttributes) {
        CountryResponse country = phoneNumberService.getCountryByPhoneNumber(phone);
        redirectAttributes.addFlashAttribute("country", country);
        return "redirect:/";
    }

}
