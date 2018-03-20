package com.kinstalk.satellite.filter;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.kinstalk.satellite.domain.AdminUser;
import com.kinstalk.satellite.domain.Menu;
import com.kinstalk.satellite.domain.RunScriptTimerDetail;
import com.kinstalk.satellite.service.api.AdminUserService;
import com.kinstalk.satellite.utils.CacheUtils;
import com.kinstalk.satellite.utils.CookieUtils;
import com.kinstalk.satellite.utils.EncryptUtil;
import net.sf.ehcache.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import sun.misc.BASE64Decoder;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;



public class AdminLoginFilter implements Filter {
	private Logger  logger = LoggerFactory.getLogger(AdminLoginFilter.class);

	//cookie 超时时间，默认一天。
	//private String cookieTimeout = "864//白名单列表。00";

	private List<String> whiteListUrl = new ArrayList<String>();
	private String passportUrl = "";


	private AdminUserService adminUserService;

	// 系统分类.如新统计系统menuTypeId是1,在web.xml里面进行配置。
	private String menuTypeId = "";

	private static final String rightErrorPage = "/rightError";

	public static final boolean isMac = System.getProperty("os.name") != null
			&& System.getProperty("os.name").toLowerCase().contains("mac");

	public static final boolean isWindows = System.getProperty("os.name") != null
			&& System.getProperty("os.name").toLowerCase().contains("windows");

	public static String USER_MENU_FILTER_KEY = "user_menu_filter_key";

	public static String USER_MENU_HTML_FIELD = "user_menu_html_key_";

	public static String USER_MENU_JSON_FIELD = "user_menu_json_key_";


	@Override
	public void init(FilterConfig filterConfig) throws ServletException {

		this.passportUrl = ResourceBundle.getBundle("project").getString("priest.return.url");

		this.menuTypeId = filterConfig.getInitParameter("menuTypeId");


		//this.cookieTimeout = filterConfig.getInitParameter("cookieTimeout");
		String tmpWhiteListUrl = filterConfig.getInitParameter("whiteListUrl");
		if (tmpWhiteListUrl != null && !tmpWhiteListUrl.equals("")) {
			String[] tmpWhiteListUrlList = tmpWhiteListUrl.split(",");
			Collections.addAll(whiteListUrl, tmpWhiteListUrlList);
		}
		//System.out.println("cookieTimeout:" + cookieTimeout);
		logger.debug("whiteListUrl:{}", whiteListUrl);

	}

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

		HttpServletRequest httpRequest = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;

		String url = httpRequest.getPathInfo();

		String referer = httpRequest.getHeader("referer");

		boolean isWhiteUrl = false;
		//增加判断白名单。
		for (String whiteUrl : whiteListUrl) {
			if (whiteUrl != null && url.startsWith(whiteUrl)) {
				isWhiteUrl = true;
			}
		}

		//如果是白名单直接返回，不进行权限过滤。
		if (isWhiteUrl) {
			filterChain.doFilter(servletRequest, servletResponse);
			return;
		}

//        try {
//            if (url != null && !url.equals("")) {
//                URL urlTmp = new URL(url);
//                referer = urlTmp.getProtocol() + "://" + urlTmp.getHost();
//            }
//        } catch (MalformedURLException e) {
//        }


		// (String) httpRequest.getSession().getAttribute("passport_user");
		String userId = null;
		//测试使用。
		//String userId = "zhangsan";
		//httpRequest.getSession().setAttribute("passport_user", userId);

		boolean isLogin = false;
		//判断用户登录。

		//测试cookie登陆。

		String passportCode = CookieUtils.getCookie(httpRequest, "passport_code");
		//校验key。
		String passportKey = CookieUtils.getCookie(httpRequest, "passport_key");


