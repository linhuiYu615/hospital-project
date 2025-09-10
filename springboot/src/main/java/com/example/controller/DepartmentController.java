package com.example.controller;

import com.example.common.Result;
import com.example.pojo.entity.Department;
import com.example.service.DepartmentService;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 部门信息表前端操作接口
 **/
@CrossOrigin
@RestController
@RequestMapping("/department")
public class DepartmentController {

    @Resource
    private DepartmentService departmentService; // 注入DepartmentService依赖

    /**
     * 新增
     */
    @PostMapping("/add")
    public Result add(@RequestBody Department department) {
        departmentService.add(department); // 调用DepartmentService的新增方法
        return Result.success(); // 返回成功结果
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete/{id}")
    public Result deleteById(@PathVariable Integer id) {
        departmentService.deleteById(id); // 调用DepartmentService的根据ID删除方法
        return Result.success(); // 返回成功结果
    }

    /**
     * 批量删除
     */
    @DeleteMapping("/delete/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        departmentService.deleteBatch(ids); // 调用DepartmentService的批量删除方法
        return Result.success(); // 返回成功结果
    }

    /**
     * 修改
     */
    @PutMapping("/update")
    public Result updateById(@RequestBody Department department) {
        departmentService.updateById(department); // 调用DepartmentService的修改方法
        return Result.success(); // 返回成功结果
    }

    /**
     * 根据ID查询
     */
    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id) {
        Department department = departmentService.selectById(id); // 调用DepartmentService的根据ID查询方法
        return Result.success(department); // 返回成功结果并携带查询到的Department对象
    }

    /**
     * 查询所有
     */
    @GetMapping("/selectAll")
    public Result selectAll(Department department) {
        List<Department> list = departmentService.selectAll(department); // 调用DepartmentService的查询所有方法
        return Result.success(list); // 返回成功结果并携带查询到的Department对象列表
    }

    /**
     * 分页查询
     */
    @GetMapping("/selectPage")
    public Result selectPage(Department department,
                             @RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize) {
        PageInfo<Department> page = departmentService.selectPage(department, pageNum, pageSize); // 调用DepartmentService的分页查询方法
        return Result.success(page); // 返回成功结果并携带分页查询结果
    }

}