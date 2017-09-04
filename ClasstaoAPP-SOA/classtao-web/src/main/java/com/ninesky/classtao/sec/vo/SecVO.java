// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   SecVO.java

package com.ninesky.classtao.sec.vo;

import java.util.Date;

public class SecVO
{

	private Integer id;
	private String school_name;
	private String link_man;
	private String phone;
	private String url1;
	private String url2;
	private String url3;
	private String ip_address;
	private Date create_date;

	public void setId(Integer id)
	{
		this.id = id;
	}

	public Integer getId()
	{
		return id;
	}

	public void setSchool_name(String school_name)
	{
		this.school_name = school_name;
	}

	public String getSchool_name()
	{
		return school_name;
	}

	public void setLink_man(String link_man)
	{
		this.link_man = link_man;
	}

	public String getLink_man()
	{
		return link_man;
	}

	public void setPhone(String phone)
	{
		this.phone = phone;
	}

	public String getPhone()
	{
		return phone;
	}

	public void setUrl1(String url1)
	{
		this.url1 = url1;
	}

	public String getUrl1()
	{
		return url1;
	}

	public void setUrl2(String url2)
	{
		this.url2 = url2;
	}

	public String getUrl2()
	{
		return url2;
	}

	public void setUrl3(String url3)
	{
		this.url3 = url3;
	}

	public String getUrl3()
	{
		return url3;
	}

	public void setIp_address(String ip_address)
	{
		this.ip_address = ip_address;
	}

	public String getIp_address()
	{
		return ip_address;
	}

	public void setCreate_date(Date create_date)
	{
		this.create_date = create_date;
	}

	public Date getCreate_date()
	{
		return create_date;
	}
}
