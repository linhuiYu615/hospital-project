package com.example.service;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import com.example.common.enums.RoleEnum;
import com.example.common.enums.StatusEnum;
import com.example.pojo.entity.Account;
import com.example.pojo.entity.Record;
import com.example.mapper.RecordMapper;
import com.example.utils.TokenUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * 就诊记录表业务处理
 **/
@Service
public class RecordService {

    @Resource
    private RecordMapper recordMapper;

    /**
     * 新增
     */
    public void add(Record record) { // 添加就诊记录方法
        //先去查看该患者、该医生在当天是否已经有了就诊记录
        String time = DateUtil.format(new Date(),"yyyy-MM-dd"); // 获取当前日期的格式化字符串
        record.setTime(time); // 设置就诊记录的时间为当天日期
        List<Record> records = recordMapper.selectAll(record); // 查询所有符合条件的就诊记录
        if (CollectionUtil.isEmpty(records)){ // 如果就诊记录列表为空
            record.setInhospitalRecord(StatusEnum.NO.status); // 设置住院记录状态为未住院
            recordMapper.insert(record); // 插入就诊记录
        }
    }

    /**
     * 删除
     */
    public void deleteById(Integer id) { // 删除就诊记录方法
        recordMapper.deleteById(id); // 根据ID删除就诊记录
    }

    /**
     * 批量删除
     */
    public void deleteBatch(List<Integer> ids) { // 批量删除就诊记录方法
        for (Integer id : ids) { // 遍历ID列表
            recordMapper.deleteById(id); // 逐个删除就诊记录
        }
    }

    /**
     * 修改
     */
    public void updateById(Record record) { // 修改就诊记录方法
        recordMapper.updateById(record); // 更新就诊记录
    }

    /**
     * 根据ID查询
     */
    public Record selectById(Integer id) { // 根据ID查询就诊记录方法
        return recordMapper.selectById(id); // 返回根据ID查询的就诊记录
    }

    /**
     * 查询所有
     */
    public List<Record> selectAll(Record record) { // 查询所有就诊记录方法
        return recordMapper.selectAll(record); // 返回所有就诊记录列表
    }

    /**
     * 分页查询
     */
    public PageInfo<Record> selectPage(Record record, Integer pageNum, Integer pageSize) { // 分页查询就诊记录方法
        Account currentUser = TokenUtils.getCurrentUser(); // 获取当前登录用户信息
        if (RoleEnum.USER.name().equals(currentUser.getRole())){ // 如果当前用户是普通用户
            record.setUserId(currentUser.getId()); // 设置查询条件为当前用户的ID
        }
        if (RoleEnum.DOCTOR.name().equals(currentUser.getRole())){ // 如果当前用户是医生
            record.setDoctorId(currentUser.getId()); // 设置查询条件为当前医生的ID
        }
        PageHelper.startPage(pageNum, pageSize); // 开始分页
        List<Record> list = recordMapper.selectAll(record); // 查询所有就诊记录
        return PageInfo.of(list); // 返回分页信息
    }
}