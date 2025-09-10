package com.example.mapper;

import com.example.pojo.entity.Banner;

import java.util.List;

public interface BannerMapper {
    /**
     * 新增
     */
    int insert(Banner banner);

    /**
     * 删除
     */
    int deleteById(Integer id);

    /**
     * 修改
     */
    int updateById(Banner banner);

    /**
     * 根据ID查询
     */
    Banner selectById(Integer id);

    /**
     * 查询所有
     */
    List<Banner> selectAll(Banner banner);
}
