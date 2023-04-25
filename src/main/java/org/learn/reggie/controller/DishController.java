package org.learn.reggie.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.learn.reggie.common.R;
import org.learn.reggie.dto.DishDto;
import org.learn.reggie.entity.Category;
import org.learn.reggie.entity.Dish;
import org.learn.reggie.entity.DishFlavor;
import org.learn.reggie.service.CategoryService;
import org.learn.reggie.service.DishFlavorService;
import org.learn.reggie.service.DishService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/dish")
@Slf4j
public class DishController {

    @Autowired
    private DishService dishService;

    @Autowired
    private DishFlavorService dishFlavorService;

    @Autowired
    private CategoryService categoryService;

    @PostMapping
    public R<String> save(@RequestBody DishDto dishDto) {
        log.info(dishDto.toString());
        dishService.saveWtihFlavor(dishDto);
        return R.success("Add dish success");
    }

    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, String name) {
        Page<Dish> pageInfo = new Page<>(page, pageSize);
        Page<DishDto> dishDtoPageInfo = new Page<>(page, pageSize);
        LambdaQueryWrapper<Dish> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.like(name != null, Dish::getName, name);
        lambdaQueryWrapper.orderByDesc(Dish::getUpdateTime);
        dishService.page(pageInfo, lambdaQueryWrapper);

        //clone
        BeanUtils.copyProperties(pageInfo, dishDtoPageInfo, "records");
        List<Dish> records = pageInfo.getRecords();
        List<DishDto> list = records.stream().map((item) -> {
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(item, dishDto);
            Long categoryId = item.getCategoryId();
            Category category = categoryService.getById(categoryId);
            String categoryName = category.getName();
            dishDto.setCategoryName(categoryName);
            return dishDto;
        }).collect(Collectors.toList());


        dishDtoPageInfo.setRecords(list);
        return R.success(dishDtoPageInfo);
    }

    @GetMapping("/{id}")
    public R<DishDto> get(@PathVariable Long id){
        DishDto dishDto = dishService.getByIdWithFlavor(id);
        return R.success(dishDto);
    }

    @PutMapping
    public R<String> update(@RequestBody DishDto dishDto) {
        log.info(dishDto.toString());
        dishService.updateWithFlavor(dishDto);

        return R.success("Update dish success");
    }

    @DeleteMapping
    @Transactional
    public R<String> delete(@RequestParam(value = "ids") Long id) {
        log.info("Delete dish {}", id);
        //1. delete dish in dish table
        dishService.removeById(id);

        //2. delete dish in dish flavor table
        LambdaQueryWrapper<DishFlavor> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(DishFlavor::getDishId, id);
        dishFlavorService.remove(lambdaQueryWrapper);
        return R.success("delete dish success");
    }

    @PostMapping("/status/{status}")
    public R<String> status(@PathVariable int status, @RequestParam(value = "ids") Long id) {
        log.info("update dish {} to status {}", id, status);
        LambdaQueryWrapper<Dish> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Dish::getStatus, status);
        Dish dish = dishService.getById(id);
        dish.setStatus(status);
        dishService.updateById(dish);
        return R.success("update dish status success");
    }

    @GetMapping("/list")
    public R<List<Dish>> list(Dish dish) {
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(dish.getCategoryId()!=null, Dish::getCategoryId, dish.getCategoryId());

        //Only get the dish if status is 1 (available)
        queryWrapper.eq(Dish::getStatus, 1);
        
        //Sort
        queryWrapper.orderByAsc(Dish::getSort).orderByDesc(Dish::getUpdateTime);
        List<Dish> list = dishService.list(queryWrapper);
        return R.success(list);
    }
}
