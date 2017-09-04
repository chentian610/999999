// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   SecServiceImpl.java

package com.ninesky.classtao.sec.service.impl;

import com.ninesky.classtao.sec.service.SecService;
import com.ninesky.classtao.sec.vo.SecVO;
import com.ninesky.framework.GeneralDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service("secServiceImpl")
public class SecServiceImpl	implements SecService
{
	@Autowired
	private GeneralDAO dao;


	public void addSec(SecVO vo)
	{
		dao.insertObject("secMap.insertSec", vo);
	}

	public List getSec(SecVO vo)
	{
		return dao.queryForList("secMap.getSecList", vo);
	}
}
