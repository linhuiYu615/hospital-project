package com.example.service;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.example.common.Constants;
import com.example.common.enums.ResultCodeEnum;
import com.example.common.enums.RoleEnum;
import com.example.pojo.entity.*;
import com.example.exception.CustomException;
import com.example.mapper.DepartmentMapper;
import com.example.mapper.DoctorMapper;
import com.example.mapper.PlanMapper;
import com.example.mapper.ReserveMapper;
import com.example.utils.TokenUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * 医生业务处理
 **/
@Service
public class DoctorService {

    @Resource
    private DoctorMapper doctorMapper;
    @Resource
    private DepartmentMapper departmentMapper;
    @Resource
    private ReserveMapper reserveMapper;
    @Resource
    private PlanMapper planMapper;

    /**
     * 新增
     */
    public void add(Doctor doctor) { // 添加医生方法
        Doctor dbDoctor = doctorMapper.selectByUsername(doctor.getUsername()); // 根据用户名查询医生
        if (ObjectUtil.isNotNull(dbDoctor)) { // 如果医生已存在
            throw new CustomException(ResultCodeEnum.USER_EXIST_ERROR); // 抛出自定义异常
        }
        if (ObjectUtil.isEmpty(doctor.getPassword())) { // 如果密码为空
            doctor.setPassword(Constants.USER_DEFAULT_PASSWORD); // 设置默认密码
        }
        if (ObjectUtil.isEmpty(doctor.getName())) { // 如果姓名为空
            doctor.setName(doctor.getUsername()); // 使用用户名作为姓名
        }
        doctor.setRole(RoleEnum.DOCTOR.name()); // 设置医生角色
        doctorMapper.insert(doctor); // 插入医生信息
    }

    /**
     * 删除
     */
    public void deleteById(Integer id) { // 删除医生方法
        doctorMapper.deleteById(id); // 根据ID删除医生信息
    }

    /**
     * 批量删除
     */
    public void deleteBatch(List<Integer> ids) { // 批量删除医生方法
        for (Integer id : ids) { // 遍历ID列表
            doctorMapper.deleteById(id); // 逐个删除医生信息
        }
    }

    /**
     * 修改
     */
    public void updateById(Doctor doctor) { // 修改医生方法
        doctorMapper.updateById(doctor); // 更新医生信息
    }

    /**
     * 根据ID查询
     */
    public Doctor selectById(Integer id) { // 根据ID查询医生方法
        return doctorMapper.selectById(id); // 返回根据ID查询的医生信息
    }

    /**
     * 查询所有
     */
    public List<Doctor> selectAll(Doctor doctor) { // 查询所有医生方法
        return doctorMapper.selectAll(doctor); // 返回所有医生信息列表
    }

    /**
     * 分页查询
     */
    public PageInfo<Doctor> selectPage(Doctor doctor, Integer pageNum, Integer pageSize) { // 分页查询医生方法
        PageHelper.startPage(pageNum, pageSize); // 开始分页
        List<Doctor> list = doctorMapper.selectAll(doctor); // 查询所有医生信息
        return PageInfo.of(list); // 返回分页信息
    }

    //医生挂号页面的分页查询
    public PageInfo<Doctor> selectPage2(Doctor doctor, Integer pageNum, Integer pageSize) { // 医生挂号页面的分页查询方法
        String today = DateUtil.format(new Date(), "yyyy-MM-dd"); // 获取当前日期
        //查询在诊医生的时候，除了根据科室，还要根据（星期几），筛选出当天在诊的医生
        String week = getTodayWeek(); // 获取当天是星期几
        doctor.setWeek(week); // 设置医生的星期
        PageHelper.startPage(pageNum, pageSize); // 开始分页
        List<Doctor> list = doctorMapper.selectAll(doctor); // 查询所有医生信息
        //计算查出来的在诊医生剩余多少个号
        for (Doctor dbDoctor : list) { // 遍历医生列表
            Reserve reserve = new Reserve(); // 创建预约对象
            reserve.setDoctorId(dbDoctor.getId()); // 设置医生ID
            reserve.setTime(today); // 设置时间为当天
            List<Reserve> reserves = reserveMapper.selectAll(reserve); // 查询当天已预约该医生的数量
            Plan plan = planMapper.selectByDoctorIdAndWeek(dbDoctor.getId(), week); // 查询医生的排班信息
            //用总数量-已经挂过的号数量 = 剩余挂号数量
            dbDoctor.setNum(plan.getNum() - reserves.size()); // 计算剩余挂号数量
        }
        return PageInfo.of(list); // 返回分页信息
    }

    /**
     * 登录
     */
    public Account login(Account account) { // 登录方法
        Doctor dbDoctor = doctorMapper.selectByUsername(account.getUsername()); // 根据用户名查询医生
        if (ObjectUtil.isNull(dbDoctor)) { // 如果医生不存在
            throw new CustomException(ResultCodeEnum.USER_NOT_EXIST_ERROR); // 抛出用户不存在异常
        }
        if (!account.getPassword().equals(dbDoctor.getPassword())) { // 如果密码不匹配
            throw new CustomException(ResultCodeEnum.USER_ACCOUNT_ERROR); // 抛出用户账号错误异常
        }
        // 生成token
        String tokenData = dbDoctor.getId() + "-" + RoleEnum.DOCTOR.name(); // 生成token数据
        String token = TokenUtils.createToken(tokenData, dbDoctor.getPassword()); // 创建token
        dbDoctor.setToken(token); // 设置token
        if (ObjectUtil.isNotEmpty(dbDoctor.getDepartmentId())) { // 如果科室ID不为空
            Department department = departmentMapper.selectById(dbDoctor.getDepartmentId()); // 查询科室信息
            if (ObjectUtil.isNotNull(department)) { // 如果科室信息不为空
                dbDoctor.setDepartmentName(department.getName()); // 设置科室名称
            }
        }
        return dbDoctor; // 返回医生信息
    }

    /**
     * 修改密码
     */
    public void updatePassword(Account account) { // 修改密码方法
        Doctor dbDoctor = doctorMapper.selectByUsername(account.getUsername()); // 根据用户名查询医生
        if (ObjectUtil.isNull(dbDoctor)) { // 如果医生不存在
            throw new CustomException(ResultCodeEnum.USER_NOT_EXIST_ERROR); // 抛出用户不存在异常
        }
        if (!account.getPassword().equals(dbDoctor.getPassword())) { // 如果密码不匹配
            throw new CustomException(ResultCodeEnum.PARAM_PASSWORD_ERROR); // 抛出密码错误异常
        }
        dbDoctor.setPassword(account.getNewPassword()); // 设置新密码
        doctorMapper.updateById(dbDoctor); // 更新医生信息
    }

    /*
     * 獲取當天是星期幾
     * */
    private String getTodayWeek() { // 获取当天是星期几的方法
        LocalDate today = LocalDate.now(); // 获取当前日期
        DayOfWeek dayOfWeek = today.getDayOfWeek(); // 获取星期几
        return dayOfWeek.getDisplayName(TextStyle.FULL_STANDALONE, Locale.CHINA); // 返回中文格式的星期几
    }
}
