package com.xy.experiment.mapper;


import com.xy.experiment.entity.ExperimentUser;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.annotations.UpdateProvider;

import java.util.List;


public interface ExperimentUserMapper {


    @Select("SELECT * FROM experiment_user")
    List<ExperimentUser> getAll();

    @Select("SELECT * FROM experiment_user WHERE account = #{account} limit 1")
    ExperimentUser getOne(String account);

    @Select("SELECT * FROM experiment_user WHERE email = #{email} limit 1")
    ExperimentUser getOneByEmail(String email);

    @Select("SELECT * FROM experiment_user WHERE  account = #{account} or email = #{email} limit 1")
    ExperimentUser getOneByAccOrEmail(String account, String email);

    @Insert("insert into experiment_user(account, password, name, phone, email) values(#{account}, #{password}, #{name}, #{phone}, #{email})")
    int insert(ExperimentUser user);

    @Update("UPDATE experiment_user SET name=#{name}, password=#{password}, email=#{email}, phone=#{phone} WHERE account =#{account}")
    int update(ExperimentUser user);

    @Delete("DELETE FROM experiment_user WHERE account =#{account}")
    void delete(Long account);

    @InsertProvider(type = ExperimentUserProvider.class, method = "batchInsert")
    int batchInsert(@Param("userList") List<ExperimentUser> userList);

    @SelectProvider(type = ExperimentUserProvider.class, method = "queryUser")
    public List<ExperimentUser> queryUser(ExperimentUser user);

    @UpdateProvider(type = ExperimentUserProvider.class, method = "updateUser")
    public int updateUser(@Param("U") ExperimentUser user);

}
