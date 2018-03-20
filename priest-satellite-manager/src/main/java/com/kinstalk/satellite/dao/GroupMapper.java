package com.kinstalk.satellite.dao;

import com.kinstalk.satellite.domain.Group;

import java.util.List;
import java.util.Map;

public interface GroupMapper {

    public long save(Group group);

    public long update(Group group);

    public long delete(Long id);

    public List<Group> selectList(Map<String, Object> params);

    public Integer selectListCount(Map<String, Object> params);

    public Group selectById(Long id);

}
