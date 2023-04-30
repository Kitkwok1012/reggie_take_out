package org.learn.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.learn.reggie.common.BaseContext;
import org.learn.reggie.common.R;
import org.learn.reggie.entity.ShoppingCart;
import org.learn.reggie.service.ShoppingCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/shoppingCart")
@Slf4j
public class ShoppingCartController {

    @Autowired
    private ShoppingCartService shoppingCartService;

    /**
     * Add dish/set to shopping cart
     * @param shoppingCart
     * @return
     */
    @PostMapping("/add")
    public R<ShoppingCart> add(@RequestBody ShoppingCart shoppingCart) {
        log.info("Add to shopping cart {}" , shoppingCart);

        //Set user id
        Long userId = BaseContext.getCurrentId();
        shoppingCart.setUserId(userId);

        //check dish or setmeal in shopping cart or not
        Long dishId = shoppingCart.getDishId();
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId, userId);
        if (dishId != null) {
            //add dish
            queryWrapper.eq(ShoppingCart::getDishId, dishId);
        } else {
            //add setmeal
            queryWrapper.eq(ShoppingCart::getSetmealId, shoppingCart.getSetmealId());
        }
        //select * from shopping_cart where user_id = ? and dish_id = ?;
        ShoppingCart cart = shoppingCartService.getOne(queryWrapper);
        if (cart != null) {
            //if existed, add number + 1
            Integer number = cart.getNumber();
            cart.setNumber(number+1);
            shoppingCartService.updateById(cart);
        } else {
            //if not existed, default 1
            shoppingCart.setNumber(1);
            shoppingCart.setCreateTime(LocalDateTime.now());
            shoppingCartService.save(shoppingCart);
            cart = shoppingCart;
        }

        return R.success(cart);
    }

    @GetMapping("/list")
    public R<List<ShoppingCart>> list() {
        log.info("list shopping cart");

        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId, BaseContext.getCurrentId());
        queryWrapper.orderByDesc(ShoppingCart::getCreateTime);

        List<ShoppingCart> list = shoppingCartService.list(queryWrapper);
        return R.success(list);
    }

    @DeleteMapping("/clean")
    public R<String> clean() {
        //Delete from Shopping_cart where user_id = ?
        Long userId = BaseContext.getCurrentId();
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId, userId);

        shoppingCartService.remove(queryWrapper);
        return R.success("clean shopping cart success");
    }

}
