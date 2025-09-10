package com.example.service;

import com.example.mapper.ChartsMapper;
import com.example.pojo.DTO.ChartsDTO;
import com.example.pojo.entity.Reserve;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ChartsService {

    @Autowired
    private ChartsMapper chartsMapper;

    /**
     * 获取当天每个科室的挂号数量
     */
    public ChartsDTO getTodayRegistrations() { // 获取当天挂号信息的方法

        String today = java.time.LocalDate.now().toString(); // 获取当天日期，格式为 "YYYY-MM-DD"

        // 查询每个科室的挂号数量
        List<Map<String, Object>> registrationCounts = chartsMapper.countRegistrationsByDepartmentAndDate(today);

        // 提取科室名称和对应的挂号数量
        List<String> departmentNames = registrationCounts.stream()
                .map(map -> (String) map.get("departmentName"))
                .collect(Collectors.toList());

        List<Long> counts = registrationCounts.stream()
                .map(map -> {
                    Object countObj = map.get("count");
                    if (countObj instanceof Long) {
                        return (Long) countObj;
                    } else if (countObj instanceof Integer) {
                        return ((Integer) countObj).longValue();
                    } else {
                        return 0L; // 默认值，避免空指针异常
                    }
                })
                .collect(Collectors.toList());

        return new ChartsDTO(today, departmentNames, counts); // 返回当天挂号信息
    }

    /**
     * 获取每个月每个科室的挂号数量
     */
    public List<ChartsDTO> getMonthlyRegistrations() { // 获取每月挂号信息的方法

        // 查询每个月每个科室的挂号数量
        List<Map<String, Object>> registrationCounts = chartsMapper.countRegistrationsByDepartmentAndMonth();

        // 使用 Map 进行分组，以月份为键
        Map<String, List<Map<String, Object>>> groupedByMonth = registrationCounts.stream()
                .filter(map -> map.get("date") != null) // 过滤掉 date 为 null 的记录
                .collect(Collectors.groupingBy(map -> (String) map.get("date")));

        List<ChartsDTO> monthlyCharts = new ArrayList<>();

        for (Map.Entry<String, List<Map<String, Object>>> entry : groupedByMonth.entrySet()) {
            String month = entry.getKey();
            List<Map<String, Object>> deptCounts = entry.getValue();

            List<String> departmentNames = deptCounts.stream()
                    .map(map -> (String) map.get("departmentName"))
                    .collect(Collectors.toList());

            List<Long> counts = deptCounts.stream()
                    .map(map -> {
                        Object countObj = map.get("count");
                        if (countObj instanceof Long) {
                            return (Long) countObj;
                        } else if (countObj instanceof Integer) {
                            return ((Integer) countObj).longValue();
                        } else {
                            return 0L;
                        }
                    })
                    .collect(Collectors.toList());

            monthlyCharts.add(new ChartsDTO(month, departmentNames, counts)); // 添加每月挂号信息
        }

        return monthlyCharts; // 返回每月挂号信息列表
    }
}