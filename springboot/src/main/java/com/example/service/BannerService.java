package com.example.service;

import com.example.pojo.entity.Banner;
import com.example.mapper.BannerMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Banner信息表业务处理
 **/
@Service
public class BannerService {

    @Resource
    private BannerMapper bannerMapper;

    /**
     * 新增
     */
    public void add(Banner banner) { // 添加Banner方法
        bannerMapper.insert(banner); // 调用Mapper插入Banner信息
    }

    /**
     * 根据ID删除Banner
     */
    public void deleteById(Integer id) { // 删除Banner方法
        bannerMapper.deleteById(id); // 调用Mapper根据ID删除Banner信息
    }

    /**
     * 批量删除Banner
     */
    public void deleteBatch(List<Integer> ids) { // 批量删除Banner方法
        for (Integer id : ids) { // 遍历ID列表
            bannerMapper.deleteById(id); // 逐个删除Banner信息
        }
    }

    /**
     * 修改Banner信息
     */
    public void updateById(Banner banner) { // 修改Banner方法
        bannerMapper.updateById(banner); // 调用Mapper更新Banner信息
    }

    /**
     * 根据ID查询Banner
     */
    public Banner selectById(Integer id) { // 根据ID查询Banner方法
        return bannerMapper.selectById(id); // 返回根据ID查询的Banner信息
    }

    /**
     * 查询所有Banner
     */
    public List<Banner> selectAll(Banner banner) { // 查询所有Banner方法
        return bannerMapper.selectAll(banner); // 返回所有Banner信息列表
    }

    /**
     * 分页查询Banner
     */
    public PageInfo<Banner> selectPage(Banner banner, Integer pageNum, Integer pageSize) { // 分页查询Banner方法
        PageHelper.startPage(pageNum, pageSize); // 开始分页
        List<Banner> list = bannerMapper.selectAll(banner); // 查询所有Banner信息
        return PageInfo.of(list); // 返回分页信息
    }

}