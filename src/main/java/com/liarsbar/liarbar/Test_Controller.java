package com.liarsbar.liarbar;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class Test_Controller {
    @RequestMapping("/fuck")
    public String test() {
        return "index";
    }
}
