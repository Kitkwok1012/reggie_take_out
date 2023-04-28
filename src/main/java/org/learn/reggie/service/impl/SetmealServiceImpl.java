package org.learn.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.learn.reggie.common.CustomException;
import org.learn.reggie.dto.SetmealDto;
import org.learn.reggie.entity.Dish;
import org.learn.reggie.entity.Setmeal;
import org.learn.reggie.entity.SetmealDish;
import org.learn.reggie.mapper.SetmealMapper;
import org.learn.reggie.service.DishService;
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

    @Autowired
    private DishService dishService;

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

    /**
     * Modify set with dish
     * @param setmealDto
     */
    @Override
    @Transactional
    public void modifyWithDish(SetmealDto setmealDto) {
        //Update basic setmeal info
        setmealService.updateById(setmealDto);

        //Remove origin setmeal dish data
        LambdaQueryWrapper<SetmealDish> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(SetmealDish::getSetmealId, setmealDto.getId());
        setmealDishService.remove(lambdaQueryWrapper);

        //Insert new setmeal dish data
        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
        setmealDishes.stream().map((item) -> {
            item.setSetmealId(setmealDto.getId());
            return item;
        }).collect(Collectors.toList());
        setmealDishService.saveBatch(setmealDishes);
    }

    @Override
    @Transactional
    public void updateStatus(int status, List<Long> ids) {
        //Update setmeal set status = {status} where id in {ids}
        if (status == 1) {
            //If we want to enable the set, make sure all dish in set is available
            LambdaQueryWrapper<SetmealDish> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.in(SetmealDish::getSetmealId, ids);
            List<SetmealDish> setmealDishList = setmealDishService.list(lambdaQueryWrapper);

            //Use dish id to search dish is available or not
            for (SetmealDish setmealDish : setmealDishList) {
                Dish dish = dishService.getById(setmealDish.getDishId());
                Integer dishStatus = dish.getStatus();
                if (dishStatus == 0) {
                    throw new CustomException("Dish " + dish.getName() + " in set is not available, pls enable first");
                }
            }
        }

        // available
        LambdaUpdateWrapper<Setmeal> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.in(Setmeal::getId, ids);
        lambdaUpdateWrapper.set(Setmeal::getStatus, status);
        setmealService.update(lambdaUpdateWrapper);
    }
}
