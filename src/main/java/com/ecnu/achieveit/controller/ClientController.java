package com.ecnu.achieveit.controller;

import com.alibaba.fastjson.JSONObject;
import com.ecnu.achieveit.model.Client;
import com.ecnu.achieveit.service.ClientService;
import com.ecnu.achieveit.util.RestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.stream.Collectors;

@RestController
public class ClientController {

    @Autowired
    private ClientService clientService;

    @GetMapping("/client/ids")
    public Object getClientIds(){
        String[] ids = clientService.queryClients().stream().map(Client::getClientId).toArray(String[]::new);
        JSONObject clientIds = new JSONObject();
        clientIds.put("clientIds", ids);
        return RestResponse.success(clientIds);
    }
}
