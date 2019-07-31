package com.gant.secondhandsm.dao;

import com.gant.gmbatis.annotation.*;
import com.gant.secondhandsm.domain.TranHistory;

import java.util.List;

/**
 * @author 甘明波
 * @date 2019-07-23
 */
public interface TranDao {

    @Select("select * from tbl_tran_history")
    List<TranHistory> selectAll();

    @SelectDynamic
    TranHistory selectByCondition(TranHistory tranHistory);

    /*@SelectDynamic
    String selectByCondition(TranHistory tranHistory);*/

    @Insert
    int insertDynamic(TranHistory tranHistory);

    @Update
    int updateDynamic(TranHistory tranHistory);

    @Delete("delete from tbl_tran_history where id = ?")
    int deleteById(String id);

    @Delete("delete from tbl_tran_history where id = ?")
    int deleteByIds(String[] id);

    @DeleteDynamic
    int deleteDynamic(TranHistory tranHistory);
}
