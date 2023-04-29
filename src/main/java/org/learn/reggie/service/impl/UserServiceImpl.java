package org.learn.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.learn.reggie.entity.User;
import org.learn.reggie.mapper.UserMapper;
import org.learn.reggie.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService{
}
