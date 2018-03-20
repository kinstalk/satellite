package com.kinstalk.satellite.common.utils;

public class SatelliteConstants {

    //iframe,必须最后关闭窗口，否则执行不了刷新动作。
    public static final String WEB_IFRAME_SCRIPT = "<script type='text/javascript'>" +
            "parent.layer.msg('%s', {icon: 1,time: 1000}, function(){" +
            "   parent.location.reload();" +
            //"   parent.layer.close(parent.layer.getFrameIndex(window.name));" +
            "});" +
            "</script>";

    public static final String WEB_IFRAME_ERROR_SCRIPT = "<script type='text/javascript'>" +
            "parent.layer.msg('%s', {icon: 1,time: 1000}, function(){" +
//            "   parent.location.reload();" +
            //"   parent.layer.close(parent.layer.getFrameIndex(window.name));" +
            "});" +
            "</script>";
}
