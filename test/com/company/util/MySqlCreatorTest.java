package com.company.util;

import org.junit.Test;

import java.util.List;

/**
 * Created by Admin on 2017/9/8.
 */
public class MySqlCreatorTest {
    @Test
    public void createClassContentTest() throws Exception {
        MySqlCreator creator = new MySqlCreator("127.0.0.1", "groupon",
                "root", "honglang");
        creator.createEntityClass();
        System.out.println("OK");
    }


}
