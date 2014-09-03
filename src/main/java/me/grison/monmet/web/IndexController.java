package me.grison.monmet.web;

import me.grison.monmet.repository.AppRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Map;

@Controller
public class IndexController {
    @Autowired
    AppRepository repository;

    @RequestMapping("/")
    public String index() {
        return "forward:index.html";
    }

    @ResponseBody
    @RequestMapping("/hits")
    public Map<String, Integer> hits() {
        return new HashMap<String, Integer>() {{ put("hits", repository.getHits()); }};
    }

}