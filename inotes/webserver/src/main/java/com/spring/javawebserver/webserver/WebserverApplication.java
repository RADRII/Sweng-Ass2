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

    @GetMapping("/")
    public String index(Model model) {
        return "index";
    }

    @PostMapping("/calculate") //when calculate button pressed
    public String calculation(@RequestParam(required = false) String expression) throws Exception {

        if (expression != null && expression.equals("Calculate!")) { //input is not null
            //
            //
            return "redirect:/";
        }
        return "index";
    }

    public void evaluate() {
        //functions to calculate expressions
    }

}

