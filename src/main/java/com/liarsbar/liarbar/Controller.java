package com.liarsbar.liarbar;

import org.springframework.web.bind.annotation.RequestMapping;

@org.springframework.stereotype.Controller
public class Controller {
    @RequestMapping("/gamepage")
    public String test() {
        return "game";
    }
}
