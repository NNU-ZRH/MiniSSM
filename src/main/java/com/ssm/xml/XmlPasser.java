package com.ssm.xml;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.InputStream;

/**
 * @author zrh
 * @version 1.0.0
 * @title XmlPasser
 * @description <解析xml配置文件>
 * @create 2023/11/4 09:27
 **/
public class XmlPasser {
    public static String getBasePackage(String xml){
        try {
            SAXReader saxReader = new SAXReader();
            InputStream stream = XmlPasser.class.getClassLoader().getResourceAsStream(xml);
            //XML文档对象
            Document document = saxReader.read(stream);
            Element rootElement = document.getRootElement();
            Element element = rootElement.element("component-scan");
            Attribute attribute = element.attribute("base-package");
            return attribute.getText();
        }catch (DocumentException e){
            e.printStackTrace();
        }
        return "";
    }
}
