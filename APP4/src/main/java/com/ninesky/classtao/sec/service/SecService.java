// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   SecService.java

package com.ninesky.classtao.sec.service;

import com.ninesky.classtao.sec.vo.SecVO;
import java.util.List;

public interface SecService
{

	void addSec(SecVO secvo);

	List getSec(SecVO secvo);
}
