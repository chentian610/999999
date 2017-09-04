package com.ninesky.framework;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.ninesky.common.util.ActionUtil;
import com.ninesky.common.util.StringUtil;
import net.minidev.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author running@vip.163.com
 *
 */
public class JWTUtil {
	private static Logger logger = LoggerFactory.getLogger(JWTUtil.class);

	private static final byte[] JWT_SECRET = SystemConfig.getProperty("JWT_SECRET").getBytes();

	public static final String ACCESS_TOKEN = "token";

	private static final String USER_ID = "user_id";

	private static final String JWT_UID = "uid";

	private static final String JWT_EXT = "ext";

	public  static final String STATE = "state";

	private static final String APP_ID = "app_id";

	private static final String APP_KEY = "app_key";

	private static final String APP_SECRET = "app_secret";

    private static final String SCHOOL_ID = "school_id";


    /**
	 * 初始化head部分的数据为
	 * {
	 * 		"alg":"HS256",
	 * 		"type":"JWT"
	 * }
	 */
	private static final JWSHeader header=new JWSHeader(JWSAlgorithm.HS256, JOSEObjectType.JWT, null, null, null, null, null, null, null, null, null, null, null);

	/**
	 * 生成token，该方法只在用户登录成功后调用
	 *
	 * @param payload，可以存储用户id，token生成时间，token过期时间等自定义字段
	 * @return token字符串,若失败则返回null
	 */
	public static String createToken(Map<String, Object> payload) {
		String tokenString=null;
		// 创建一个JWS object
		JWSObject jwsObject = new JWSObject(header, new Payload(new JSONObject(payload)));
		try {
			// 将jwsObject 进行HMAC签名
			jwsObject.sign(new MACSigner(JWT_SECRET));
			tokenString=jwsObject.serialize();
			logger.info("JWT="+tokenString);
		} catch (Exception e) {
			logger.info("签名失败:" + e.getMessage());
			e.printStackTrace();
		}
		return tokenString;
	}



	/**
	 * 将Token转换成Map集合,集合中主要包含STATE状态码   data鉴权成功后从token中提取的数据
	 * 该方法在过滤器中调用，每次请求API时都校验
	 * @param token
	 * @return  Map<String, Object>
	 */
	public static Map<String, Object> TokenToMap(String token) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		if (StringUtil.isEmpty(token)) {
			resultMap.put(STATE, TokenState.EMPTY);
			return resultMap;
		}
		try {
			JWSObject jWSObject = JWSObject.parse(token);
			Payload payload = jWSObject.getPayload();
			JWSVerifier verifier = new MACVerifier(JWT_SECRET);
			if (jWSObject.verify(verifier)) {
				JSONObject jsonOBj = payload.toJSONObject();
				resultMap.put(STATE, TokenState.VALID);
				// token校验成功,若payload包含ext字段，则校验是否过期
				if (jsonOBj.containsKey(JWT_EXT)) {
					long extTime = Long.valueOf(jsonOBj.get(JWT_EXT).toString());
					long curTime = ActionUtil.getSysTime().getTime();
					// 过期了
					if (curTime > extTime) {
						resultMap.clear();
						resultMap.put(STATE, TokenState.EXPIRED);
					}
				}
				resultMap.putAll(jsonOBj);

			} else {
				// 校验失败
				resultMap.put(STATE, TokenState.INVALID);
			}

		} catch (Exception e) {
			// token格式不合法导致的异常
			resultMap.clear();
			resultMap.put(STATE, TokenState.INVALID);
		}
		return resultMap;
	}

	/**
	 * 从http请求中获取token，并解析校验
	 * 增加token中的uid和请求中user_id关联校验
	 * @param request
	 * @return  Map<String, Object>
	 */
	public static Map<String, Object> TokenToMapFromRequest(HttpServletRequest request) {
		String app_id = request.getHeader(APP_ID);
		String app_key = request.getHeader(APP_KEY);
		String app_secret = request.getHeader(APP_SECRET);
		String jwt = app_id+"."+app_key+"."+app_secret;
		if ("..".equals(jwt)) jwt = request.getHeader(ACCESS_TOKEN);
		Map<String, Object> map = TokenToMap(jwt);
		map.put("jwt",jwt);
		return map;
	}

	/**
	 * 从http请求中获取token，并解析校验
	 * 增加token中的school_id和请求中school_id关联校验
	 * @param request
	 */
	public static boolean checkSchoolToken(HttpServletRequest request) {
		Map<String, Object> map = TokenToMapFromRequest(request);
		if (!TokenState.VALID.equals(map.get(STATE))) return false;
		String school_id = request.getParameter(SCHOOL_ID);
		if (StringUtil.isEmpty(school_id)) return false;
		return school_id.equals(map.get(SCHOOL_ID).toString());
	}

	/**
	 * 从http请求中获取token，并解析校验
	 * 增加token中的school_id和请求中school_id关联校验
	 * @param request
	 */
	public static boolean checkUserToken(HttpServletRequest request) {
		Map<String, Object> map = TokenToMapFromRequest(request);
		if (!TokenState.VALID.equals(map.get(STATE))) return false;
		String user_id = request.getParameter(USER_ID);
		if (StringUtil.isEmpty(user_id)) return false;
		return user_id.equals(map.get(JWT_UID).toString());
	}
}
