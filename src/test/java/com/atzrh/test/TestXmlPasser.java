package com.atzrh.test;

import com.ssm.xml.XmlPasser;
import com.sun.org.apache.bcel.internal.generic.NEW;
import org.junit.Test;
import org.omg.IOP.ComponentIdHelper;

/**
 * @author zrh
 * @version 1.0.0
 * @title TestXmlPasser
 * @description <TODO description class purpose>
 * @create 2023/11/4 09:37
 **/
public class TestXmlPasser {
    public static void main(String[] args) {

    }
    @Test
    public void test1(){
        String document = XmlPasser.getBasePackage("miniSSM.xml");
        System.out.println(document);
    }
}
