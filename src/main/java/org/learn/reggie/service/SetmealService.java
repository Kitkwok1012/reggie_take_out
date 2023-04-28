package org.learn.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.learn.reggie.dto.SetmealDto;
import org.learn.reggie.entity.Setmeal;

public interface SetmealService extends IService<Setmeal> {
    /**
     * Save set with dish
     * @param setmealDto
     */
    public void saveWithDish(SetmealDto setmealDto);
}
