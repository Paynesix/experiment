package com.xy.experiment.mapper;

import com.github.pagehelper.StringUtil;
import com.xy.experiment.entity.ExperimentUser;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.jdbc.SQL;

import java.util.List;
import java.util.Map;

/**
 * 利用@Provider实现动态SQL
 *
 * @author Lynch
 */
public class ExperimentUserProvider {
    public String queryUser(ExperimentUser user) {
        StringBuffer sql = new StringBuffer("select * from users where 1=1 ");
        if (StringUtil.isNotEmpty(user.getAccount())) {
            sql.append(String.format("and username like '%s'", "%" + user.getAccount() + "%"));
        }

        return sql.toString();
    }

    public String batchInsert(Map map) {
        List<ExperimentUser> userList = (List<ExperimentUser>) map.get("userList");
        StringBuffer sql = new StringBuffer("insert into users (username,password) values ");

        for (ExperimentUser user : userList) {
            sql.append(String.format("('%s', '%s'),", user.getAccount(), user.getPassword()));
        }

        sql = sql.deleteCharAt(sql.length() - 1);
        System.out.println(sql.toString());
        return sql.toString();
    }

    public String updateUser(@Param("U") ExperimentUser user) {
        SQL sql = new SQL() {{
            UPDATE("experiment_user");

            if (StringUtil.isNotEmpty(user.getPassword())) {
                SET("password = #{U.password}");
            }

            WHERE("account = #{U.account}");
        }};

        return sql.toString();
    }


}