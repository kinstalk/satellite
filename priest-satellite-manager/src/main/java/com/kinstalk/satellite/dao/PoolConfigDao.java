package com.kinstalk.satellite.dao;

import com.kinstalk.satellite.domain.PhonePool;
import com.kinstalk.satellite.domain.PoolConfig;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * Created by zhangchuanqi on 16/7/8.
 */
public interface PoolConfigDao {

    String SELECT_ALL_FIELD=" id as id, max_number as maxNumber, min_number as minNumber,groups as groups,size as size,last_maxNumber as lastMaxNumber ";

    String INSERT_ALL_FIELD = " max_number,min_number,groups,size,last_maxNumber ";

    String PHONE_POOL_CONFIG = " `phone_pool_config` ";

    String PHONE_POOL = " `phone_pool` ";


    @Options(useGeneratedKeys = true, keyProperty = "id", useCache = true)
    @Insert("insert into " + PHONE_POOL_CONFIG + " (" + INSERT_ALL_FIELD + ") " +
            "values (#{maxNumber},#{minNumber},#{groups},#{size},0)")
    public long save(PoolConfig poolConfig);


    @Update("update" + PHONE_POOL_CONFIG +
            "set max_number=#{maxNumber},min_number=#{minNumber},groups=#{groups},size=#{size},last_maxNumber=(select MAX(phone_end) from "+PHONE_POOL+" where agent_id <>0 ) where id=#{id}")
    public long update(PoolConfig poolConfig);

    @Update("delete from" +PHONE_POOL_CONFIG+"where id=#{id}")
    public long delete(Long id);


    @Select("select * from"+PHONE_POOL)
    public List<PhonePool> selectList();

    @Select("select count(1) as remainGroups,Max(phone_end-phone_start+1) as remainSize from "+PHONE_POOL+" where agent_id=0")

    public PoolConfig selectRemain();

    @Select("select Max(phone_end) from "+PHONE_POOL+"where agent_id<>0")

    public String selectMax();
}
