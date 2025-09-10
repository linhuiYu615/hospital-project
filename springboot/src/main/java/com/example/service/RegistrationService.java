package com.example.service;

import cn.hutool.core.collection.CollectionUtil;
import com.example.common.enums.RoleEnum;
import com.example.pojo.entity.Account;
import com.example.pojo.entity.Registration;
import com.example.mapper.RegistrationMapper;
import com.example.utils.TokenUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 公告信息表业务处理
 **/
@Service
public class RegistrationService {

    @Resource
    private RegistrationMapper registrationMapper;

    /**
     * 新增
     */
    public void add(Registration registration) { // 添加挂号记录方法
        // 判断该患者是否正在住院中，如果是，不需要生成一条新的住院记录；如果已出院或者没有住院，则生成一条新的住院记录
        // Registration dbRegistration = registrationMapper.selectByUserIdInHospital(registration.getUserId()); // 根据用户ID查询是否有正在住院的记录
        List<Registration> registrations = registrationMapper.selectAll(registration); // 查询所有符合条件的挂号记录
        if (CollectionUtil.isEmpty(registrations)) { // 如果挂号记录列表为空
            registrationMapper.insert(registration); // 插入挂号记录
        }
    }

    /**
     * 删除
     */
    public void deleteById(Integer id) { // 删除挂号记录方法
        registrationMapper.deleteById(id); // 根据ID删除挂号记录
    }

    /**
     * 批量删除
     */
    public void deleteBatch(List<Integer> ids) { // 批量删除挂号记录方法
        for (Integer id : ids) { // 遍历ID列表
            registrationMapper.deleteById(id); // 逐个删除挂号记录
        }
    }

    /**
     * 修改
     */
    public void updateById(Registration registration) { // 修改挂号记录方法
        registrationMapper.updateById(registration); // 更新挂号记录
    }

    /**
     * 根据ID查询
     */
    public Registration selectById(Integer id) { // 根据ID查询挂号记录方法
        return registrationMapper.selectById(id); // 返回根据ID查询的挂号记录
    }

    /**
     * 查询所有
     */
    public List<Registration> selectAll(Registration registration) { // 查询所有挂号记录方法
        return registrationMapper.selectAll(registration); // 返回所有挂号记录列表
    }

    /**
     * 分页查询
     */
    public PageInfo<Registration> selectPage(Registration registration, Integer pageNum, Integer pageSize) { // 分页查询挂号记录方法
        Account currentUser = TokenUtils.getCurrentUser(); // 获取当前登录用户信息
        if (RoleEnum.USER.name().equals(currentUser.getRole())) { // 如果当前用户是普通用户
            registration.setUserId(currentUser.getId()); // 设置查询条件为当前用户的ID
        }
        PageHelper.startPage(pageNum, pageSize); // 开始分页
        List<Registration> list = registrationMapper.selectAll(registration); // 查询所有挂号记录
        return PageInfo.of(list); // 返回分页信息
    }
}