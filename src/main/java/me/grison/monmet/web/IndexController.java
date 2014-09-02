package me.grison.monmet.web;

import me.grison.monmet.repository.AppRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;

@Controller
public class IndexController {
    @Autowired
    AppRepository repository;

    @RequestMapping("/")
    public String index(Map<String, Object> model) {
        model.put("hits", repository.getHits());
        return "index";
    }

}