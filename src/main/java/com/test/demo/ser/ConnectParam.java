package com.test.demo.ser;

import com.test.demo.entity.TableInfo;
import com.test.demo.ser.AllTable;
import lombok.Data;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.List;


/**
 * 驱动配置
 * @author  dxt
 * @date 2019年11月2日18:26:05
 * */
@Component
@Data
public  class  ConnectParam {
    //获得驱动
    public static  String DRIVER = "com.mysql.jdbc.Driver" ;
    //获得url
    public  static String URL = "jdbc:mysql://49.234.76.112:3306/amor?useUnicode=true&characterEncoding=utf-8";
    //获得连接数据库的用户名
    public static String USER = "root";
    //获得连接数据库的密码
    public static String PASS = "1218";

    public static List<TableInfo> Tables = AllTable.getAllTables();

    //指定包名
    public  static String PackageOutPath = "com.xinhu.wealth.appmc.entity";

    //作者名字
    public static String authorName = "dxt";
    //指定实体生成所在包的路径
    public static String BasePath = new File("").getAbsolutePath();

    public static final SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

}
