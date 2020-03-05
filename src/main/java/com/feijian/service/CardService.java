package com.feijian.service;

import com.feijian.dto.UserSearchDTO;
import com.feijian.model.Card;
import com.feijian.response.PageDataResult;

import java.util.Map;

public interface CardService {
    /**
     * 用户的添加
     * @param record
     */
    void insert(Card record);

    void updateCardById(Card record);

    PageDataResult findAll(UserSearchDTO userSearch, Integer pageNum, Integer pageSize);

    /**
     * 用户的重复性判断
     * @param record
     */
    int checkCard(Card record);

    /**
     * vip客户的添加
     * @param card
     * @return
     */
    Map<String,Object> addUser(Card card, String adminPassword);

    /**
     * vip用户的信息更新
     * @param card
     * @return
     */
    Map<String,Object> updateCard(Card card, String adminPassword);

    /**
     * 根据用户名查找客户
     * @param userName
     * @return
     */
    Card findByUserName(String userName);

    /**
     * 用户消费扣减
     * @param card
     * @return
     */
    Map<String,Object> setDown(Card card);

    /**
     * 用户充值操作
     * @param card
     * @return
     */
    Map<String, Object> upCard(Card card);
}