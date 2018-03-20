package com.kinstalk.satellite.service.api;

import com.kinstalk.satellite.common.page.Page;
import com.kinstalk.satellite.domain.Group;

import java.util.List;


public interface GroupService {

    public Page<Group> queryGroupPage(Integer currentPage, Integer pageSize);

    public Group queryGroup(Long id);

    public long deleteGroup(Long id);

    public long saveGroup(Group group);

    public List<Group> queryAllGroup();


}

