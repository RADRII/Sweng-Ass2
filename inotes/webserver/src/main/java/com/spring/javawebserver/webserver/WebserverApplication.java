package com.spring.javawebserver.webserver;

import java.io.File;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@SpringBootApplication
public class WebserverApplication {
    public static void main(String[] args) {
        SpringApplication.run(WebserverApplication.class, args);
    }

}

@Controller
class ExpressionController {
    public static boolean valid = true; // to see if the expression is valid

    @GetMapping("/")
    public String index(Model model) {
        return "index";
    }

    @PostMapping("/calculate") // when calculate button pressed
    public String calculation(@RequestParam(required = false) String expression) throws Exception {

        if (expression != null && expression.equals("Calculate!")) { // input is not null
            String[] tmp = new String[expression.length()];
            for (int i = 0; i < tmp.length; i++) {
                tmp[i] = "";
            }
            int tmpCounter = 0, charCounter = 0;
            while (charCounter < expression.length()) {
                tmp[tmpCounter] = tmp[tmpCounter] + expression.substring(charCounter, charCounter + 1);
                charCounter++;
                tmpCounter++;
                if (charCounter < expression.length()) {
                    if (expression.charAt(charCounter) >= '0'
                            && expression.charAt(charCounter) <= '9' && tmp[tmpCounter - 1].matches("[0-9]*")) {
                        tmpCounter--;
                    }
                }
            }

            solveFunction(tmp);
            expression = "";
            for (int i = 0; i < tmp.length; i++) {
                expression += tmp[i];
            }
            return expression;
        }

        return "index";
    }

    public static void solveFunction(String[] expr) {
        for (int i = 0; i < expr.length; i++) {
            if (expr[i].contains("*") || expr[i].contains("/")) {
                operatorFunction(expr, i);
            }
        }

        for (int i = 0; i < expr.length; i++) {
            if (expr[i].contains("+") || expr[i].contains("-")) {
                operatorFunction(expr, i);
            }
        }

        for (int i = 0; i < expr.length; i++) {
        }
    }

    public static void operatorFunction(String[] expression, int index) {
        if (!validation(expression)) {
            System.out.println("Invalid string entered!");
            valid = false;
            return;
        }
        String operator = expression[index];
        int before = -1;
        for (int i = index - 1; i >= 0; i--) {
            if (expression[i] != "") {
                before = i;
                i = -1;
            }
        }
        int after = -1;
        for (int i = index + 1; i < expression.length; i++) {
            if (expression[i] != "") {
                after = i;
                i = expression.length;
            }
        }

        if (before >= 0 && after >= 0 && valid) {
            int numberOne = Integer.parseInt(expression[before]);
            int numberTwo = Integer.parseInt(expression[after]);
            String result = "";
            switch (operator) {
                case "*":
                    result = Integer.toString(numberOne * numberTwo);
                    break;

                case "/":
                    if (numberTwo == 0) {
                        valid = false;
                        System.out.println("My maths teacher says if you divide by zero the world explode!");
                    }
                    result = Integer.toString(numberOne / numberTwo);
                    break;

                case "+":
                    result = Integer.toString(numberOne + numberTwo);
                    break;

                case "-":
                    result = Integer.toString(numberOne - numberTwo);
                    break;
            }
            expression[before] = "";
            expression[after] = "";
            expression[index] = result;

        }
    }

    public static boolean validation(String text) {

        if (text.matches("(.*)[^+-_*/](.*)[^0-9][(.*)") || text.matches("[A-Za-z]")) {
            return false;
        }
        return true;

    }

    public static boolean validation(String[] string) {
        String tmp = "";
        for (int i = 0; i < string.length; i++) {
            tmp += string[i];
        }
        tmp.replace(" ", "");

        return validation(tmp);
    }

}
