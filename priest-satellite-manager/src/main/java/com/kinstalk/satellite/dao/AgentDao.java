package com.kinstalk.satellite.dao;

import com.kinstalk.satellite.domain.AgentModel;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * Created by zhangchuanqi on 16/6/24.
 */
public interface AgentDao {

    String SELECT_ALL_FIELD=" agent_id as agentId, agent_area as agentArea, agent_ip as agentIp,agent_port as agentPort ";

    String INSERT_ALL_FIELD = " agent_area,agent_ip,agent_port,create_time ";

    String TABLE_AGENT = " `agent` ";


    @Options(useGeneratedKeys = true, keyProperty = "id", useCache = true)
    @Insert("insert into " + TABLE_AGENT + " (" + INSERT_ALL_FIELD + ") " +
            "values (#{agentArea},#{agentIp},#{agentPort},#{createTime})")
    public long save(AgentModel agentModel);


    @Update("update" + TABLE_AGENT +
            "set agent_area=#{agentArea},agent_ip=#{agentIp},agent_port=#{agentPort} where agent_id=#{agentId}")
    public long update(AgentModel agentModel);

    @Update("delete from" +TABLE_AGENT+"where agent_id=#{id}")
    public long delete(Long id);

    @Select("select"+ SELECT_ALL_FIELD +"from"+TABLE_AGENT+"where agent_id =#{id}")
    public AgentModel selectById(Long id);


    @Select("select"+ SELECT_ALL_FIELD +"from"+TABLE_AGENT+" order by agent_id desc")
    public List<AgentModel> selectList();


}
