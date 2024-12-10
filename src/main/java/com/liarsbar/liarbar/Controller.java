package com.liarsbar.liarbar;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@org.springframework.stereotype.Controller
public class Controller {
<<<<<<< HEAD
    @RequestMapping("/gamepage")
    public String test() {
        return "game";
=======

    @RequestMapping("/gamepage")
    public String test() {
        return "game";
    }

    // 显示静态的 game.html 页面
    @GetMapping("/gamepage")
    public String showGamePage() {
        return "forward:/game.html";  // 跳转到 static 目录下的 game.html
>>>>>>> 03b5ca04a2daef25f78bcfcb8f9f34a21aa707a3
    }
}
