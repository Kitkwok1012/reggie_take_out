package org.learn.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.learn.reggie.common.CustomException;
import org.learn.reggie.dto.SetmealDto;
import org.learn.reggie.entity.Setmeal;
import org.learn.reggie.entity.SetmealDish;
import org.learn.reggie.mapper.SetmealMapper;
import org.learn.reggie.service.SetmealDishService;
import org.learn.reggie.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {

    @Autowired
    private SetmealDishService setmealDishService;

    @Autowired
    private SetmealService setmealService;

    /**
     * Save set with dish
     * @param setmealDto
     */
    @Override
    @Transactional
    public void saveWithDish(SetmealDto setmealDto) {
        //save Set basic info -> setmeal db
        this.save(setmealDto);

        //Save Relation between Set and Dish setmeal_dish
        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
        setmealDishes.stream().map((item) -> {
            item.setSetmealId(setmealDto.getId());
            return item;
        }).collect(Collectors.toList());

        setmealDishService.saveBatch(setmealDishes);
    }

    /**
     * Delete set with dish
     * @param ids
     */
    @Override
    @Transactional
    public void removeWithDish(List<Long> ids) {
        //select count(*) from setmeal where id in (1, 2, 3) and status = 1

        //Check Status can delete or not
        LambdaQueryWrapper<Setmeal> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.in(Setmeal::getId, ids);
        lambdaQueryWrapper.eq(Setmeal::getStatus, 1);

        int count = this.count(lambdaQueryWrapper);
        if (count > 0) {
            throw new CustomException("Set is in Sale, Can not be deleted");
        }

        //Delete set in setmeal table
        this.removeByIds(ids);

        //Delete relation in setmeal_dish table
        //delete from setmeal_dish where setmealid in (1, 2 ,3);
        LambdaQueryWrapper<SetmealDish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(SetmealDish::getSetmealId, ids);
        setmealDishService.remove(queryWrapper);
    }
}
