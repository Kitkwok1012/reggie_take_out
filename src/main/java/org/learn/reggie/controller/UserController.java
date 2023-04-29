package org.learn.reggie.controller;

import com.amazonaws.services.sns.AmazonSNS;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.rkdevblog.sns.service.AwsSnsService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.learn.reggie.common.R;
import org.learn.reggie.config.AwsSnsConfiguration;
import org.learn.reggie.entity.User;
import org.learn.reggie.service.UserService;
import org.learn.reggie.utils.ValidateCodeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.Map;

@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private AwsSnsConfiguration awsSnsConfiguration;


    @PostMapping("/sendMsg")
    public R<String> sendMsg(@RequestBody User user, HttpSession session){
        String phone = user.getPhone();

        if (StringUtils.isNotEmpty(phone)) {
            String code = ValidateCodeUtils.generateValidateCode(4).toString();

            AmazonSNS amazonSNS = awsSnsConfiguration.amazonSNS();
            AwsSnsService awsSnsService = new AwsSnsService(amazonSNS);
            log.info("sendSms to {}", phone);
            log.info("code : {}", code);

            //If you want send sms message to test the function,
            //set key on application.yml -> amazonProperties -> access key and secret key
            //awsSnsService.sendSms(phone, "Reggie take out validation code : "  + code);

            session.setAttribute(phone, code);
            return R.success("Send sms validation code success");
        }

        return R.error("Send sms validation code fail");
    }

    @PostMapping("/login")
    public R<User> login(@RequestBody Map map, HttpSession session) {
        String phone = map.get("phone").toString();
        String code = map.get("code").toString();
        if (StringUtils.isEmpty(code)) {
            return R.error("validation code cannot be null");
        }
        if (StringUtils.isNotEmpty(phone)) {
            if (session.getAttribute(phone) != null) {
                String correctCode = session.getAttribute(phone).toString();
                if (correctCode.equals(code)) {
                    //check database no this phone number, create new user

                    LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();
                    lambdaQueryWrapper.eq(User::getPhone, phone);
                    User user = userService.getOne(lambdaQueryWrapper);

                    if (user == null) {
                        //create new user
                        user = new User();
                        user.setPhone(phone);
                        user.setStatus(1);
                        userService.save(user);
                    }
                    session.setAttribute("user", user.getId());
                    return R.success(user);
                } else {
                    return R.error("validation code is wrong, please try again");
                }
            } else {
                return R.error("Time out, please get a new validation code");
            }
        }
        return R.error("System error, please contact +852 11223344");
    }
}
