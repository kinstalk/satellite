package com.kinstalk.satellite.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


/**
 * User: liuling
 * Date: 16/4/12
 * Time: 下午6:55
 */
@Controller
public class IndexController {

    @RequestMapping(value = { "/" })
    public String index(HttpServletResponse response, Model view, HttpServletRequest request) throws IOException {


       // view.addAttribute("MenuUserList",request.getAttribute("MenuList"));
        return "/index";
    }

}
