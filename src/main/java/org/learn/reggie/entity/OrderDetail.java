package org.learn.reggie.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * order detail
 */
@Data
public class OrderDetail implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private String name;

    private Long orderId;


    private Long dishId;


    private Long setmealId;


    private String dishFlavor;


    private Integer number;

    private BigDecimal amount;

    private String image;
}
