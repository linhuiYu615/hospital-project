package com.example.controller;

import com.example.common.Result;
import com.example.pojo.entity.Registration;
import com.example.service.RegistrationService;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 住院登记表前端操作接口
 **/
@CrossOrigin
@RestController
@RequestMapping("/registration")
public class RegistrationController {

    @Resource
    private RegistrationService registrationService; // 注入RegistrationService依赖

    /**
     * 新增
     */
    @PostMapping("/add")
    public Result add(@RequestBody Registration registration) {
        registrationService.add(registration); // 调用RegistrationService的新增方法
        return Result.success(); // 返回成功结果
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete/{id}")
    public Result deleteById(@PathVariable Integer id) {
        registrationService.deleteById(id); // 调用RegistrationService的根据ID删除方法
        return Result.success(); // 返回成功结果
    }

    /**
     * 批量删除
     */
    @DeleteMapping("/delete/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        registrationService.deleteBatch(ids); // 调用RegistrationService的批量删除方法
        return Result.success(); // 返回成功结果
    }

    /**
     * 修改
     */
    @PutMapping("/update")
    public Result updateById(@RequestBody Registration registration) {
        registrationService.updateById(registration); // 调用RegistrationService的修改方法
        return Result.success(); // 返回成功结果
    }

    /**
     * 根据ID查询
     */
    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id) {
        Registration registration = registrationService.selectById(id); // 调用RegistrationService的根据ID查询方法
        return Result.success(registration); // 返回成功结果并携带查询到的Registration对象
    }

    /**
     * 查询所有
     */
    @GetMapping("/selectAll")
    public Result selectAll(Registration registration ) {
        List<Registration> list = registrationService.selectAll(registration); // 调用RegistrationService的查询所有方法
        return Result.success(list); // 返回成功结果并携带查询到的Registration对象列表
    }

    /**
     * 分页查询
     */
    @GetMapping("/selectPage")
    public Result selectPage(Registration registration,
                             @RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize) {
        PageInfo<Registration> page = registrationService.selectPage(registration, pageNum, pageSize); // 调用RegistrationService的分页查询方法
        return Result.success(page); // 返回成功结果并携带分页查询结果
    }

}