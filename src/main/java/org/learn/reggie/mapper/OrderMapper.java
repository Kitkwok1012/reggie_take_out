package org.learn.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.learn.reggie.entity.Orders;

@Mapper
public interface OrderMapper extends BaseMapper<Orders> {
}
