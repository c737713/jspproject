package com.example.jsp.manager.impl;

import com.example.jsp.dao.OrderInfoDao;
import com.example.jsp.manager.todao.OrderInfoManagerToDao;
import com.example.jsp.manager.toservice.OrderInfoManager;
import com.example.jsp.pojo.OrderInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author 橙鼠鼠
 */
@Service
public class OrderInfoManagerImpl implements OrderInfoManagerToDao, OrderInfoManager {
    private OrderInfoDao orderInfoDao;

    @Autowired
    public void setOrderInfoDao(OrderInfoDao orderInfoDao) {
        this.orderInfoDao = orderInfoDao;
    }

    @Override
    public int save(OrderInfo target) {
        return orderInfoDao.save(target);
    }

    @Override
    public void delete(int id) {
        orderInfoDao.delete(id);
    }

    @Override
    public int insert(OrderInfo target) {
        Integer id = getId(target);
        if (id == null) {
            int save = save(target);
            target.setId(save);
            return save;
        }
        target.setId(id);
        return target.getId();
    }

    @Override
    public void destroy(int id) {
        delete(id);
    }

    @Override
    public void destroy(OrderInfo orderInfo) {
        destroy(orderInfo.getId());
    }

    @Override
    public OrderInfo select(int id) {
        return orderInfoDao.selectById(id);
    }

    @Override
    public int restore(OrderInfo target) {
        Integer id = getId(target);
        if (id == null) {
            update(target);
            return 0;
        }
        target.setId(id);
        return target.getId();
    }

    @Override
    public void update(OrderInfo target) {
        orderInfoDao.update(target);
    }

    @Override
    public List<OrderInfo> selectByOrderId(int id) {
        return orderInfoDao.selectByOrderId(id);
    }

    @Override
    public Integer getId(OrderInfo orderInfo) {
        return orderInfoDao.getId(orderInfo);
    }

    @Override
    public boolean isNotExist(int id) {
        return select(id) == null;
    }

    @Override
    public void deleteByOrderId(int id) {
        orderInfoDao.deleteByOrder(id);
    }
}
