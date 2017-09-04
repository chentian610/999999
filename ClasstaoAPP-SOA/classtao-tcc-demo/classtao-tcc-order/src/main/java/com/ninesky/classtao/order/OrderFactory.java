package com.ninesky.classtao.order;

import com.ninesky.classtao.order.vo.Order;
import com.ninesky.classtao.order.vo.OrderLine;
import com.ninesky.classtao.order.vo.Product;
import com.ninesky.framework.GeneralDAO;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by changming.xie on 4/1/16.
 */
@Component
public class OrderFactory {


    @Autowired
    GeneralDAO dao;

    public Order buildOrder(long payerUserId, long payeeUserId, List<Pair<Long, Integer>> productQuantities) {

        Order order = new Order(payerUserId, payeeUserId);

        for (Pair<Long, Integer> pair : productQuantities) {
            long productId = pair.getLeft();
            Product product = dao.queryObject("productMap.findById",productId);
            order.addOrderLine(new OrderLine(productId, pair.getRight(),product.getPrice()));
        }

        return order;
    }
}
