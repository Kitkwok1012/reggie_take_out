package org.learn.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.learn.reggie.entity.Orders;

public interface OrderService extends IService<Orders> {

    public void submit(Orders orders);
}
