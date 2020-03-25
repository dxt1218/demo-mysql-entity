package com.test.demo.ser;

import com.test.demo.entity.ColumnInfo;
import com.test.demo.entity.TableInfo;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 创建实体工具
 * @author  dxt
 * @date 2019年11月3日18:45:24
 * */
@Slf4j
public class WriteEntityTool {

    private List<TableInfo> tables = ConnectParam.Tables;
    /**
     * 写实体类
     * */
    public void WriteEntity(String tableName,String type) throws IOException {
        PrintWriter pw = null;
        //解析生成实体java文件的所有内容
        String content = "";
        if("p".equals(type)){
            content=parsePlus(tableName);
        }else {
            content=parse(tableName);
        }
        //生成文件路径
        String path = pkgDirName();
        String javaPath = path + "/" + AllTable.convertSnakeToPasca(tableName) + "Entity.java";
        FileWriter fw = new FileWriter(javaPath);
        pw = new PrintWriter(fw);
        pw.println(content);
        pw.flush();
        System.out.println("create class 【" + tableName + "】");
        if (pw!= null)
            pw.close();
    }



    //解析生成实体java文件的所有内容 适用于Mybatis-Plus框架
    private   String parsePlus(String tableName){
        StringBuffer sb = new StringBuffer();
        sb.append("package " + ConnectParam.PackageOutPath + ";\r\n");
        sb.append("\r\n");
        if(haveDate(tableName)){
            sb.append("import java.time.LocalDateTime;\r\n");
        }
        sb.append("import lombok.Data;\r\n");
        sb.append("import com.baomidou.mybatisplus.annotation.IdType;\r\n");
        sb.append("import com.baomidou.mybatisplus.annotation.TableField;\r\n");
        sb.append("import com.baomidou.mybatisplus.annotation.TableId;\r\n");
        sb.append("import com.baomidou.mybatisplus.annotation.TableName;\r\n");
        sb.append("import com.baomidou.mybatisplus.extension.activerecord.Model;\r\n");
        sb.append("import io.swagger.annotations.*;\r\n");
        sb.append("import java.io.Serializable;\r\n");
        sb.append("\r\n");
        // 注释部分
        sb.append("/**\r\n");
        sb.append(" * 表:  " + tableName + "\r\n");
        sb.append(" * @author  " + ConnectParam.authorName + "\r\n");
        sb.append(" * @date  " + ConnectParam.SDF.format(new Date()) + "\r\n");
        sb.append(" */ \r\n");
        sb.append("@Data\r\n");
        sb.append("@ApiModel(\""+tableName+"实体\")\r\n");
        sb.append("@TableName(\""+tableName+"\")\r\n");
        sb.append("public class " + AllTable.convertSnakeToPasca(tableName) + "Entity extends Model<"+AllTable.convertSnakeToPasca(tableName) +"Entity> {\r\n\r\n");//转 UserName
        sb.append("\r\n");
        parseColumns(sb,tableName);

        sb.append("}\r\n");
        log.error(sb.toString());
        return sb.toString();

    }

    //解析生成实体java文件的所有内容
    private   String parse(String tableName){
        StringBuffer sb = new StringBuffer();
        sb.append("package " + ConnectParam.PackageOutPath + ";\r\n");
        sb.append("\r\n");
        if(haveDate(tableName)){
            sb.append("import java.time.LocalDateTime;\r\n");
        }
        sb.append("import lombok.Data;\r\n");
        sb.append("import javax.persistence.Column;\r\n");
        sb.append("import javax.persistence.Id;\r\n");
        sb.append("import javax.persistence.Table;\r\n");
        sb.append("import io.swagger.annotations.*;\r\n");
        sb.append("\r\n");
        // 注释部分
        sb.append("/**\r\n");
        sb.append(" * 表:  " + tableName + "\r\n");
        sb.append(" * @author  " + ConnectParam.authorName + "\r\n");
        sb.append(" * @date  " + ConnectParam.SDF.format(new Date()) + "\r\n");
        sb.append(" */ \r\n");
        sb.append("@Data\r\n");
        sb.append("@ApiModel(\""+tableName+"实体\")\r\n");
        sb.append("@Table(name=\""+tableName+"\")\r\n");
        sb.append("public class " + AllTable.convertSnakeToPasca(tableName) + "Entity{\r\n\r\n");//转 UserName
        sb.append("\r\n");
        parseColumns(sb,tableName);

        sb.append("}\r\n");
        log.error(sb.toString());
        return sb.toString();

    }
    /**
     * 功能：获取并创建实体所在的路径目录
     * @return
     */
    private  String pkgDirName() {
        String dirName = ConnectParam.BasePath + "/src/main/java/" + ConnectParam.PackageOutPath.replace(".", "/");
        File dir = new File(dirName);
        if (!dir.exists()) {
            dir.mkdirs();
            System.out.println("mkdirs dir 【" + dirName + "】");
        }
        return dirName;
    }

