package org.learn.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.learn.reggie.dto.DishDto;
import org.learn.reggie.entity.Dish;
import org.learn.reggie.entity.DishFlavor;
import org.learn.reggie.mapper.DishMapper;
import org.learn.reggie.service.DishFlavorService;
import org.learn.reggie.service.DishService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {

    @Autowired
    private DishFlavorService dishFlavorService;

    @Autowired
    private DishService dishService;


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

    @Override
    public DishDto getByIdWithFlavor(Long id) {
        DishDto dishDto = new DishDto();
        Dish dish = dishService.getById(id);
        LambdaQueryWrapper<DishFlavor> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(DishFlavor::getDishId, dish.getId());
        List<DishFlavor> list = dishFlavorService.list(lambdaQueryWrapper);

        BeanUtils.copyProperties(dish, dishDto);
        dishDto.setFlavors(list);
        return  dishDto;
    }

    @Override
    @Transactional
    public void updateWithFlavor(DishDto dishDto) {
        //update Dish basic info
        this.updateById(dishDto);

        //clear dish flavor
        LambdaQueryWrapper<DishFlavor> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(DishFlavor::getDishId, dishDto.getId());
        dishFlavorService.remove(lambdaQueryWrapper);

        //add dish flavor
        List<DishFlavor> flavors = dishDto.getFlavors();
        flavors.stream().map((item) ->  {
            item.setDishId(dishDto.getId());
            return item;
        }).collect(Collectors.toList());

        dishFlavorService.saveBatch(flavors);
    }
}
