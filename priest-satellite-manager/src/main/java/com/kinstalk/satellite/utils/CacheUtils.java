package com.kinstalk.satellite.utils;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

public class CacheUtils {
    // 获得缓存管理.
    private static CacheManager singletonCacheManager = CacheManager.create();

    public static CacheManager getSingletonCacheManager() {
        if (singletonCacheManager == null) {
            singletonCacheManager = CacheManager.create();
        }
        return singletonCacheManager;
    }

    //私有方法，如果没有cache则，创建一个cache，并设置 20 分钟缓存。
    //有的时候会同时创建对象，增加同步锁。
    private static synchronized Cache getCacheByKey(String key) {
        Cache menuTypeUidCache = null;
        try {

            if (getSingletonCacheManager().cacheExists(key)) {
                // 如果存在直接获得.
                menuTypeUidCache = getSingletonCacheManager()
                        .getCache(key);
            } else {// 否则创建.
                // 可以存5000个数据.60 * 20 设置20分钟缓存.
                menuTypeUidCache = new Cache(key, 5000, false, false, 60 * 10, 60 * 10);
                // 放入缓存.
                getSingletonCacheManager().addCache(menuTypeUidCache);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return menuTypeUidCache;
    }

    //获得缓存方法。
    public static Element getElementByKeyAndField(String key, String field) {
        Cache cache = getCacheByKey(key);
        //按照key的cache查询field的Cache。
        if (cache != null && cache.isKeyInCache(field)) {// 存在缓存 取到缓存.
            // 按照Email取得缓存.
            Element element = cache.get(field);
            return element;
        }
        return null;
    }

    //清除缓存方法
    public static Element removeAllElementByKey(String key) {
        Cache cache = getCacheByKey(key);
        //按照key的cache查询field的Cache。
        if (cache != null) {// 存在缓存 取到缓存.
            // 按照Email取得缓存.
            cache.removeAll();
        }
        return null;
    }


    //设置缓存方法。
    public static void setElementByKeyAndField(String key, String field, Object value) {
        Cache cache = getCacheByKey(key);
        //按照key的cache查询field的Cache。
        if (cache != null && !cache.isKeyInCache(field)) {// 存在缓存 取到缓存.
            // 按照Email取得缓存.
            // 放入缓存.key 是Email.
            Element element = new Element(field, value);
            cache.put(element);
        }
    }

    public static void main(String[] args) {
        String userMenuHtml = null;
        String userId = "1001";
        Element htmlElement = CacheUtils.getElementByKeyAndField("USER_MENU_FILTER_KEY", "USER_MENU_HTML_KEY" + userId);

        try {
            userMenuHtml = (String) htmlElement.getValue();
        } catch (Exception e) {// 有可能缓存会有问题.
            //System.out.println(e.getMessage());
        }
        System.out.println(userMenuHtml);

        CacheUtils.setElementByKeyAndField("USER_MENU_FILTER_KEY", "USER_MENU_HTML_KEY" + userId, "zhangsan 0001.");

        htmlElement = CacheUtils.getElementByKeyAndField("USER_MENU_FILTER_KEY", "USER_MENU_HTML_KEY" + userId);

        try {
            userMenuHtml = (String) htmlElement.getValue();
        } catch (Exception e) {// 有可能缓存会有问题.
            //System.out.println(e.getMessage());
        }

        System.out.println(userMenuHtml);

    }


}
