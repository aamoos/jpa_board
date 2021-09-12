package jpa.board.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.Map;

@Controller
@Slf4j
public class BoardController {

    @GetMapping("/")
    public String list(HttpSession session, Model model){

        System.out.println(session.getAttribute("authority"));
        model.addAttribute("authority", session.getAttribute("authority"));
        return "main/list";
    }

    @GetMapping("/write")
    public String write(){
        return "main/write";
    }

    @ResponseBody
    @PostMapping("/write")
    public Map<String, Object> writeSubmit(@RequestBody Map<String, Object> params){
        log.info("params={}", params);
        return params;
    };

    @GetMapping("/update")
    public String update(){
        return "main/update";
    }

}
