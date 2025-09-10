package com.example.service;

import cn.hutool.core.util.ObjectUtil;
import com.example.common.enums.ResultCodeEnum;
import com.example.common.enums.RoleEnum;
import com.example.pojo.entity.Account;
import com.example.pojo.entity.Plan;
import com.example.exception.CustomException;
import com.example.mapper.PlanMapper;
import com.example.utils.TokenUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 排班信息表业务处理
 **/
@Service
public class PlanService {

    @Resource
    private PlanMapper planMapper;

    /**
     * 新增
     */
    public void add(Plan plan) { // 添加排班方法
        //要先做一次排班的校验：同一个医生同一天只能有一个排班记录
        //根据医生的id和星期几查询有没有记录
        Plan dbPlan = planMapper.selectByDoctorIdAndWeek(plan.getDoctorId(), plan.getWeek()); // 根据医生ID和星期查询排班记录
        if (ObjectUtil.isNotEmpty(dbPlan)) { // 如果存在排班记录
            throw new CustomException(ResultCodeEnum.PLAN_EXIST_ERROR); // 抛出排班已存在异常
        }
        planMapper.insert(plan); // 插入排班信息
    }

    /**
     * 删除
     */
    public void deleteById(Integer id) { // 删除排班方法
        planMapper.deleteById(id); // 根据ID删除排班信息
    }

    /**
     * 批量删除
     */
    public void deleteBatch(List<Integer> ids) { // 批量删除排班方法
        for (Integer id : ids) { // 遍历ID列表
            planMapper.deleteById(id); // 逐个删除排班信息
        }
    }

    /**
     * 修改
     */
    public void updateById(Plan plan) { // 修改排班方法
        Plan dbPlan = planMapper.selectByDoctorIdAndWeek(plan.getDoctorId(), plan.getWeek()); // 根据医生ID和星期查询排班记录
        if (ObjectUtil.isNotEmpty(dbPlan) && !plan.getId().equals(dbPlan.getId())) { // 如果存在排班记录且不是当前要修改的排班记录
            throw new CustomException(ResultCodeEnum.PLAN_EXIST_ERROR); // 抛出排班已存在异常
        }
        planMapper.updateById(plan); // 更新排班信息
    }

    /**
     * 根据ID查询
     */
    public Plan selectById(Integer id) { // 根据ID查询排班方法
        return planMapper.selectById(id); // 返回根据ID查询的排班信息
    }

    /**
     * 查询所有
     */
    public List<Plan> selectAll(Plan plan) { // 查询所有排班方法
        return planMapper.selectAll(plan); // 返回所有排班信息列表
    }

    /**
     * 分页查询
     */
    public PageInfo<Plan> selectPage(Plan plan, Integer pageNum, Integer pageSize) { // 分页查询排班方法
        Account currentUser = TokenUtils.getCurrentUser(); // 获取当前登录用户信息
        if (RoleEnum.DOCTOR.name().equals(currentUser.getRole())) { // 如果当前用户是医生
            plan.setDoctorId(currentUser.getId()); // 设置查询条件为当前医生的ID
        }
        PageHelper.startPage(pageNum, pageSize); // 开始分页
        List<Plan> list = planMapper.selectAll(plan); // 查询所有排班信息
        return PageInfo.of(list); // 返回分页信息
    }
}