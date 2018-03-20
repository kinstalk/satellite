package com.kinstalk.satellite.dao;

import com.kinstalk.satellite.domain.AgentModel;
import com.kinstalk.satellite.domain.PhonePool;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * Created by zhangchuanqi on 16/6/24.
 */
public interface PhonePoolDao {



    String SELECT_ALL_FIELD=" id as id, agent_id as agentId, timer_id as timerId,phone_start as phoneStart,phone_end as phoneEnd ";

    String INSERT_ALL_FIELD = " agent_id,timer_id,phone_start,phone_end ";

    String TABLE_PHONE_POOL = " `phone_pool` ";


    @Options(useGeneratedKeys = true, keyProperty = "id", useCache = true)
    @Insert("insert into " + TABLE_PHONE_POOL + " (" + INSERT_ALL_FIELD + ") " +
            "values (0,0,#{phoneStart},#{phoneEnd})")
    public long save(PhonePool phonePool);


    @Update("update" + TABLE_PHONE_POOL +
            "set agent_id=#{agentId},timer_id=#{timerId} where id=#{id} and agent_id =0 and timer_id =0")
    public long update(PhonePool phonePool);

    @Update("delete from" +TABLE_PHONE_POOL+" where agent_id=0 and timer_id=0 ")
    public long delete();

    @Select("select"+ SELECT_ALL_FIELD +"from"+TABLE_PHONE_POOL+"where agent_id =0 and timer_id =0 ")
    public List<PhonePool> selectList();



    @Select("select"+ SELECT_ALL_FIELD +"from"+TABLE_PHONE_POOL+"where agent_id =0 and timer_id =0 limit 0,1")
    public PhonePool selectSingle();

    @Select("select"+ SELECT_ALL_FIELD +"from"+TABLE_PHONE_POOL+"where agent_id =#{agentId} and timer_id =#{timerId} limit 0,1")
    public PhonePool selectExist(PhonePool phonePool);


}
