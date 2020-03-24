package com.test.demo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
/**
 * 表详情
 * @author  dxt
 * @date 2019年11月2日18:26:05
 * */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TableInfo {

    private String tableName; //表名

    private String tableType; //表类型

    private String remarks; //表备注

    private List<ColumnInfo> columnInfos;//列详情
}
