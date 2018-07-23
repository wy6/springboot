package com.example.web;

import com.alibaba.fastjson.JSON;
import com.example.common.BaseRequest;
import com.example.common.BaseResult;
import com.example.entity.user.User;
import com.example.enums.ResultCodeEnum;
import com.example.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Date;
import java.util.List;

/**
 * @Description:
 * @Author: WangY
 * @Date: Created in 2018/7/21 14:56
 * @Modified By：
 */

@RestController
public class UserController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private UserService userService;

    @GetMapping("/toAddUser")
    public ModelAndView toAddUser() {
        ModelAndView mv = new ModelAndView("add-user");
        return mv;
    }

    @PostMapping("/addUser")
    public BaseResult addUser(@RequestBody BaseRequest<User> userBaseRequest) {
        logger.info("UserController.addUser in,baseRequest[{}].", JSON.toJSONString(userBaseRequest));
        User user = userBaseRequest.getReqData();
        user.setCreateTime(new Date());
        userService.addUser(user);
        return BaseResult.ok(user);
    }

    @PostMapping("/addTwoUser")
    public BaseResult addTwoUser(@RequestBody BaseRequest<User> userBaseRequest) {
        logger.info("*UserController.addTwoUser.50 in,baseRequest[{}].", JSON.toJSONString(userBaseRequest));
        User user = userBaseRequest.getReqData();
        user.setCreateTime(new Date());
        try {
            userService.addTwoUser(user);
            return BaseResult.ok(user);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("!!!UserController.addTwoUser.58 in,error[{}].", e.getMessage());;
            return BaseResult.error(ResultCodeEnum.ERROR);
        }
    }

    @PostMapping("/delUser")
    public BaseResult delUser(@RequestBody BaseRequest<User> userBaseRequest) {
        logger.info("UserController.delUser.50 in,baseRequest[{}].", JSON.toJSONString(userBaseRequest));
        User user = userBaseRequest.getReqData();
        try {
            userService.delUser(user.getId());
            return BaseResult.ok();
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("UserController.delUser.57 in,error[{}].", e.getMessage());
            return BaseResult.error(ResultCodeEnum.ERROR);
        }
    }

    @PostMapping("/queryUser")
    public BaseResult queryUser(@RequestBody BaseRequest<User> userBaseRequest) {
        logger.info("UserController.queryUser.64 in,baseRequest[{}].", JSON.toJSONString(userBaseRequest));
        User user = userBaseRequest.getReqData();
        List<User> userList = null;
        try {
            userList = userService.queryUserName(user.getUserName());
            return BaseResult.ok(userList);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("UserController.queryUser.71 in,error[{}].", e.getMessage());
            return BaseResult.error(ResultCodeEnum.ERROR);
        }
    }

    @PostMapping("/queryAllUser")
    public BaseResult queryAllUser() {
        List<User> userList = null;
        try {
            userList = userService.queryAllUserByPage(1, 2);
            return BaseResult.ok(userList);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("!!!UserController.queryAllUser.100 in,error[{}].", e.getMessage());
            return BaseResult.error(ResultCodeEnum.ERROR);
        }
    }

}
