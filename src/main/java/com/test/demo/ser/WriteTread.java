package com.test.demo.ser;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WriteTread implements  Runnable{

    private  String tableName;


    @Override
    public void run() {

    }
}
