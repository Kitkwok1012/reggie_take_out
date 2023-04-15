package org.learn.reggie.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.learn.reggie.common.R;
import org.learn.reggie.entity.Employee;
import org.learn.reggie.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

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
        //loginEmployee.setPassword(null);
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
}
