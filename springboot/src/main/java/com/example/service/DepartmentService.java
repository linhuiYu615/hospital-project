package com.example.service;

import com.example.pojo.entity.Department;
import com.example.mapper.DepartmentMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 科室信息表业务处理
 **/
@Service
public class DepartmentService { // DepartmentService类，处理科室相关业务逻辑

    @Resource // 注入DepartmentMapper实例
    private DepartmentMapper departmentMapper; // DepartmentMapper实例

    /**
     * 新增
     */
    public void add(Department department) { // 新增科室方法
        departmentMapper.insert(department); // 调用Mapper插入科室信息
    }

    /**
     * 删除
     */
    public void deleteById(Integer id) { // 删除科室方法
        departmentMapper.deleteById(id); // 调用Mapper根据ID删除科室信息
    }

    /**
     * 批量删除
     */
    public void deleteBatch(List<Integer> ids) { // 批量删除科室方法
        for (Integer id : ids) { // 遍历ID列表
            departmentMapper.deleteById(id); // 逐个删除科室信息
        }
    }

    /**
     * 修改
     */
    public void updateById(Department department) { // 修改科室方法
        departmentMapper.updateById(department); // 调用Mapper更新科室信息
    }

    /**
     * 根据ID查询
     */
    public Department selectById(Integer id) { // 根据ID查询科室方法
        return departmentMapper.selectById(id); // 返回根据ID查询的科室信息
    }

    /**
     * 查询所有
     */
    public List<Department> selectAll(Department department) { // 查询所有科室方法
        return departmentMapper.selectAll(department); // 返回所有科室信息列表
    }

    /**
     * 分页查询
     */
    public PageInfo<Department> selectPage(Department department, Integer pageNum, Integer pageSize) { // 分页查询科室方法
        PageHelper.startPage(pageNum, pageSize); // 开始分页
        List<Department> list = departmentMapper.selectAll(department); // 查询所有科室信息
        return PageInfo.of(list); // 返回分页信息
    }

}