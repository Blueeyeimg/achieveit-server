package com.ecnu.achieveit.service.impl;

import com.ecnu.achieveit.dao.ClientMapper;
import com.ecnu.achieveit.model.Client;
import com.ecnu.achieveit.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class ClientServiceImpl implements ClientService {

    @Autowired(required = false)
    private ClientMapper clientMapper;

    @Override
    public List<Client> queryClients() {
        return clientMapper.selectClientList();
    }
}
