package org.learn.reggie.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 套餐 Set
 */
@Data
public class Setmeal implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;


	//id
	private Long categoryId;


	//Set name
	private String name;


	//Set Price
	private BigDecimal price;


	//0: Unavailable 1: Available
	private Integer status;


	//Code
	private String code;


	//描述信息
	private String description;


	//image
	private String image;


	@TableField(fill = FieldFill.INSERT)
	private LocalDateTime createTime;


	@TableField(fill = FieldFill.INSERT_UPDATE)
	private LocalDateTime updateTime;


	@TableField(fill = FieldFill.INSERT)
	private Long createUser;


	@TableField(fill = FieldFill.INSERT_UPDATE)
	private Long updateUser;


	//idDeleted
	private Integer isDeleted;
}