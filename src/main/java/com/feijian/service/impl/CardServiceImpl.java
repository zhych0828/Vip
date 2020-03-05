package com.feijian.service.impl;

import com.feijian.dao.CardMapper;
import com.feijian.dao.LogMapper;
import com.feijian.dto.UserSearchDTO;
import com.feijian.model.Card;
import com.feijian.model.Log;
import com.feijian.response.PageDataResult;
import com.feijian.service.CardService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CardServiceImpl implements CardService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    private CardMapper cardMapper;

    @Resource
    private LogMapper logMapper;

    private Log log = new Log();

    /**
     * 会员的添加
     * @param record
     */
    @Override
    @Transactional
    public void insert(Card record) {
        cardMapper.insert(record);
    }

    /**
     * 会员的修改
     * @param record
     */
    @Override
    @Transactional
    public void updateCardById(Card record) {
        cardMapper.updateByPrimaryKeySelective(record);
    }

    /**
     * 查找查找所有的会员
     * @return
     */
    @Override
    public PageDataResult findAll(UserSearchDTO userSearch, Integer pageNum, Integer pageSize) {
        PageDataResult pageDataResult = new PageDataResult();
        PageHelper.startPage(pageNum, pageSize);
        List<Card> baseAllUsers = cardMapper.findAll(userSearch);
        if(baseAllUsers.size() != 0){
            PageInfo<Card> pageInfo = new PageInfo<>(baseAllUsers);
            pageDataResult.setList(baseAllUsers);
            pageDataResult.setTotals((int) pageInfo.getTotal());
        }

        return pageDataResult;
    }

    @Override
    public int checkCard(Card record) {
        return cardMapper.checkCard(record);
    }

    /**
     * vip客户的添加
     * @param card
     * @return
     */
    @Override
    @Transactional
    public Map<String,Object> addUser(Card card, String adminPassword) {
        Map<String,Object> data = new HashMap();
        Log log = new Log();
        if (!adminPassword.equals("900120")) {
            data.put("code", 0);
            data.put("msg", "管理员密码输入错误！");
            logger.error("用户[添加], 结果=管理员密码输入错误！");
            return data;
        }
        try {
        Card old = cardMapper.getUserByUserName(card.getUserName(),null);
        if(old != null){
            data.put("code",0);
            data.put("msg","用户名已存在！");
            logger.error("用户[新增]，结果=用户名已存在！");
            return data;
        }else if (card.getPassword()==null || "".equals(card.getPassword())) {
            card.setPassword("123456");
        }else {
            card.setPassword(card.getPassword());
        }
        /*String phone = card.getPhone();
        if (phone.length() != 11) {
            data.put("code", 0);
            data.put("msg", "手机号码的位数不对！");
            logger.error("设置用户[新增或跟新], 结果=手机号码的位数不对!");
            return data;
        }*/
        card.setSurplus(card.getTotal());
        card.setUseCount(0);
        int result = cardMapper.insert(card);
        if (result == 0) {
            data.put("code", 0);
            data.put("msg", "新增失败");
            logger.error("用户[新增], 结果=失败");
            return data;
        }
        data.put("code", 1);
        data.put("msg", "新增成功");
        logger.info("用户[新增], 结果=成功");
        log.setLogTime(new Date());
        log.setUserName(card.getUserName());
        log.setRemark(card.getUserName() + "会员添加操作");
        log.setFlag(2);
        logMapper.insertSelective(log);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("用户[新增]异常！", e);
            return data;
        }
        return data;
    }

    /**
     * vip用户的信息更新
     * @param card
     * @return
     */
    @Override
    @Transactional
    public Map<String,Object> updateCard(Card card, String adminPassword) {
        Map<String,Object> data = new HashMap<>();
        if (!adminPassword.equals("900120")) {
            data.put("code", 0);
            data.put("msg", "管理员密码输入错误！");
            logger.error("用户[添加], 结果=管理员密码输入错误！");
            return data;
        }
        Integer id = card.getUserId();
        Card old = cardMapper.getUserByUserName(card.getUserName(),id);
        if (old != null) {
            data.put("code", 0);
            data.put("msg", "用户名已经存在");
            logger.error("用户[更新], 结果=用户名已经存在！");
            return data;
        }
        if (card.getPassword() != null) {
            card.setPassword(card.getPassword());
        }
        /*card.setSurplus(card.getTotal());*/
        int result = cardMapper.updateByPrimaryKeySelective(card);
        if(result == 0) {
            data.put("code", 0);
            data.put("msg", "更新失败");
            logger.error("用户[更新],  结果=更新失败！" );
            return data;
        }
        data.put("code", 1);
        data.put("msg", "更新成功！");
        logger.info("用户[更新],  结果=更新成功！");
        log.setLogTime(new Date());
        log.setUserName(card.getUserName());
        log.setUserId(card.getUserId());
        log.setRemark(card.getUserName() + "-会员更新操作-<剩" + card.getSurplus() + "次>");
        log.setFlag(4);
        logMapper.insertSelective(log);
        return data;
    }

    /**
     * 根据用户名查找客户
     * @param userName
     * @return
     */
    @Override
    public Card findByUserName(String userName) {
        return cardMapper.findByUserName(userName);
    }

    /**
     * 会员扣费
     * @param card
     * @return
     */
    @Override
    @Transactional
    public Map<String, Object> setDown(Card card) {
        Map<String,Object> data = new HashMap<>();
        String author = cardMapper.findPassword(card.getUserId());
        if (!card.getPassword().equals(author)) {
            data.put("code", 0);
            data.put("msg", card.getUserName() + "的密码输入错误！重新输入或者联系管理员");
            logger.error("用户[扣减], 结果=密码输入错误！");
            return data;
        }
        if (card.getSurplus() == 0) {
            data.put("code", 0);
            data.put("msg", card.getUserName() + "的会员卡已使用完！请充值！");
            logger.error("用户[扣减], 结果=会员卡用完！");
            return data;
        }
        int result = cardMapper.setDown(card.getUserId());
        if(result == 0) {
            data.put("code", 0);
            data.put("msg", "扣费失败");
            logger.error("用户[扣减],  结果=扣减失败！" );
            return data;
        }
        data.put("code", 1);
        data.put("msg", "扣费成功");
        logger.info("用户[扣减],  结果=扣减成功！" );
        log.setLogTime(new Date());
        log.setUserName(card.getUserName());
        log.setUserId(card.getUserId());
        log.setRemark(card.getUserName() + "-会员扣减操作-<剩" + card.getSurplus() + " - 1次>");
        log.setFlag(1);
        logMapper.insertSelective(log);
        return data;
    }

    /**
     * 用户充值
     * @param card
     * @return
     */
    @Override
    @Transactional
    public Map<String, Object> upCard(Card card) {
        Map<String, Object> data = new HashMap<>();
        String auth = card.getPassword();
        if (!auth.equals("900120")) {
            data.put("code", 0);
            data.put("msg", "管理员密码输入错误！");
            logger.error("用户[充值], 结果=管理员密码输入错误！");
            return data;
        }
        card.setSurplus(card.getTotal());
        card.setUseCount(0);
        cardMapper.upCard(card);
        data.put("code", 1);
        data.put("msg", "充值成功");
        logger.info("用户[充值],  结果=充值成功！" );
        log.setLogTime(new Date());
        log.setUserName(card.getUserName());
        log.setUserId(card.getUserId());
        log.setRemark(card.getUserName() + "会员的充值操作");
        log.setFlag(3);
        logMapper.insertSelective(log);
        return data;
    }
}