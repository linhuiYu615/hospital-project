package com.example.controller;

import com.example.common.Result;
import com.example.pojo.entity.Admin;
import com.example.service.AdminService;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.util.List;

/**
 * 管理员前端操作接口
 **/
@CrossOrigin
@RestController
@RequestMapping("/admin")
public class AdminController {

    @Resource
    private AdminService adminService; // 注入AdminService依赖

    /**
     * 新增
     */
    @PostMapping("/add")
    public Result add(@RequestBody Admin admin) {
        adminService.add(admin); // 调用AdminService的新增方法
        return Result.success(); // 返回成功结果
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete/{id}")
    public Result deleteById(@PathVariable Integer id) {
        adminService.deleteById(id); // 调用AdminService的根据ID删除方法
        return Result.success(); // 返回成功结果
    }

    /**
     * 批量删除
     */
    @DeleteMapping("/delete/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        adminService.deleteBatch(ids); // 调用AdminService的批量删除方法
        return Result.success(); // 返回成功结果
    }

    /**
     * 修改
     */
    @PutMapping("/update")
    public Result updateById(@RequestBody Admin admin) {
        adminService.updateById(admin); // 调用AdminService的修改方法
        return Result.success(); // 返回成功结果
    }

    /**
     * 根据ID查询
     */
    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id) {
        Admin admin = adminService.selectById(id); // 调用AdminService的根据ID查询方法
        return Result.success(admin); // 返回成功结果并携带查询到的Admin对象
    }

    /**
     * 查询所有
     */
    @GetMapping("/selectAll")
    public Result selectAll(Admin admin ) {
        List<Admin> list = adminService.selectAll(admin); // 调用AdminService的查询所有方法
        return Result.success(list); // 返回成功结果并携带查询到的Admin对象列表
    }

    /**
     * 分页查询
     */
    @GetMapping("/selectPage")
    public Result selectPage(Admin admin,
                             @RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize) {
        PageInfo<Admin> page = adminService.selectPage(admin, pageNum, pageSize); // 调用AdminService的分页查询方法
        return Result.success(page); // 返回成功结果并携带分页查询结果
    }

}


