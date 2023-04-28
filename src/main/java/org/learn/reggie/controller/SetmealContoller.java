package org.learn.reggie.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.learn.reggie.common.R;
import org.learn.reggie.dto.SetmealDto;
import org.learn.reggie.entity.Category;
import org.learn.reggie.entity.Dish;
import org.learn.reggie.entity.Setmeal;
import org.learn.reggie.entity.SetmealDish;
import org.learn.reggie.service.CategoryService;
import org.learn.reggie.service.SetmealDishService;
import org.learn.reggie.service.SetmealService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/setmeal")
@Slf4j
public class SetmealContoller {

    @Autowired
    private SetmealService setmealService;

    @Autowired
    private SetmealDishService setmealDishService;

    @Autowired
    private CategoryService categoryService;

    @PostMapping
    public R<String> save(@RequestBody SetmealDto setmealDto) {
        log.info("Set meal : {}", setmealDto);
        setmealService.saveWithDish(setmealDto);
        return R.success("Add Dish Success");
    }

    @GetMapping("/page")
    public R<Page> list(int page, int pageSize, String name) {
        Page<Setmeal> pageInfo = new Page<>();
        Page<SetmealDto> dtoPage = new Page<>();
        pageInfo.setPages(page);
        pageInfo.setSize(pageSize);

        LambdaQueryWrapper<Setmeal> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.like(Strings.isNotEmpty(name), Setmeal::getName, name);
        lambdaQueryWrapper.orderByDesc(Setmeal::getUpdateTime);

        setmealService.page(pageInfo, lambdaQueryWrapper);

        BeanUtils.copyProperties(pageInfo, dtoPage,"records");
        List<Setmeal> records = pageInfo.getRecords();
        List<SetmealDto> list = records.stream().map((item) -> {
            SetmealDto setmealDto = new SetmealDto();
            BeanUtils.copyProperties(item, setmealDto);
            Long categoryId = item.getCategoryId();
            Category category = categoryService.getById(categoryId);
            if (category != null) {
                setmealDto.setCategoryName(category.getName());
            }
            return setmealDto;
        }).collect(Collectors.toList());

        dtoPage.setRecords(list);

        return R.success(dtoPage);
    }

    @DeleteMapping
    public R<String> delete(@RequestParam List<Long> ids) {
        log.info("ids : {}",ids);
        setmealService.removeWithDish(ids);
        return R.success("Set meal delete success");
    }


    @GetMapping("/{id}")
    public R<SetmealDto> getById(@PathVariable long id) {
        Setmeal setmeal = setmealService.getById(id);

        SetmealDto setmealDto = new SetmealDto();
        BeanUtils.copyProperties(setmeal, setmealDto);

        LambdaQueryWrapper<SetmealDish> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(SetmealDish::getSetmealId, id);

        List<SetmealDish> list = setmealDishService.list(lambdaQueryWrapper);
        setmealDto.setSetmealDishes(list);

        return R.success(setmealDto);
    }

    @PutMapping
    public R<String> modify(@RequestBody SetmealDto setmealDto) {
        log.info("Modify setmealdish {}", setmealDto);
        setmealService.modifyWithDish(setmealDto);
        return R.success("modify setmeal success");
    }

    @PostMapping("/status/{status}")
    public R<String> status(@PathVariable int status, @RequestParam List<Long> ids) {
        log.info("update status {}", status);
        setmealService.updateStatus(status, ids);
        return R.success("update status success");
    }

}
