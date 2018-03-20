package com.kinstalk.satellite.dao;

import com.kinstalk.satellite.domain.Menu;

import java.util.List;
import java.util.Map;

public interface MenuMapper {

    public long save(Menu menu);

    public long update(Menu menu);

    public long delete(Long id);

    public List<Menu> selectList(Map<String, Object> params);

    public Integer selectListCount(Map<String, Object> params);

    public List<Menu> selectMenuListByGroupId(Long groupId);

    public Menu selectById(Long id);

    public List<Menu> queryMenuByTypeIdAndParentIdAndUid(Map<String, Object> params);

    //查询前一个menu菜单。
    Menu selectPrevMenuByOrderId(Map<String, Object> params);

    //查询下一个menu菜单。
    Menu selectNextMenuByOrderId(Map<String, Object> params);
}