    /**
     * @description 生成所有成员变量及注释
     */
    private void parseColumns(StringBuffer sb,String tableName){
        List<ColumnInfo> cs = getColumns(tableName);
        for(int i = 0; i < cs.size(); i++){
            if(i==0){
                sb.append("\t @TableId(value =\""+cs.get(i).getColumnName()+"\",type = IdType.AUTO )\r\n");
            }else {
                sb.append("\t @TableField(\""+cs.get(i).getColumnName()+"\")\r\n");
            }
            sb.append("\t@ApiModelProperty(\""+cs.get(i).getRemarks()+"\")\r\n");
            sb.append("\tprivate "+sqlType2JavaType(cs.get(i).getDataTypeName())+" "+AllTable.convertSnakeToCamel(cs.get(i).getColumnName()) +";//"+cs.get(i).getRemarks()+"\r\n");
            sb.append("\r\n");
            if(i==cs.size()-1){
                sb.append("\t@Override\r\n");
                sb.append("\t  protected Serializable pkVal() { \n" +
                        " return this."+AllTable.convertSnakeToCamel(cs.get(i).getColumnName()) +";}\r\n ");
            }
        }
    }



    //获取某个表的 所有列信息
    private List<ColumnInfo> getColumns(String tableName){
        List<ColumnInfo> cs = new ArrayList<>();
        for(TableInfo t :tables){
            if(t.getTableName().equals(tableName)){
                for(ColumnInfo c :t.getColumnInfos()){
                    cs.add(c);
                }
            }
        }
        return cs;
    }
    //是否有Date参数
    private boolean haveDate(String tableName){
        for(TableInfo t :tables){
            if(t.getTableName().equals(tableName)){
                for(ColumnInfo c :t.getColumnInfos()){
                    if("LocalDateTime".equals(sqlType2JavaType(c.getDataTypeName()))){
                        return true;
                    }
                }
            }
        }
        return false;
    }
    /**
     * @return
     * @description 查找sql字段类型所对应的Java类型
     */
    private String sqlType2JavaType(String sqlType) {
        if (sqlType.equalsIgnoreCase("bit")) {
            return "Boolean";
        } else if (sqlType.equalsIgnoreCase("tinyint")) {
            return "Integer";
        } else if (sqlType.equalsIgnoreCase("smallint")) {
            return "Integer";
        } else if (sqlType.equalsIgnoreCase("int")) {
            return "Integer";
        } else if (sqlType.equalsIgnoreCase("bigint")) {
            return "Integer";
        } else if (sqlType.equalsIgnoreCase("float")) {
            return "float";
        } else if (sqlType.equalsIgnoreCase("numeric")
                || sqlType.equalsIgnoreCase("real") || sqlType.equalsIgnoreCase("money")
                || sqlType.equalsIgnoreCase("smallmoney")) {
            return "Double";
        } else if (sqlType.equalsIgnoreCase("varchar") || sqlType.equalsIgnoreCase("char")
                || sqlType.equalsIgnoreCase("nvarchar") || sqlType.equalsIgnoreCase("nchar")
                || sqlType.equalsIgnoreCase("text")|| sqlType.equalsIgnoreCase("longtext")) {
            return "String";
        } else if (sqlType.equalsIgnoreCase("datetime")) {
            return "LocalDateTime";
        } else if (sqlType.equalsIgnoreCase("image")) {
            return "Blod";
        }else if (sqlType.equalsIgnoreCase("decimal")) {
            return "BigDecimal";
        }
        return null;
    }

}
