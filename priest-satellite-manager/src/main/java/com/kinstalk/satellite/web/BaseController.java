package com.kinstalk.satellite.web;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class BaseController {
    /**
     * 直接将 ajax 输入到页面
     */
    protected void writeToPage(HttpServletResponse response, String str) {
        try {
            if (response != null) {// 在junit测试的时候会不显示.
                response.setHeader("Pragma", "No-cache");
                response.setHeader("Cache-Control", "no-cache");
                response.setDateHeader("Expires", 0);
                response.setContentType("text/plain;charset=utf-8");
                PrintWriter writer = null;
                writer = response.getWriter();
                writer.write(str);
            }
        } catch (IOException e) {
        } finally {
        }

    }

    protected void writeHtmlToPage(HttpServletResponse response, String str) {
        try {
            if (response != null) {// 在junit测试的时候会不显示.
                response.setHeader("Pragma", "No-cache");
                response.setHeader("Cache-Control", "no-cache");
				response.setContentType("text/html;charset=utf-8");
				response.setDateHeader("Expires", 0);
                PrintWriter writer = null;
                writer = response.getWriter();
                writer.write(str);
            }
        } catch (IOException e) {
        } finally {
        }

    }
}
