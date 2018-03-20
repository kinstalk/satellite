package com.kinstalk.satellite.dao;

import com.kinstalk.satellite.domain.Script;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;
import java.util.Map;

public interface ScriptMapper {


    String SELECT_ALL_FIELD = " script_id as id,script_name as name,script_path as path,app_id as appId,remark as remark,create_time as createTime ";


    String INSERT_ALL_FIELD = " script_name,script_path,app_id,remark,create_time ";

    String TABLE_SCRIPT = " `script` ";

    @Insert("insert into " + TABLE_SCRIPT + " (" + INSERT_ALL_FIELD + ") " +
                    "values (#{name},#{path},#{appId},#{remark},#{createTime})")
    public long save(Script script);


    @Update("update" + TABLE_SCRIPT +
            "set script_name=#{name},script_path=#{path},app_id=#{appId},remark=#{remark}  where script_id=#{id}")
    public long update(Script script);

    @Update("delete from" +TABLE_SCRIPT+"where script_id=#{id}")
    public long delete(Long id);


    @Select("select"+ SELECT_ALL_FIELD +"from"+TABLE_SCRIPT+" order by script_id desc")
    public List<Script> selectList();
    @Select("select count(script_id) from"+TABLE_SCRIPT)
    public Integer selectListCount(Map<String, Object> params);
    @Select("select "+ SELECT_ALL_FIELD+" from"+TABLE_SCRIPT+"where script_id=#{id}")
    public Script selectById(Long id);
}
