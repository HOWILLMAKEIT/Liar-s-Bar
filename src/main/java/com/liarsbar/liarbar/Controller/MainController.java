package com.liarsbar.liarbar.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@org.springframework.stereotype.Controller
public class MainController {

    // 显示静态的 game.html 页面
    @GetMapping("/gamepage")
    public String showGamePage() {
        return "forward:/game.html";  // 跳转到 static 目录下的 game.html

    }
}
