package com.test.demo.ser;

import lombok.extern.slf4j.Slf4j;

import java.sql.*;
/**
 * 连接 和 关闭
 * @author  dxt
 * @date 2019年11月2日18:26:05
 * */
@Slf4j
public class ConnectTool {
   static {
        try {
            //初始化JDBC驱动并让驱动加载到jvm中
            Class.forName(ConnectParam.DRIVER);
        } catch (ClassNotFoundException e) {
            log.error("驱动加载失败 可能为空："+e);
        }
    }

    public  static Connection getConnection(){

        Connection conn = null;
        try {
            //连接数据库
            conn = DriverManager.getConnection(ConnectParam.URL,ConnectParam.USER,ConnectParam.PASS);
            conn.setAutoCommit(true);
        } catch (SQLException e) {
           log.error("连接创建失败："+e);
        }
        return conn;
    }

    //关闭连接
    public static void close(Object o){
        if (o == null){
            return;
        }
        if (o instanceof ResultSet){
            try {
                ((ResultSet)o).close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else if(o instanceof Statement){
            try {
                ((Statement)o).close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else if (o instanceof Connection){
            Connection c = (Connection)o;
            try {
                if (!c.isClosed()){
                    c.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }


    public static void close(ResultSet rs, Statement stmt,
                             Connection conn){
        close(rs);
        close(stmt);
        close(conn);
    }

    public static void close(ResultSet rs,
                             Connection conn){
        close(rs);
        close(conn);
    }

}
