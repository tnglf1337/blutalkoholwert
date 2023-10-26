package com.example.blutalkoholwert;

import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Controller
public class WebController {

    @GetMapping("/")
    public String index(){
        return "index";
    }

    @PostMapping("/")
    public String result(Model model,
                         @Valid PersonForm personForm,
                         BindingResult bindingResult) {

        String alkoholart = personForm.alkoholart();
        String geschlecht = personForm.geschlecht();
        Double weight = personForm.weight();
        Double menge = personForm.menge();

        double promille = computeBlutalkoholWert(alkoholart, weight, menge, geschlecht);

        double result = round(promille, 2);

        model.addAttribute("promille", result);

        if(bindingResult.hasErrors()) {
            return "error";
        }

        return "index";
    }

    private double computeBlutalkoholWert(String alkoholart, Double weight, Double menge, String geschlecht) {
        double verteilungsfaktor = choseVerteilungsfaktor(geschlecht);
        double alkoholgehaltInGramm = getAlkoholgehaltInGramm(alkoholart, menge);

        return alkoholgehaltInGramm / (weight * verteilungsfaktor);
    }

    private double choseVerteilungsfaktor(String geschlecht) {
        switch (geschlecht) {
            case "m" -> {
                return 0.7;
            }
            case "w" -> {
                return 0.6;
            }
            default -> {
                System.out.println("kein gültiges geschlecht");
                return 1.0;
            }
        }
    }

    private double getAlkoholgehaltInGramm(String alkoholart, Double menge) {
        double a;

        switch (alkoholart) {
            case "bier" -> a = 0.05;
            case "wein" -> a = 0.12;
            case "korn" -> a = 0.32;
            case "vodka" -> a = 0.40;
            case "whiskey" -> a = 0.43;
            default -> {
                a = 0;
                System.out.println("keine gültige alkoholart");
            }
        }

        return a * menge;
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
}
