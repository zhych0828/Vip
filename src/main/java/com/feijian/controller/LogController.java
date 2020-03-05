package com.feijian.controller;

import com.feijian.dto.LogSearchDTO;
import com.feijian.response.PageDataResult;
import com.feijian.service.LogService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

@Slf4j
@Controller
public class LogController {

    private org.slf4j.Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    private LogService logService;


    @RequestMapping(value = "/findLog", method = RequestMethod.POST)
    @ResponseBody
    public PageDataResult findLog(@RequestParam("pageNum") Integer pageNum,
                                  @RequestParam("pageSize") Integer pageSize, LogSearchDTO logSearchDTO) {
        PageDataResult pageDataResult = new PageDataResult();
        try {
            if (null == pageNum) {
                pageNum = 1;
            }
            if (null == pageSize) {
                pageSize = 10;
            }
            // 获取用户列表
            pageDataResult = logService.findLog(logSearchDTO, pageNum, pageSize);

        } catch (Exception e) {
            e.printStackTrace();
            logger.error("日志列表查询异常！", e);
        }
        return pageDataResult;
    }
}