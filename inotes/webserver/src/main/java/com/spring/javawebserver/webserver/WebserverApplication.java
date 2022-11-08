package com.spring.javawebserver.webserver;

import java.io.File;
import java.text.DecimalFormat;
import java.util.*;

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
    private static final DecimalFormat df = new DecimalFormat("0.000");

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
                if (expression.length() - charCounter > 5 &&
                        ((expression.substring(charCounter, charCounter + 4).equals("exp(")
                                || expression.substring(charCounter, charCounter + 4).equals("log(")))) {
                    if (expression.substring(charCounter, charCounter + 4).equals("exp(")) {
                        tmp[tmpCounter] = "exp";
                        charCounter += 3;
                    }
                    if (expression.substring(charCounter, charCounter + 4).equals("log(")) {
                        tmp[tmpCounter] = "log";
                        charCounter += 3;
                    }
                    System.out.println(expression.charAt(charCounter));
                } else if (expression.charAt(charCounter) == ')') {
                    charCounter++;
                } else {
                    tmp[tmpCounter] = tmp[tmpCounter] + expression.substring(charCounter, charCounter + 1);
                }

                charCounter++;
                System.out.println(tmp[tmpCounter]);
                tmpCounter++;
                if (charCounter < expression.length()) {
                    if (expression.charAt(charCounter) >= '0'
                            && expression.charAt(charCounter) <= '9'
                            && (tmp[tmpCounter - 1].matches("[0-9]*")
                                    || tmp[tmpCounter - 1].matches("[0-9]*[.][0-9]*"))) {
                        tmpCounter--;
                    } else if (expression.charAt(charCounter) == '.' && tmp[tmpCounter - 1].matches("[0-9]*")) {
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
            if (expr[i].contains("log") || expr[i].contains("exp"))
                operatorFunction(expr, i);
        }
    }

    public static void operatorFunction(String[] expression, int index) {
        if (!validation(expression)) {
            System.out.println("Invalid string entered!");
            valid = false;
            return;
        }

        // evaluating log and exp
        ArrayList<String> topLevelCheck = new ArrayList<String>(Arrays.asList(expression));
        if (topLevelCheck.contains("log")) {
            int expIndex = topLevelCheck.indexOf("log");
            String result = "";
            result = Double.toString(Math.log(Double.parseDouble(expression[expIndex + 1])));
            expression[expIndex] = result;
            expression[expIndex + 1] = "";
        }
        if (topLevelCheck.contains("exp")) {
            int expIndex = topLevelCheck.indexOf("exp");
            String result = "";
            result = Double.toString(Math.exp(Double.parseDouble(expression[expIndex + 1])));
            expression[expIndex] = result;
            expression[expIndex + 1] = "";
        }

        String operator = expression[index];
        int before = -1;
        for (int i = index - 1; i >= 0; i--) {
            if (!Objects.equals(expression[i], "")) {
                before = i;
                i = -1;
            }
        }
        int after = -1;
        for (int i = index + 1; i < expression.length; i++) {
            if (!Objects.equals(expression[i], "")) {
                after = i;
                i = expression.length;
            }
        }

        if (before >= 0 && after >= 0 && valid) {
            double numberOne = Double.parseDouble(expression[before]);
            numberOne += 0.000;
            double numberTwo = Double.parseDouble(expression[after]);
            String result = "";
            switch (operator) {
                case "*":
                    result = Double.toString(numberOne * numberTwo);
                    break;

                case "/":
                    if (numberTwo == 0) {
                        valid = false;
                        System.out.println("My maths teacher says if you divide by zero the world explode!");
                    }
                    result = Double.toString(numberOne / numberTwo);
                    break;

                case "+":
                    result = Double.toString(numberOne + numberTwo);
                    break;

                case "-":
                    result = Double.toString(numberOne - numberTwo);
                    break;
                case "exp":
                    result = Double.toString(Math.exp(numberTwo));
                    break;
                case "log":
                    result = Double.toString(Math.log(numberTwo));
                    break;
            }
            if (operator.equals("log") || operator.equals("exp")) {
                expression[before] = Double.toString(numberOne);
            } else {
                expression[before] = "";
            }
            expression[after] = "";
            expression[index] = result;

        }
    }

    public static boolean validation(String text) {

        if (text.matches("(.*)[^+-_*/](.*)[^0-9](.*)") || text.matches("[A-Za-z]") ||
                text.matches("(.*)[log(](.*)[)](.*)") || text.matches("(.*)[exp(](.*)[)](.*)")) {
            return true;
        }
        return false;
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
