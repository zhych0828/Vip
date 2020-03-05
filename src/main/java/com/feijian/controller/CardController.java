package com.feijian.controller;

import com.feijian.dto.UserSearchDTO;
import com.feijian.model.Card;
import com.feijian.response.PageDataResult;
import com.feijian.service.CardService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Controller
public class CardController {

    private org.slf4j.Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    private CardService cardService;

    /**
     * 会员卡的添加
     *
     * @param card
     */
    @RequestMapping("/addCard")
    public void insert(Card card) {
        cardService.insert(card);
    }

    /**
     * 会员信息的修改
     *
     * @param card
     */
    @RequestMapping("/updateCardById")
    public void updateCardById(Card card) {
        cardService.updateCardById(card);
    }

    /**
     * 查找所有的客户
     *
     * @return
     */
    @RequestMapping(value = "/findAll", method = RequestMethod.POST)
    @ResponseBody
    public PageDataResult findAll(@RequestParam("pageNum") Integer pageNum,
                                  @RequestParam("pageSize") Integer pageSize, UserSearchDTO userSearchDTO) {
        PageDataResult pdr = new PageDataResult();
        try {
            if (null == pageNum) {
                pageNum = 1;
            }
            if (null == pageSize) {
                pageSize = 14;
            }
            // 获取用户列表
            pdr = cardService.findAll(userSearchDTO, pageNum, pageSize);

        } catch (Exception e) {
            e.printStackTrace();
            logger.error("用户列表查询异常！", e);
        }
        return pdr;
    }

    /**
     * 用户的添加或者修改
     *
     * @param card
     * @return
     */
    @RequestMapping(value = "/setCard", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> setCard(Card card, String adminPassword) {
        logger.info("设置用户:" + card);
        Map<String, Object> data = new HashMap<>();
        if (card.getUserId() == null) {
            data = cardService.addUser(card, adminPassword);
        } else {
            data = cardService.updateCard(card, adminPassword);
        }
        return data;
    }

    /**
     * 用户的扣减
     *
     * @param card
     * @return
     */
    @RequestMapping(value = "/downCard", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> downCard(Card card) {
        logger.info("用户扣减:" + card);
        Map<String, Object> data = new HashMap<>();
        data = cardService.setDown(card);
        return data;
    }

    /**
     * 用户得充值操作
     *
     * @param card
     * @return
     */
    @RequestMapping(value = "/upCard", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> upCard(Card card) {
        logger.info("用户充值:" + card);
        Map<String, Object> data = new HashMap<>();
        data = cardService.upCard(card);
        return data;
    }
}