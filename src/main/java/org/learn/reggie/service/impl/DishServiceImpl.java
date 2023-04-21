package org.learn.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.learn.reggie.dto.DishDto;
import org.learn.reggie.entity.Dish;
import org.learn.reggie.entity.DishFlavor;
import org.learn.reggie.mapper.DishMapper;
import org.learn.reggie.service.DishFlavorService;
import org.learn.reggie.service.DishService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {

    @Autowired
    private DishFlavorService dishFlavorService;


    @Override
    @Transactional
    public void saveWtihFlavor(DishDto dishDto) {
        //save dish
        this.save(dishDto);
        Long dishId = dishDto.getId();

        //save dish flavor
        List<DishFlavor> flavors = dishDto.getFlavors();
        flavors.stream().map((item) ->  {
            item.setDishId(dishId);
            return item;
        }).collect(Collectors.toList());

        dishFlavorService.saveBatch(flavors);

    }
}
