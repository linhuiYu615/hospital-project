package com.example.controller;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.example.common.Result;
import com.example.common.enums.ResultCodeEnum;
import com.example.common.enums.RoleEnum;
import com.example.pojo.entity.Account;
import com.example.service.AdminService;
import com.example.service.DoctorService;
import com.example.service.UserService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 基础前端接口
 */
@CrossOrigin
@RestController
public class WebController {

    @Resource
    private AdminService adminService;
    @Resource
    private DoctorService doctorService;
    @Resource
    private UserService userService;

    @GetMapping("/")
    public Result hello() {
        return Result.success("访问成功"); // 返回访问成功的结果
    }

    /**
     * 登录
     */
    @PostMapping("/login")
    public Result login(@RequestBody Account account) {
        if (ObjectUtil.isEmpty(account.getUsername()) || ObjectUtil.isEmpty(account.getPassword())
                || ObjectUtil.isEmpty(account.getRole())) {
            return Result.error(ResultCodeEnum.PARAM_LOST_ERROR); // 返回参数缺失错误
        }
        if (RoleEnum.ADMIN.name().equals(account.getRole())) {
            return Result.success(adminService.login(account)); // 调用AdminService的登录方法并返回结果
        }
        if (RoleEnum.DOCTOR.name().equals(account.getRole())) {
            return Result.success(doctorService.login(account)); // 调用DoctorService的登录方法并返回结果
        }
        if (RoleEnum.USER.name().equals(account.getRole())) {
            return Result.success(userService.login(account)); // 调用UserService的登录方法并返回结果
        }
        return Result.success(); // 返回成功结果
    }

    /**
     * 注册
     */
    @PostMapping("/register")
    public Result register(@RequestBody Account account) {
        if (StrUtil.isBlank(account.getUsername()) || StrUtil.isBlank(account.getPassword())
                || ObjectUtil.isEmpty(account.getRole())) {
            return Result.error(ResultCodeEnum.PARAM_LOST_ERROR); // 返回参数缺失错误
        }
        if (RoleEnum.USER.name().equals(account.getRole())) {
            userService.register(account); // 调用UserService的注册方法
        }
        return Result.success(); // 返回成功结果
    }

    /**
     * 修改密码
     */
    @PutMapping("/updatePassword")
    public Result updatePassword(@RequestBody Account account) {
        if (StrUtil.isBlank(account.getUsername()) || StrUtil.isBlank(account.getPassword())
                || ObjectUtil.isEmpty(account.getNewPassword())) {
            return Result.error(ResultCodeEnum.PARAM_LOST_ERROR); // 返回参数缺失错误
        }
        if (RoleEnum.ADMIN.name().equals(account.getRole())) {
            adminService.updatePassword(account); // 调用AdminService的修改密码方法
        }
        if (RoleEnum.DOCTOR.name().equals(account.getRole())) {
            doctorService.updatePassword(account); // 调用DoctorService的修改密码方法
        }
        if (RoleEnum.USER.name().equals(account.getRole())) {
            userService.updatePassword(account); // 调用UserService的修改密码方法
        }
        return Result.success(); // 返回成功结果
    }

}