		if (passportCode != null && !passportCode.equals("") && passportKey != null && !passportKey.equals("")) {
			//生成校验字符串。

			String newPassportKey = EncryptUtil.sha(passportCode);
			if (newPassportKey != null && newPassportKey.equals(passportKey)) {
				//校验成功。
				userId = AdminLoginFilter.passportDecoder(passportCode);
				//校验成功，将userId放到 passport_user 里面。
				//httpRequest.getSession().setAttribute("passport_user", userId);
				isLogin = true;
			}
		}


		// 本机开发。
//        if (isWindows || isMac) {
//            // 如果是windows返回测试,系统的用户
//            userId = "dev_test_" + menuTypeId;
//            isLogin = true;
//        }


		//判断用户是否登录，然后进行拦截，跳转到登录页面。
		if (!isLogin) {
			String newPassportUrl = String.format(this.passportUrl, referer);
			httpResponse.sendRedirect(newPassportUrl);
		}

		// 设置缓存数据
		RunScriptTimerDetail.UserMenuJson userMenuJson = null;
		String userMenuJsonStr = null;//(UserMenuJson) httpRequest.getSession().getAttribute("UserMenuJsonCache");
		Element jsonElement = CacheUtils.getElementByKeyAndField(USER_MENU_FILTER_KEY, USER_MENU_JSON_FIELD + userId);

		try {
			if(jsonElement != null) {
				userMenuJsonStr = (String) jsonElement.getValue();
			}
		} catch (Exception e) {// 有可能缓存会有问题.
			logger.error("Read Cache Fail", e);
		}
		//存储字符串。
		if (userMenuJsonStr == null || userMenuJsonStr.contains("\"menuList\":[]") || userMenuJsonStr.equals("") || "{\"menuList\":[]}".equals(userMenuJsonStr)) {
			// 获得json接口数据.
			userMenuJsonStr = getUserMenuJson(userId);

			logger.debug(userMenuJsonStr);
			CacheUtils.setElementByKeyAndField(USER_MENU_FILTER_KEY, USER_MENU_JSON_FIELD + userId, userMenuJsonStr);
		}

		// 字符替换处理
		//str = commReplace(str);
		Gson gson = new Gson();
		//System.out.println(str);
		Type type = new TypeToken<RunScriptTimerDetail.UserMenuJson>() {
		}.getType();

		try {
			// 转换后的json.
			userMenuJson = gson.fromJson(userMenuJsonStr, type);
		} catch (Exception e) {
			e.printStackTrace();
		}

		// 3分钟更新一次缓存.***************结束***************

		if (userMenuJson != null) {
			// 设置用户名
			//httpRequest.setAttribute("UserName", userMenuJson.getUserName());
			httpRequest.setAttribute("UserName", userId);
			JsonArray menuJsonArray = new JsonArray();
			List<Menu> menuList = userMenuJson.getMenuList();

			if (menuList != null && menuList.size() > 0) {
				menuJsonArray = loopMenuJson(menuList, "_self");
			}
			// 添加到数组里面
			// menuTypeJson.add("nodes", menuJsonArray);

			// 放到页面展示
			httpRequest.setAttribute("MenuUserList", new Gson().toJson(menuJsonArray));
			//增加用户菜单.
//            httpRequest.setAttribute("MenuList", userMenuJson.getMenuList());
		}

