package org.learn.reggie.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.learn.reggie.common.BaseContext;
import org.learn.reggie.common.R;
import org.learn.reggie.dto.OrdersDto;
import org.learn.reggie.entity.OrderDetail;
import org.learn.reggie.entity.Orders;
import org.learn.reggie.service.OrderDetailService;
import org.learn.reggie.service.OrderService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/order")
@Slf4j
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderDetailService orderDetailService;

    @PostMapping("/submit")
    public R<String> submit(@RequestBody Orders orders) {
        log.info("Order : {}", orders);
        orderService.submit(orders);
        return R.success("Place order success");
    }

    @GetMapping("/userPage")
    public R<Page<OrdersDto>> list(Long page, int pageSize) {
        Page<Orders> pageInfo = new Page<>();
        Page<OrdersDto> ordersDtoPage = new Page<>();
        pageInfo.setPages(page);
        pageInfo.setSize(pageSize);

        //user id
        Long userid = BaseContext.getCurrentId();

        LambdaQueryWrapper<Orders> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Orders::getUserId, userid);
        List<Orders> records = orderService.page(pageInfo, lambdaQueryWrapper).getRecords();

        BeanUtils.copyProperties(pageInfo, ordersDtoPage, "records");

        List<OrdersDto> orderDtoList = records.stream().map((item) -> {
            OrdersDto ordersDto = new OrdersDto();
            BeanUtils.copyProperties(item, ordersDto);
            LambdaQueryWrapper<OrderDetail> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(OrderDetail::getOrderId, item.getId());
            List<OrderDetail> orderDetails = orderDetailService.list(queryWrapper);
            ordersDto.setOrderDetails(orderDetails);
            return ordersDto;
        }).collect(Collectors.toList());

        ordersDtoPage.setRecords(orderDtoList);

        return R.success(ordersDtoPage);
    }
}
