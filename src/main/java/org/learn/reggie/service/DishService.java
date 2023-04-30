package org.learn.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.learn.reggie.dto.DishDto;
import org.learn.reggie.entity.Dish;

public interface DishService extends IService<Dish> {

    //Add dish and Dish flavor at the same time
    public void saveWtihFlavor(DishDto dishDto);

    public DishDto getByIdWithFlavor(Long id);

    public void updateWithFlavor(DishDto dishDto);
}
