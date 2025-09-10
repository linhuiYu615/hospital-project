package com.example.service;

import cn.hutool.core.date.DateUtil;
import com.example.pojo.entity.Account;
import com.example.pojo.entity.Notice;
import com.example.mapper.NoticeMapper;
import com.example.utils.TokenUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * 公告信息表业务处理
 **/
@Service
public class NoticeService {

    @Resource
    private NoticeMapper noticeMapper;

    /**
     * 新增
     */
    public void add(Notice notice) { // 添加公告方法
        notice.setTime(DateUtil.today()); // 设置公告时间为当天
        Account currentUser = TokenUtils.getCurrentUser(); // 获取当前登录用户信息
        notice.setUser(currentUser.getUsername()); // 设置公告用户为当前用户的用户名
        noticeMapper.insert(notice); // 插入公告信息
    }

    /**
     * 删除
     */
    public void deleteById(Integer id) { // 删除公告方法
        noticeMapper.deleteById(id); // 根据ID删除公告信息
    }

    /**
     * 批量删除
     */
    public void deleteBatch(List<Integer> ids) { // 批量删除公告方法
        for (Integer id : ids) { // 遍历ID列表
            noticeMapper.deleteById(id); // 逐个删除公告信息
        }
    }

    /**
     * 修改
     */
    public void updateById(Notice notice) { // 修改公告方法
        noticeMapper.updateById(notice); // 更新公告信息
    }

    /**
     * 根据ID查询
     */
    public Notice selectById(Integer id) { // 根据ID查询公告方法
        return noticeMapper.selectById(id); // 返回根据ID查询的公告信息
    }

    /**
     * 查询所有
     */
    public List<Notice> selectAll(Notice notice) { // 查询所有公告方法
        return noticeMapper.selectAll(notice); // 返回所有公告信息列表
    }

    /**
     * 分页查询
     */
    public PageInfo<Notice> selectPage(Notice notice, Integer pageNum, Integer pageSize) { // 分页查询公告方法
        PageHelper.startPage(pageNum, pageSize); // 开始分页
        List<Notice> list = noticeMapper.selectAll(notice); // 查询所有公告信息
        return PageInfo.of(list); // 返回分页信息
    }
}