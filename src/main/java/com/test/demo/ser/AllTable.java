package com.test.demo.ser;

import com.test.demo.entity.ColumnInfo;
import com.test.demo.entity.TableInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * 获取数据库方法
 * @author  dxt
 * @date 2019年11月2日18:26:05
 *
 * */
@Slf4j
public class AllTable {
    @Autowired
    ConnectParam connectParam;

    @Autowired
    TableInfo tableInfo;

    public static List<TableInfo> getAllTables(){
        Connection conn =  ConnectTool.getConnection();
        ResultSet rs = null;
        ResultSet rs2 = null;
        /**
         * 设置连接属性,使得可获取到表的REMARK(备注)
         * Mysql 不支持
         */
        //((MysqlConnection)conn).setRemarksReporting(true);
        List<TableInfo> tables = new ArrayList<>();
        try {
            String[] types = { "TABLE" };
            DatabaseMetaData dbmd  = conn.getMetaData();//元数据
            rs = dbmd.getTables(null, null, "%", types);
            while (rs.next()) {
                String tableName = rs.getString("TABLE_NAME");  //表名
                if(!"WARNING".equals(tableName)){//多一个 WARNING 不存在的表
                    TableInfo temp =new TableInfo();
                    String tableType = rs.getString("TABLE_TYPE");  //表类型
                    String remarks = rs.getString("REMARKS");       //表备注
                    temp.setTableName(tableName);
                    temp.setRemarks(remarks);
                    temp.setTableType(tableType);
                    log.info(tableName + " - " + tableType + " - " + remarks);
                    rs2 =dbmd.getColumns(null, null, tableName, null);
                    tables.add(temp);
                    List<ColumnInfo> columnInfos = new ArrayList<>();
                    while(rs2.next()){
                        ColumnInfo columnInfo = new ColumnInfo();
                        String columnName = rs2.getString("COLUMN_NAME");  //列名
                        int dataType = rs2.getInt("DATA_TYPE");     //对应的java.sql.Types的SQL类型(列类型ID)
                        String dataTypeName = rs2.getString("TYPE_NAME");  //java.sql.Types类型名称(列类型名称)
                        /**
                         *  0 (columnNoNulls) - 该列不允许为空
                         *  1 (columnNullable) - 该列允许为空
                         *  2 (columnNullableUnknown) - 不确定该列是否为空
                         */
                        int nullAble = rs2.getInt("NULLABLE");  //是否允许为null
                        String columnRemarks = rs2.getString("REMARKS");  //列描述
                        String columnDef = rs2.getString("COLUMN_DEF");  //默认值
                        columnInfo.setColumnName(columnName);//转换获取的 表列名
                        columnInfo.setDataType(dataType);
                        columnInfo.setDataTypeName(dataTypeName);
                        columnInfo.setNullAble(nullAble);
                        columnInfo.setRemarks(columnRemarks);
                        System.out.println(columnName +
                                " - " + dataType + " - " + dataTypeName + " - " + nullAble + " - " + columnRemarks + " - " + columnDef + " - " );
                        columnInfos.add(columnInfo);
                    }
                    temp.setColumnInfos(columnInfos);
                }
            }
        } catch (SQLException e) {
           log.error("getTables获取方法异常："+e);
        }finally {
            ConnectTool.close(rs,conn);
            ConnectTool.close(rs2);
        }
        return tables;
    }

    /**
     * 由下划线(蛇型）命名法，转化成驼峰命名法
     *例子：userName
     * @param name
     */
    public static String convertSnakeToCamel(String name) {
        if (name == null || name.length() == 0) {
            return name;
        }

        char[] nameChars = name.toCharArray();
        boolean previousLine = false;
        for (int i=0; i<nameChars.length; i++) {
            if (previousLine) {
                nameChars[i] -= 32;
                previousLine = false;
            }
            if (nameChars[i] == "_".charAt(0)) {
                previousLine = true;
            }
        }
        return String.valueOf(nameChars).replaceAll("_", "");
    }

    /**
     * 由下划线(蛇型）命名法，转化成帕斯卡命名法
     * 例子：UserName
     * @param name
     * @return
     */
    public static String convertSnakeToPasca(String name) {
        if (name == null || name.length() == 0) {
            return name;
        }
        name = convertSnakeToCamel(name);
        char[] nameChars = name.toCharArray();
        nameChars[0] -= 32;
        return String.valueOf(nameChars);
    }

    private static String convertCamelToPasca(String name) {
        char[] nameChars = name.toCharArray();
        nameChars[0] -= 32;
        return String.valueOf(nameChars);
    }

}
