package com.ninesky.classtao.redpackage.service;

import com.ninesky.classtao.redpackage.vo.RedPacketTradeOrderDto;
import org.mengyun.tcctransaction.api.Compensable;

import java.math.BigDecimal;

public interface RedPackageService {
    BigDecimal getRedPacketAccountByUserId(long userId);

    @Compensable
    public String record(RedPacketTradeOrderDto tradeOrderDto);
}
