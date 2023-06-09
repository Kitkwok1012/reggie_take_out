package org.learn.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.learn.reggie.dto.SetmealDto;
import org.learn.reggie.entity.Setmeal;

import java.util.List;

public interface SetmealService extends IService<Setmeal> {
    /**
     * Save set with dish
     * @param setmealDto
     */
    public void saveWithDish(SetmealDto setmealDto);

    /**
     * Delete set with dish
     * @param ids
     */
    public void removeWithDish(List<Long> ids);

    /**
     * Modify set with dish
     * @param setmealDto
     */
    public void modifyWithDish(SetmealDto setmealDto);


    /**
     * Update status for set meal
     * @param status
     * @param ids
     */
    public void updateStatus(int status, List<Long> ids);
}
