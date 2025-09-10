package com.example.mapper;

import com.example.pojo.DTO.ChartsDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

@Mapper
public interface ChartsMapper {

    // 查询某一天各科室的挂号数量
    @Select("SELECT d.name AS departmentName, COUNT(r.id) AS count " +
            "FROM reserve r " +
            "JOIN doctor doc ON r.doctor_id = doc.id " +
            "JOIN department d ON doc.department_id = d.id " +
            "WHERE DATE(r.time) = #{date} " + // 根据日期筛选挂号记录
            "GROUP BY d.name " + // 按科室名称分组
            "ORDER BY d.name") // 按科室名称排序
    List<Map<String, Object>> countRegistrationsByDepartmentAndDate(String date);

    // 查询各月份各科室的挂号数量
    @Select("SELECT DATE_FORMAT(r.time, '%Y-%m') AS date, d.name AS departmentName, COUNT(*) AS count " +
            "FROM reserve r " +
            "JOIN doctor doc ON r.doctor_id = doc.id " +
            "JOIN department d ON doc.department_id = d.id " +
            "WHERE r.time IS NOT NULL " + // 排除 r.time 为 null 的记录
            "GROUP BY date, departmentName " + // 按日期和科室名称分组
            "ORDER BY date, departmentName") // 按日期和科室名称排序
    List<Map<String, Object>> countRegistrationsByDepartmentAndMonth();
}