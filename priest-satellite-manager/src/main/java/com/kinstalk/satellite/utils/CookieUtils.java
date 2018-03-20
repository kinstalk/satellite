package com.kinstalk.satellite.utils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CookieUtils {
    /* ------------------------- cookie ------------------------- */

    public static String getCookie(HttpServletRequest request, String key) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) return null;
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(key)) return cookie.getValue();
        }
        return null;
    }

    /*
     * 有效期默认为-1
     * 格式为：key=value; domain=.kinstalk.com; path=/; HttpOnly
     */
    public static void setHttpOnlyCookie(HttpServletResponse response, String key, String value, String domain) {
        StringBuffer sb = new StringBuffer();
        sb.append(key).append("=").append(value).append("; ");
        sb.append("domain=").append(domain).append("; ");
        sb.append("path=/").append("; ");
        sb.append("HttpOnly");
        String cookieValue = sb.toString();
        response.addHeader("Set-Cookie", cookieValue);
    }
}
