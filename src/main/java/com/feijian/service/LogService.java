package com.feijian.service;

import com.feijian.dto.LogSearchDTO;
import com.feijian.response.PageDataResult;

public interface LogService {
    PageDataResult findLog(LogSearchDTO logSearchDTO, Integer pageNum, Integer pageSize);
}
