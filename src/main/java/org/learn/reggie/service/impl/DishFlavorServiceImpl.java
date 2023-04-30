package org.learn.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.learn.reggie.entity.DishFlavor;
import org.learn.reggie.mapper.DishFlavorMapper;
import org.learn.reggie.service.DishFlavorService;
import org.springframework.stereotype.Service;


@Service
public class DishFlavorServiceImpl extends ServiceImpl<DishFlavorMapper, DishFlavor> implements DishFlavorService {
}
