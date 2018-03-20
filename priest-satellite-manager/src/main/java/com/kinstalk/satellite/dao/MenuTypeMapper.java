package com.kinstalk.satellite.dao;

import com.kinstalk.satellite.domain.MenuType;

import java.util.List;
import java.util.Map;

public interface MenuTypeMapper {

    public long save(MenuType menuType);

    public long update(MenuType menuType);

    public long delete(Long id);

    public List<MenuType> selectList(Map<String, Object> params);

    public Integer selectListCount(Map<String, Object> params);

    public MenuType selectById(Long id);

}
