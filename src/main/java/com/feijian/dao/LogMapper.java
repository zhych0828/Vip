package com.feijian.dao;

import com.feijian.dto.LogSearchDTO;
import com.feijian.model.Log;

import java.util.List;

public interface LogMapper {
    int deleteByPrimaryKey(Integer logId);

    int insert(Log record);

    int insertSelective(Log record);

    Log selectByPrimaryKey(Integer logId);

    int updateByPrimaryKeySelective(Log record);

    int updateByPrimaryKey(Log record);

    List<Log> findLog(LogSearchDTO logSearchDTO);
}