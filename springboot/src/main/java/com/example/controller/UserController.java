package com.example.controller;

import com.example.common.Result;
import com.example.pojo.entity.User;
import com.example.service.UserService;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 用户前端操作接口
 **/
@CrossOrigin
@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService; // 注入UserService依赖

    /**
     * 新增
     */
    @PostMapping("/add")
    public Result add(@RequestBody User user) {
        userService.add(user); // 调用UserService的新增方法
        return Result.success(); // 返回成功结果
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete/{id}")
    public Result deleteById(@PathVariable Integer id) {
        userService.deleteById(id); // 调用UserService的根据ID删除方法
        return Result.success(); // 返回成功结果
    }

    /**
     * 批量删除
     */
    @DeleteMapping("/delete/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        userService.deleteBatch(ids); // 调用UserService的批量删除方法
        return Result.success(); // 返回成功结果
    }

    /**
     * 修改
     */
    @PutMapping("/update")
    public Result updateById(@RequestBody User user) {
        userService.updateById(user); // 调用UserService的修改方法
        return Result.success(); // 返回成功结果
    }

    /**
     * 根据ID查询
     */
    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id) {
        User user = userService.selectById(id); // 调用UserService的根据ID查询方法
        return Result.success(user); // 返回成功结果并携带查询到的User对象
    }

    /**
     * 查询所有
     */
    @GetMapping("/selectAll")
    public Result selectAll(User user ) {
        List<User> list = userService.selectAll(user); // 调用UserService的查询所有方法
        return Result.success(list); // 返回成功结果并携带查询到的User对象列表
    }

    /**
     * 分页查询
     */
    @GetMapping("/selectPage")
    public Result selectPage(User user,
                             @RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize) {
        PageInfo<User> page = userService.selectPage(user, pageNum, pageSize); // 调用UserService的分页查询方法
        return Result.success(page); // 返回成功结果并携带分页查询结果
    }

}