package com.ljming.provider.project.controller;

import com.ljming.provider.project.service.DubboServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Title:DubboServiceController
 * <p>
 * Description:
 * </p >
 * Author Jming.L
 * Date 2022/4/8 14:36
 */
@RestController
@RequestMapping("/provider")
public class DubboServiceController {

    @Autowired
    DubboServiceImp dubboService;

    @RequestMapping("/select")
    public String select(String message) {
        dubboService.select(message);
        return "查询:" + message;
    }

    @RequestMapping("/insert")
    public String insert(String message) {
        dubboService.select(message);
        return "插入:" + message;
    }


}
