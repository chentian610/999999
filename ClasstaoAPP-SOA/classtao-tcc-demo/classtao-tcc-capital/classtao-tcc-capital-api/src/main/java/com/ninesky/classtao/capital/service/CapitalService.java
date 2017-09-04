package com.ninesky.classtao.capital.service;

import com.ninesky.classtao.capital.vo.CapitalTradeOrderDto;
import com.ninesky.common.vo.ReceiveVO;
import org.mengyun.tcctransaction.api.Compensable;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;

public interface CapitalService {
    BigDecimal getCapitalAccountByUserId(long userId);

    @Compensable
    public String record(CapitalTradeOrderDto tradeOrderDto);
}
