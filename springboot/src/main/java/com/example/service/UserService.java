package com.example.service;

import cn.hutool.core.util.ObjectUtil;
import com.example.common.Constants;
import com.example.common.enums.ResultCodeEnum;
import com.example.common.enums.RoleEnum;
import com.example.pojo.entity.Account;
import com.example.pojo.entity.User;
import com.example.exception.CustomException;
import com.example.mapper.UserMapper;
import com.example.utils.TokenUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 患者业务处理
 **/
@Service
public class UserService {

    @Resource
    private UserMapper userMapper;

    /**
     * 新增
     */
    public void add(User user) {
        User dbUser = userMapper.selectByUsername(user.getUsername()); // 根据用户名查询数据库中是否已存在该用户
        if (ObjectUtil.isNotNull(dbUser)) { // 如果数据库中已存在该用户
            throw new CustomException(ResultCodeEnum.USER_EXIST_ERROR); // 抛出用户已存在异常
        }
        if (ObjectUtil.isEmpty(user.getPassword())) { // 如果用户密码为空
            user.setPassword(Constants.USER_DEFAULT_PASSWORD); // 设置默认密码
        }
        if (ObjectUtil.isEmpty(user.getName())) { // 如果用户姓名为空
            user.setName(user.getUsername()); // 使用用户名作为姓名
        }
        user.setRole(RoleEnum.USER.name()); // 设置用户角色为普通用户
        userMapper.insert(user); // 插入用户信息
    }

    /**
     * 删除
     */
    public void deleteById(Integer id) {
        userMapper.deleteById(id); // 根据ID删除用户信息
    }

    /**
     * 批量删除
     */
    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) { // 遍历ID列表
            userMapper.deleteById(id); // 逐个删除用户信息
        }
    }

    /**
     * 修改
     */
    public void updateById(User user) {
        userMapper.updateById(user); // 更新用户信息
    }

    /**
     * 根据ID查询
     */
    public User selectById(Integer id) {
        User dbUser = userMapper.selectById(id); // 根据ID查询用户信息
        // 生成token
        String tokenData = dbUser.getId() + "-" + RoleEnum.USER.name(); // 生成token的数据
        String token = TokenUtils.createToken(tokenData, dbUser.getPassword()); // 创建token
        dbUser.setToken(token); // 设置用户的token
        return dbUser; // 返回用户信息
    }

    /**
     * 查询所有
     */
    public List<User> selectAll(User user) {
        return userMapper.selectAll(user); // 查询所有用户信息
    }

    /**
     * 分页查询
     */
    public PageInfo<User> selectPage(User user, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize); // 开始分页
        List<User> list = userMapper.selectAll(user); // 查询所有用户信息
        return PageInfo.of(list); // 返回分页信息
    }

    /**
     * 登录
     */
    public Account login(Account account) {
        Account dbUser = userMapper.selectByUsername(account.getUsername()); // 根据用户名查询用户信息
        if (ObjectUtil.isNull(dbUser)) { // 如果用户不存在
            throw new CustomException(ResultCodeEnum.USER_NOT_EXIST_ERROR); // 抛出用户不存在异常
        }
        if (!account.getPassword().equals(dbUser.getPassword())) { // 如果密码不匹配
            throw new CustomException(ResultCodeEnum.USER_ACCOUNT_ERROR); // 抛出用户账号异常
        }
        // 生成token
        String tokenData = dbUser.getId() + "-" + RoleEnum.USER.name(); // 生成token的数据
        String token = TokenUtils.createToken(tokenData, dbUser.getPassword()); // 创建token
        dbUser.setToken(token); // 设置用户的token
        return dbUser; // 返回用户信息
    }

    /**
     * 注册
     */
    public void register(Account account) {
        User user = new User(); // 创建新用户
        BeanUtils.copyProperties(account, user); // 复制属性
        add(user); // 添加用户信息
    }

    /**
     * 修改密码
     */
    public void updatePassword(Account account) {
        User dbUser = userMapper.selectByUsername(account.getUsername()); // 根据用户名查询用户信息
        if (ObjectUtil.isNull(dbUser)) { // 如果用户不存在
            throw new CustomException(ResultCodeEnum.USER_NOT_EXIST_ERROR); // 抛出用户不存在异常
        }
        if (!account.getPassword().equals(dbUser.getPassword())) { // 如果密码不匹配
            throw new CustomException(ResultCodeEnum.PARAM_PASSWORD_ERROR); // 抛出密码错误异常
        }
        dbUser.setPassword(account.getNewPassword()); // 更新密码为新密码
        userMapper.updateById(dbUser); // 更新用户信息
    }
}