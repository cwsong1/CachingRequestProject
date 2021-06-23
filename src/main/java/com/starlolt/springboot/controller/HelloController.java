package com.starlolt.springboot.controller;

import com.starlolt.springboot.dto.HelloDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
public class HelloController {
    @RequestMapping(value = "/hello", method = RequestMethod.GET)
    public String helloGet(@ModelAttribute HelloDto request) {
        log.info("request:" + request.toString());
        return "GET_SUCCESS";
    }

    @RequestMapping(value = "/hello", method = RequestMethod.POST, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public String helloPost(@ModelAttribute HelloDto request) {
        log.info("request:" + request.toString());
        return "POST_SUCCESS";
    }

    @RequestMapping(value = "/helloJson", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public String helloPostJson(@RequestBody HelloDto request) {
        log.info("request:" + request.toString());
        return "POST_JSON_SUCCESS";
    }
}
