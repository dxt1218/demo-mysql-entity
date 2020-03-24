package com.test.demo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 列详情
 * @author  dxt
 * @date 2019年11月2日18:26:05
 * */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ColumnInfo {

    private String columnName; //列名

    private Integer dataType; // //对应的java.sql.Types的SQL类型(列类型ID)

    private String dataTypeName; // TYPE_NAME  java.sql.Types类型名称(列类型名称)

    private String remarks;//描述

    /**
     *  0 (columnNoNulls) - 该列不允许为空
     *  1 (columnNullable) - 该列允许为空
     *  2 (columnNullableUnknown) - 不确定该列是否为空
     * */
    private int nullAble;
}
