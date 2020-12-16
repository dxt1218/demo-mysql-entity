package com.test.demo;

import com.test.demo.entity.TableInfo;
import com.test.demo.ser.AllTable;
import com.test.demo.ser.ConnectParam;
import com.test.demo.ser.WriteEntityTool;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

@Slf4j
@SpringBootApplication
public class demo {
    public static void main(String[] args) {
           WriteEntityTool writeEntityTool =  new WriteEntityTool();
         for(TableInfo tableInfo:ConnectParam.Tables){
              try {
                 // writeEntityTool.WriteEntity(tableInfo.getTableName());
                  // writeEntityTool.WriteEntity("market_ticket_base","p");
                   writeEntityTool.WriteEntity(tableInfo.getTableName(),"p");
              } catch (IOException e) {
                  e.printStackTrace();
              }
         }
         System.out.println("list+"+ AllTable.getAllTables().toString());
    }
}
