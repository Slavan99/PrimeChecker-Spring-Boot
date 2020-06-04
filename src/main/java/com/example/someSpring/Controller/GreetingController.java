package com.example.someSpring.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;
import java.lang.String;

@Controller
public class GreetingController {

    @GetMapping("/")
    public String greeting(@RequestParam(name = "name", required = false, defaultValue = "World") String name,
                           Map<String, Object> model) {
        model.put("name", name);
        return "greeting";
    }
    /*
    @PostMapping("/hello")
    public String add(@RequestParam String name, Map<String, Object> model){
        User user = new User(name);
        userRepository.save(user);

        return hello(model);
    }

    @PostMapping("filter")
    public String filter(@RequestParam String filter, Map<String, Object> model){
        Iterable<User> byName;
        if(filter != null && !filter.isEmpty()) {
            byName = userRepository.findByName(filter);
        }
        else{
            byName = userRepository.findAll();
        }
        model.put("users", byName);

        return "hello";
    }

     */

}