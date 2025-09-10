package com.example.controller;

import com.example.common.Result;
import com.example.pojo.DTO.ChartsDTO;
import com.example.service.ChartsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/charts")
public class ChartsController {

    @Resource
    private ChartsService chartsService; // 注入ChartsService依赖

    /*
     * 每个科室的当天挂号量 柱状图
     */
    @GetMapping("/dailyReserve")
    public Result getTodayRegistrations() {
        ChartsDTO data = chartsService.getTodayRegistrations(); // 调用ChartsService的获取当天挂号量方法
        return Result.success(data); // 返回成功结果并携带当天挂号量数据
    }

    /*
     * 每个月每个科室的挂号量 柱状图
     */
    @GetMapping("/monthlyReserve")
    public Result getMonthlyRegistrations() {
        List<ChartsDTO> data = chartsService.getMonthlyRegistrations(); // 调用ChartsService的获取每月挂号量方法
        return Result.success(data); // 返回成功结果并携带每月挂号量数据
    }
}