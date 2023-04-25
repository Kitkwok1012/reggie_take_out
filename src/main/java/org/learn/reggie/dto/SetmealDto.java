package org.learn.reggie.dto;

import org.learn.reggie.entity.Setmeal;
import org.learn.reggie.entity.SetmealDish;
import lombok.Data;
import java.util.List;

@Data
public class SetmealDto extends Setmeal {

    private List<SetmealDish> setmealDishes;

    private String categoryName;
}
