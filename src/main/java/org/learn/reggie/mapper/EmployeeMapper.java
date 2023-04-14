package org.learn.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.learn.reggie.entity.Employee;

@Mapper
public interface EmployeeMapper extends BaseMapper<Employee> {
}
