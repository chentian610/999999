package com.ninesky.classtao.shiro;

import org.apache.shiro.authc.UsernamePasswordToken;

/**
 * 自定义ShiroToken
 * @author chenth
 */
public class MyShiroToken extends UsernamePasswordToken implements java.io.Serializable {

	private static final long serialVersionUID = -6451794657814516274L;

	/**
	 * 手机号码
	 **/
	private String phone;
	/**
	 * 登录密码[字符串类型] 因为父类是char[] ]
	 **/
	private String pass_word;

	/**
	 * Json Web Token
	 */
	private String token;

	public MyShiroToken(String phone, String pass_word, String jwt_token) {
		super(phone, pass_word);
		this.phone = phone;
		this.pass_word = pass_word;
		this.token = jwt_token;
	}

	public MyShiroToken(String phone) {
		this.phone = phone;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getPass_word() {
		return pass_word;
	}
}