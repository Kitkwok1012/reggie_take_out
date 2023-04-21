package org.learn.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.learn.reggie.common.CustomException;
import org.learn.reggie.entity.Category;
import org.learn.reggie.entity.Dish;
import org.learn.reggie.entity.Setmeal;
import org.learn.reggie.mapper.CategoryMapper;
import org.learn.reggie.service.CategoryService;
import org.learn.reggie.service.DishService;
import org.learn.reggie.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    @Autowired
    private DishService dishService;

    @Autowired
    private SetmealService setmealService;

    /**
     * Return by id
     * @param id
     */
    @Override
    public void remove(Long id) {
        //check is in the dish or not
        LambdaQueryWrapper<Dish> dishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        dishLambdaQueryWrapper.eq(Dish::getCategoryId, id);
        int dishCount = dishService.count(dishLambdaQueryWrapper);
        if (dishCount > 0) {
            throw new CustomException("Dish is in this category, cannot be deleted.");
        }

        //check is in the set or not
        LambdaQueryWrapper<Setmeal> setmealLambdaQueryWrapper = new LambdaQueryWrapper<>();
        setmealLambdaQueryWrapper.eq(Setmeal::getCategoryId, id);
        int setmealCount = setmealService.count(setmealLambdaQueryWrapper);
        if (setmealCount > 0) {
            throw new CustomException("Set meal is in this category, cannot be deleted.");
        }

        //remove by id
        super.removeById(id);
    }
}
