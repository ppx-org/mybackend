package com.planb.test;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

public interface UserMapper extends BaseMapper<TestExample> {
//    Page<TestExample> selectPage(Page<TestExample> page, @Param("ew") Wrapper<TestExample> queryWrapper);
}