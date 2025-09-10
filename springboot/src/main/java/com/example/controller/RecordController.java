package com.example.controller;

import com.example.common.Result;
import com.example.pojo.entity.Record;
import com.example.service.RecordService;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 就诊记录表前端操作接口
 **/
@CrossOrigin
@RestController
@RequestMapping("/record")
public class RecordController {

    @Resource
    private RecordService recordService; // 注入RecordService依赖

    /**
     * 新增
     */
    @PostMapping("/add")
    public Result add(@RequestBody Record record) {
        recordService.add(record); // 调用RecordService的新增方法
        return Result.success(); // 返回成功结果
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete/{id}")
    public Result deleteById(@PathVariable Integer id) {
        recordService.deleteById(id); // 调用RecordService的根据ID删除方法
        return Result.success(); // 返回成功结果
    }

    /**
     * 批量删除
     */
    @DeleteMapping("/delete/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        recordService.deleteBatch(ids); // 调用RecordService的批量删除方法
        return Result.success(); // 返回成功结果
    }

    /**
     * 修改
     */
    @PutMapping("/update")
    public Result updateById(@RequestBody Record record) {
        recordService.updateById(record); // 调用RecordService的修改方法
        return Result.success(); // 返回成功结果
    }

    /**
     * 根据ID查询
     */
    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id) {
        Record record = recordService.selectById(id); // 调用RecordService的根据ID查询方法
        return Result.success(record); // 返回成功结果并携带查询到的Record对象
    }

    /**
     * 查询所有
     */
    @GetMapping("/selectAll")
    public Result selectAll(Record record ) {
        List<Record> list = recordService.selectAll(record); // 调用RecordService的查询所有方法
        return Result.success(list); // 返回成功结果并携带查询到的Record对象列表
    }

    /**
     * 分页查询
     */
    @GetMapping("/selectPage")
    public Result selectPage(Record record,
                             @RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize) {
        PageInfo<Record> page = recordService.selectPage(record, pageNum, pageSize); // 调用RecordService的分页查询方法
        return Result.success(page); // 返回成功结果并携带分页查询结果
    }

}