package com.liarsbar.liarbar;

import org.springframework.web.bind.annotation.RequestMapping;

@org.springframework.stereotype.Controller
public class Controller {
    @RequestMapping("/fuck")
    public String test() {
        return "index";
    }
}
