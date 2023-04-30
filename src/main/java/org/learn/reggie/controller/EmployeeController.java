package org.learn.reggie.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.learn.reggie.common.BaseContext;
import org.learn.reggie.common.R;
import org.learn.reggie.entity.Employee;
import org.learn.reggie.log.WebLog;
import org.learn.reggie.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;

    /**
     *
     * @param request
     * @param employee
     * @return R
     */

    //1. Enquiry username exist in database
    //2. Handle password MD5 encode
    //3  Check password
    //4. Check baned
    //5. Put employee id to session and return
    @PostMapping("/login")
    public R<Employee> login(HttpServletRequest request, @RequestBody Employee employee) {

        LambdaQueryWrapper<Employee> employeeLambdaQueryWrapper = new LambdaQueryWrapper<>();
        employeeLambdaQueryWrapper.eq(Employee::getUsername, employee.getUsername());
        Employee loginEmployee = employeeService.getOne(employeeLambdaQueryWrapper);

        String password = employee.getPassword();
        password = DigestUtils.md5DigestAsHex(password.getBytes());

        if (loginEmployee == null) {
            return R.error("UserID is not exist");
        }

        if (!password.equals(loginEmployee.getPassword())) {
            return R.error("UserID or Password is not correct");
        }

        if (loginEmployee.getStatus() == 0) {
            return R.error("User is banned!");
        }

        request.getSession().setAttribute("employee", loginEmployee.getId());
        //should clear the password before return the employee object
        loginEmployee.setPassword(null);
        return R.success(loginEmployee);
    }

    /**
     *
     * @param request
     * @return R
     */
    @PostMapping("/logout")
    public R<String> logout(HttpServletRequest request) {
        request.getSession().removeAttribute("employee");
        return R.success("logout success");
    }

    @PostMapping
    public R<String> save(HttpServletRequest request, @RequestBody Employee employee){
        log.info("Add new employee");
        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));

//        employee.setCreateTime(LocalDateTime.now());
//        employee.setUpdateTime(LocalDateTime.now());
//        Long id = (Long) request.getSession().getAttribute("employee");
//        employee.setCreateUser(id);
//        employee.setUpdateUser(id);

        employeeService.save(employee);



        return R.success("Add new employee success");
    }

    /**
     * Use id to update employee
     * @param request
     * @param employee
     * @return
     */
    @PutMapping
    public R<String> update(HttpServletRequest request, @RequestBody Employee employee) {
//        employee.setUpdateTime(LocalDateTime.now());
//        employee.setUpdateUser(((Long) request.getSession().getAttribute("employee")));
        employeeService.updateById(employee);
        return R.success("Update employee success");
    }

    @GetMapping("/page")
    @WebLog()
    public R<Page> page(HttpServletRequest request, int page, int pageSize, String name) {
        log.info("page = {}, pageSize = {}, name = {}", page, pageSize, name);
        Page pageInfo = new Page(page, pageSize);

        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.isNotEmpty(name),Employee::getName,name);
        queryWrapper.orderByDesc(Employee::getUpdateTime);

        employeeService.page(pageInfo, queryWrapper);
        return R.success(pageInfo);
    }

    @GetMapping("/{id}")
    public R<Employee> getById(@PathVariable Long id) {
        log.info("query user by id {} ", id);
        Employee employee = employeeService.getById(id);
        if (employee != null) {
            return R.success(employee);
        }
        return R.error("Can't find the user with id " + id);
    }
}
