package com.example.controller;

import com.example.common.Result;
import com.example.pojo.entity.Banner;
import com.example.service.BannerService;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/banner")

public class BannerController {
    @Resource
    private BannerService bannerService; // 注入BannerService依赖

    /**
     * 新增
     */
    @PostMapping("/add")
    public Result add(@RequestBody Banner banner) {
        bannerService.add(banner); // 调用BannerService的新增方法
        return Result.success(); // 返回成功结果
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete/{id}")
    public Result deleteById(@PathVariable Integer id) {
        bannerService.deleteById(id); // 调用BannerService的根据ID删除方法
        return Result.success(); // 返回成功结果
    }

    /**
     * 批量删除
     */
    @DeleteMapping("/delete/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        bannerService.deleteBatch(ids); // 调用BannerService的批量删除方法
        return Result.success(); // 返回成功结果
    }

    /**
     * 修改
     */
    @PutMapping("/update")
    public Result updateById(@RequestBody Banner banner) {
        bannerService.updateById(banner); // 调用BannerService的修改方法
        return Result.success(); // 返回成功结果
    }

    /**
     * 根据ID查询
     */
    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id) {
        Banner banner = bannerService.selectById(id); // 调用BannerService的根据ID查询方法
        return Result.success(banner); // 返回成功结果并携带查询到的Banner对象
    }

    /**
     * 查询所有
     */
    @GetMapping("/selectAll")
    public Result selectAll(Banner banner) {
        List<Banner> list = bannerService.selectAll(banner); // 调用BannerService的查询所有方法
        return Result.success(list); // 返回成功结果并携带查询到的Banner对象列表
    }

    /**
     * 分页查询
     */
    @GetMapping("/selectPage")
    public Result selectPage(Banner banner,
                             @RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize) {
        PageInfo<Banner> page = bannerService.selectPage(banner, pageNum, pageSize); // 调用BannerService的分页查询方法
        return Result.success(page); // 返回成功结果并携带分页查询结果
    }
}