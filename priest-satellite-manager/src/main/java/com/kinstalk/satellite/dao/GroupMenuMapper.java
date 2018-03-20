package com.kinstalk.satellite.dao;

import com.kinstalk.satellite.domain.GroupMenu;
import com.kinstalk.satellite.domain.Menu;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface GroupMenuMapper {

    public long save(GroupMenu groupMenu);

    public long update(GroupMenu groupMenu);

    public long delete(GroupMenu groupMenu);

    public List<GroupMenu> selectList(Map<String, Object> params);

    public Integer selectListCount(Map<String, Object> params);

    public GroupMenu selectById(Long id);

    public List<Menu> selectMenuList(@Param("ids")List<Long> ids);

}
