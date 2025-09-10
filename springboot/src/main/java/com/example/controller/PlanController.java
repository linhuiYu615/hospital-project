package com.example.controller;

import com.example.common.Result;
import com.example.pojo.entity.Plan;
import com.example.service.PlanService;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 排班信息表前端操作接口
 **/
@CrossOrigin
@RestController
@RequestMapping("/plan")
public class PlanController {

    @Resource
    private PlanService planService; // 注入PlanService依赖

    /**
     * 新增
     */
    @PostMapping("/add")
    public Result add(@RequestBody Plan plan) {
        planService.add(plan); // 调用PlanService的新增方法
        return Result.success(); // 返回成功结果
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete/{id}")
    public Result deleteById(@PathVariable Integer id) {
        planService.deleteById(id); // 调用PlanService的根据ID删除方法
        return Result.success(); // 返回成功结果
    }

    /**
     * 批量删除
     */
    @DeleteMapping("/delete/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        planService.deleteBatch(ids); // 调用PlanService的批量删除方法
        return Result.success(); // 返回成功结果
    }

    /**
     * 修改
     */
    @PutMapping("/update")
    public Result updateById(@RequestBody Plan plan) {
        planService.updateById(plan); // 调用PlanService的修改方法
        return Result.success(); // 返回成功结果
    }

    /**
     * 根据ID查询
     */
    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id) {
        Plan plan = planService.selectById(id); // 调用PlanService的根据ID查询方法
        return Result.success(plan); // 返回成功结果并携带查询到的Plan对象
    }

    /**
     * 查询所有
     */
    @GetMapping("/selectAll")
    public Result selectAll(Plan plan ) {
        List<Plan> list = planService.selectAll(plan); // 调用PlanService的查询所有方法
        return Result.success(list); // 返回成功结果并携带查询到的Plan对象列表
    }

    /**
     * 分页查询
     */
    @GetMapping("/selectPage")
    public Result selectPage(Plan plan,
                             @RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize) {
        PageInfo<Plan> page = planService.selectPage(plan, pageNum, pageSize); // 调用PlanService的分页查询方法
        return Result.success(page); // 返回成功结果并携带分页查询结果
    }

}