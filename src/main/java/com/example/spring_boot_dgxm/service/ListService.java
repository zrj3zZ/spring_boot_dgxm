package com.example.spring_boot_dgxm.service;

import oracle.jdbc.internal.OracleTypes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.CallableStatementCallback;
import org.springframework.jdbc.core.CallableStatementCreator;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ListService {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * 将所有信息汇总到一个json返回
     * @return
     */
    public Map itemsList(){
        List<Map<String, Object>> deptlist=dept_info();
        List<Map<String, Object>> userlist=user_info();
        List<Map<String, Object>> dmlist=dept_member();
        Map map=getXnRyList();
        map.put("dept_info",deptlist);
        map.put("user_info",userlist);
        map.put("dept_member",dmlist);
        return map;
    }
    /**
     * 取得部门信息集合
     * @return
     */
    public List<Map<String, Object>> dept_info(){
        String sql = "select id,departmentname as dept_name,'' as create_by,'' as create_date,'' as update_by,'' as update_date,'' as del_flag,'督导系统' as system_flag from orgdepartment where parentdepartmentid!=51  order by id";
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
        return list;
    }

    /**
     * 取得人员信息集合
     * @return
     */
    public List<Map<String, Object>> user_info(){
        StringBuffer sql=new StringBuffer();
        sql.append(" select s.userid as account_id,s.username as name,s.mobile as phone,s.email as email,decode(s.orgroleid,1,0,1) account_type,decode(s.orgroleid,1,0,1) background_perm ");
        sql.append(" ,'' as password,'' as user_img,'' as sex,'' as login_time,'' as account_flag,'' as remarks,s.id,'督导系统' as system_flag, ");
        sql.append(" (select rolename from orgrole where id=s.orgroleid) as job  from orguser s  where s.orgroleid!=3 ");
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql.toString());
        return list;
    }

    /**
     * 取得部门成员集合
     * @return
     */
    public List<Map<String, Object>> dept_member(){
        String sql = "select s.userid as user_id,s.departmentid as dept_id,s.startdate as join_time from orguser s  where s.orgroleid!=3";
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
        return list;
    }

    /**
     *存储过程，取得项目信息和项目组信息集合
     * @return
     */
    public Map getXnRyList() {
        Map resultList = (Map) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call proc_dg_project_info(?,?)}";// 调用的sql
                        CallableStatement cs = con.prepareCall(storedProc);
                        cs.registerOutParameter(1, OracleTypes.CURSOR);// 设置输入参数的值
                        cs.registerOutParameter(2, OracleTypes.CURSOR);// 注册输出参数的类型
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException,DataAccessException {
                        Map map=new HashMap();
                        List<Map<String,Object>> resultsMap = new ArrayList();
                        List<Map<String,Object>> ryresultsMap = new ArrayList();
                        cs.execute();
                        ResultSet rs = (ResultSet) cs.getObject(1);// 获取游标一行的值
                        HashMap rowMap;
                        while (rs.next()) {// 转换每行的返回值到Map中
                            rowMap = new HashMap();
                            rowMap.put("user_id", rs.getString("USER_ID")==null?"":rs.getString("USER_ID"));
                            rowMap.put("proj_type", rs.getString("PROJ_TYPE"));
                            rowMap.put("proj_name", rs.getString("PROJ_NAME"));
                            rowMap.put("proj_number_xt", rs.getString("PROJ_NUMBER_XT"));
                            String flag="正常状态";
                            if(rs.getString("STATUS").toString().equals("已完成"))
                                flag="删除状态";
                            rowMap.put("status", flag);
                            rowMap.put("proj_info", "");
                            rowMap.put("proj_number", "");
                            rowMap.put("proj_audit_status", "");
                            rowMap.put("remarks", "");
                            rowMap.put("id", "");
                            rowMap.put("system_flag", "督导系统");
                            resultsMap.add(rowMap);
                        }
                        ResultSet rsrun = (ResultSet) cs.getObject(2); //获取数据集合
                        while (rsrun.next()){
                            rowMap = new HashMap();
                            rowMap.put("pi_name",rsrun.getString("PROJECTNAME"));
                            rowMap.put("pi_number",rsrun.getString("PROJECTNO"));
                            rowMap.put("user_id",rsrun.getString("USER_ID"));
                            rowMap.put("remarks", "");
                            rowMap.put("del_flag", "");
                            rowMap.put("system_flag", "督导系统");
                            ryresultsMap.add(rowMap);
                        }
                        rs.close();
                        map.put("project_info",resultsMap);
                        map.put("projitem_info",ryresultsMap);
                        return map;
                    }
                });
        return resultList;
    }
}
