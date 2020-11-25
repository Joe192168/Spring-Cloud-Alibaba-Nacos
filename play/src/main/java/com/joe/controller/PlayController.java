package com.joe.controller;

import com.joe.entity.Play;
import com.joe.service.PlayServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PlayController {

    @Autowired
    private PlayServiceImpl playService;

    @GetMapping("/save")
    public String save(){
        Play play = new Play();
        play.setName("张三");
        playService.save(play);
        return "保存成功";
    }
}
