package com.feijian.service.impl;

import com.feijian.dao.LogMapper;
import com.feijian.dto.LogSearchDTO;
import com.feijian.model.Log;
import com.feijian.response.PageDataResult;
import com.feijian.service.LogService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class LogServiceImpl implements LogService {

    @Resource
    private LogMapper logMapper;

    @Override
    public PageDataResult findLog(LogSearchDTO logSearchDTO, Integer pageNum, Integer pageSize) {
        PageDataResult pageDataResult = new PageDataResult();
        PageHelper.startPage(pageNum, pageSize);
        List<Log> allLog = logMapper.findLog(logSearchDTO);
        if (allLog.size() != 0) {
            PageInfo<Log> pageInfo = new PageInfo<>(allLog);
            pageDataResult.setList(allLog);
            pageDataResult.setTotals((int) pageInfo.getTotal());
        }
        return pageDataResult;
    }
}
