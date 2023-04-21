package org.learn.reggie.controller;


import lombok.extern.slf4j.Slf4j;
import org.learn.reggie.common.R;
import org.learn.reggie.dto.DishDto;
import org.learn.reggie.service.DishFlavorService;
import org.learn.reggie.service.DishService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/dish")
@Slf4j
public class DishController {

    @Autowired
    private DishService dishService;

    @Autowired
    private DishFlavorService dishFlavorService;

    @PostMapping
    public R<String> save(@RequestBody DishDto dishDto) {
        log.info(dishDto.toString());
        dishService.saveWtihFlavor(dishDto);
        return R.success("Add dish success");
    }
}
