package org.learn.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.learn.reggie.entity.SetmealDish;
import org.learn.reggie.mapper.SetmealDishMapper;
import org.learn.reggie.service.SetmealDishService;
import org.learn.reggie.service.SetmealService;
import org.springframework.stereotype.Service;


@Service
@Slf4j
public class SetmealDishServiceImpl extends ServiceImpl<SetmealDishMapper, SetmealDish> implements SetmealDishService {
}
