package com.feijian.dao;

import com.feijian.dto.UserSearchDTO;
import com.feijian.model.Card;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CardMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Card record);

    int insertSelective(Card record);

    Card selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Card record);

    int updateByPrimaryKey(Card record);

    List<Card> findAll(UserSearchDTO userSearchDTO);

    int checkCard(Card record);

    Card getUserByUserName(@Param("userName")String userName, @Param("userId") Integer userId);

    Card findByUserName(@Param("userName") String userName);

    int setDown(@Param("userId") Integer userId);

    String findPassword(@Param("userId") Integer userId);

    void upCard(Card record);
}