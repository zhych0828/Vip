package com.feijian.service.impl;

import com.feijian.dao.UserMapper;
import com.feijian.model.User;
import com.feijian.service.UserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class UserServiceImpl implements UserService {

    @Resource
    private UserMapper userMapper;

    @Override
    public void insert(User record) {
        userMapper.insert(record);
    }
}