		// 统计的url是 /admin/开始的
		if (url.indexOf("/admin/") == 0
				&& !url.contains("/admin/index")) {
			// /pages/admin/index.jsp 不包括首页
			if (userMenuJson != null && userMenuJson.getMenuList() != null) {
				boolean haveRight = false;
				// 用户菜单
				for (Menu userMenu : userMenuJson.getMenuList()) {
					// 判断系统是否有权限
					if (userMenu != null && userMenu.getUrl() != null
							&& url.equals(userMenu.getUrl())) {
						// 有权限
						haveRight = true;
						// break;循环全部
					}
				}

				// 判断用户是否没有权限//查询全部菜单.
				if (!haveRight) {
					haveRight = true;
					for (Menu userMenu : userMenuJson.getMenuList()) {
						// 判断系统是否没有有权限
						if (userMenu != null && userMenu.getUrl() != null
								&& url.equals(userMenu.getUrl())) {
							// 有权限
							haveRight = false;
							// break;循环全部
						}
					}
				}

				if (haveRight) {

					if (httpResponse != null && httpRequest != null) {
						filterChain.doFilter(servletRequest, servletResponse);
					}
					return;
				} else {
					httpResponse.sendRedirect(rightErrorPage);
					return;
				}
			} else {
				httpResponse.sendRedirect(rightErrorPage);
				return;
			}
		} else {
			if (httpResponse != null && httpRequest != null) {
				filterChain.doFilter(servletRequest, servletResponse);
			}
			return;
		}

	}

	private String getUserMenuJson(String uid) {
		// 返回类
		JsonObject tmpJson = new JsonObject();
		/* 用户菜单json. */
		JsonArray menuJsonArray = new JsonArray();
		if (adminUserService == null) {
			ApplicationContext act = ContextLoader.getCurrentWebApplicationContext();
			adminUserService = (AdminUserService) act.getBean("adminUserService");

		}

		AdminUser adminUser = adminUserService.queryAdminUserAndMenuList(uid);

		if (adminUser != null && adminUser.getMenuList() != null) {

			for (Menu menu : adminUser.getMenuList()) {

				// set json属性。
				// 得到当前系统菜单.
				JsonObject menuJson = new JsonObject();
				menuJson.addProperty("name", menu.getName());
				menuJson.addProperty("id", menu.getId());
				menuJson.addProperty("url", menu.getUrl());
				menuJson.addProperty("parentId", menu.getParentId());
				menuJson.addProperty("level", menu.getLevel());
				menuJson.addProperty("orderId", menu.getOrderId());
				menuJsonArray.add(menuJson);
			}

			// 返回菜单权限
			tmpJson.add("menuList", menuJsonArray);
			tmpJson.addProperty("userName", adminUser.getName());
		}

		return new Gson().toJson(tmpJson);
	}


	@Override
	public void destroy() {

	}

	//对密码进行加密解密，默认使用一个最简单的方式。
	public static String passportEncoder(String str) {
		return BASE64Encoder(str);
	}

	public static String passportDecoder(String str) {
		return BASE64Decoder(str);
	}

	// 将 s 进行 BASE64 编码
	private static String BASE64Encoder(String str) {
		if (str == null) return null;
		try {
			return (new sun.misc.BASE64Encoder()).encode(str.getBytes());
		} catch (Exception e) {
			return null;
		}
	}

	// 将 BASE64 编码的字符串 s 进行解码
	private static String BASE64Decoder(String str) {
		if (str == null) return null;
		BASE64Decoder decoder = new BASE64Decoder();//
		try {
			byte[] b = decoder.decodeBuffer(str);
			return new String(b);
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 递归json 组装数组.
	 */
	private JsonArray loopMenuJson(List<com.kinstalk.satellite.domain.Menu> menuList, String a_target) {

		JsonArray menuJsonArray = new JsonArray();
		for (com.kinstalk.satellite.domain.Menu menu : menuList) {
			JsonObject menuJson = new JsonObject();
			menuJson.addProperty("id", menu.getId());
			menuJson.addProperty("name", menu.getName());
			menuJson.addProperty("url", menu.getUrl());

			menuJson.addProperty("open", true);
			menuJson.addProperty("level", menu.getLevel());
			menuJson.addProperty("parentId", menu.getParentId());

			if (menu.getChildren() != null) {
				// 添加到子节点
				JsonArray childrenMenuJsonArray = loopMenuJson(
						menu.getChildren(), a_target);
				menuJson.add("children", childrenMenuJsonArray);
			}
			menuJsonArray.add(menuJson);
		}
		return menuJsonArray;
	}

}
