package org.learn.reggie.entity;

import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * order
 */
@Data
public class Orders implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private String number;

    //1 Pending payment
    //2 Pending deliver
    //3 Delivered
    //4 Finished
    //5 Canceled
    private Integer status;


    private Long userId;

    private Long addressBookId;

    private LocalDateTime orderTime;


    private LocalDateTime checkoutTime;

    //1. paypal
    //2. credit care
    private Integer payMethod;


    private BigDecimal amount;

    private String remark;

    private String userName;

    private String phone;

    private String address;

    private String consignee;
}
