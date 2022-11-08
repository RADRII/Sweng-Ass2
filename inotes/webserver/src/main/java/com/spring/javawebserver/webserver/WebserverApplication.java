package com.spring.javawebserver.webserver;

import java.io.File;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.ArrayList;

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
    public String res="---";

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("expression", res);
        return "index";
    }

    @PostMapping("/calculate") // when calculate button pressed
    public String calculation(@RequestParam String description,
                              @RequestParam(required = false) String expression,
                              Model model) throws Exception {

            String[] toEval = splitString(description);
            String resultStr = result(toEval);
            model.addAttribute("answer", resultStr);
            res = resultStr;
            //return "redirect:/";

        return "index";
    }

    public static String[] splitString (String expression) { //takes a string (expression) and splits it into a String array = number, operator, number, operator
        ArrayList<String> res = new ArrayList<String>();
        int resIndex = 0;
        String temp = "";
        int lastOpIndex = 0;
        for(int i=0;i<expression.length();i++) {
            char current = expression.charAt(i);
            if(i==expression.length()-1) { //last char
                if(Character.isDigit(current)) {
                    temp=temp+current; //append to temp
                    res.add(temp);
                    resIndex++;
                }
                else {
                    System.err.println("Error: invalid character input detected.");
                    System.exit(1);
                }
            }
            else if (Character.isDigit(current)) {
                temp=temp+current; //append digit to temp string
            }
            else if (current=='*'||current=='-'||current=='+') { //if operator
                if (lastOpIndex==(i-1)&&lastOpIndex!=0) { //duplicate check
                    System.err.println("Error: duplicate operation character input detected.");
                    System.exit(1);
                }
                res.add(temp);
                resIndex++;
                temp = "";
                res.add(Character.toString(current));
                resIndex++;
                lastOpIndex = i;
            }
            else {
                System.err.println("Error: invalid input detected.");
                System.exit(1);
            }
        }
        String[] res1=new String[res.size()];
        res1=res.toArray(res1);
        return res1;
    }

    public static String result (String[] splitExpression) {
        String result = "";
        boolean complete = false;
        String[] temp = splitExpression;
        int idx=0;
        int num1 = Integer.parseInt(temp[idx]); //first num
        temp[idx] = null;
        String nextOperator = "";
        while (!complete) {
            int nextNum = 0;
            boolean mulIsNextOperator = false;
            int num2 = Integer.parseInt(temp[idx+2]); //second num
            temp[idx+2] = null;
            String operator = temp[idx+1];
            temp[idx+1] = null;
            //System.out.println("Num1: " + num1 + "num2: " + num2 + "operator: " +operator);
            int res = num1;

            if(idx<=temp.length-4) {

                nextOperator = temp[idx+3];
                if (nextOperator.equals("*")) {
                    mulIsNextOperator = true;
                    nextNum = Integer.parseInt(temp[idx+4]);
                }
            }

            if (operator.equals("+")) {
                if(mulIsNextOperator) {
                    res=Multiplication(num2, nextNum);
                    //System.out.println("temp result: " + res);
                    res=Addition(num1, res);
                    //System.out.println("final add: " + res );
                    idx+=2;
                }
                else {
                    res=Addition(num1, num2);
                }
            }
            else if (operator.equals("-")) {
                if(mulIsNextOperator) {
                    res=Multiplication(num2, nextNum);
                    //ystem.out.println("temp result: " + res);
                    res=Subtraction(num1, res);
                    //System.out.println("final sub: " + res );
                    idx+=2;
                }
                else {
                    res=Subtraction(num1, num2);
                }
            }
            else if (operator.equals("*")) {
                if(mulIsNextOperator) {
                    res=Multiplication(num2, nextNum);
                    //System.out.println("temp result: " + res);
                    res=Multiplication(num1, res);
                    //System.out.println("final mul: " + res );
                    idx+=2;
                }
                else{
                    res=Multiplication(num1, num2);
                }
            }
            else {
                System.out.println("Invalid input detected.");
                System.exit(1);
                //complete = true; //end program
            }
            //System.out.println(res);
            idx+=2;
            num1=res;

            if (idx==temp.length-1) {
                //System.out.println("ended loop");
                complete=true;
            }

        }
        result = String.valueOf(num1);
        return result;
    }

    public static int Addition(int a, int b){
        return a+b;
    }
    public static int Subtraction(int a, int b){
        return a-b;
    }
    public static int Multiplication(int a, int b){
        return a*b;
    }

}
