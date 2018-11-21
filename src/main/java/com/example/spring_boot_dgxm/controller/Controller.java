package com.example.spring_boot_dgxm.controller;

import com.example.spring_boot_dgxm.configure.AESUtil;
import com.example.spring_boot_dgxm.configure.CheckProperties;
import com.example.spring_boot_dgxm.service.ListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;
import  org.json.JSONObject;
@RestController
public class Controller {
    @Autowired
    private ListService listService;
    @Autowired
    private CheckProperties checkProperties;

    @RequestMapping(value = "/getJson", produces = "application/json;charset=UTF-8")
    public String login() {
        Map map = listService.itemsList();
        JSONObject json = new JSONObject(map);
        return json.toString();
    }

   @RequestMapping(value = "/getList")
    public String getList() {
        Map map = listService.itemsList();
      JSONObject json = new JSONObject(map);
        String value = AESUtil.encrypt(json.toString(), checkProperties.getAeskey());
        return value;
    }
}
