package org.learn.reggie.entity;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.io.Serializable;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
/**
 * 用户信息
 */
@Data
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;


    //name
    private String name;


    //phone
    private String phone;


    //0 f 1 m
    private String sex;


    //id number
    private String idNumber;


    //image
    private String avatar;


    //0 ban 1 enable
    private Integer status;
}
