package com.example.nguyenthikieutrang_bai3_module3;

import android.content.Context;
import android.util.Log;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class XMLDomParser {
    public static ArrayList<Employee> parseXML(Context context) {
        ArrayList<Employee> employees = new ArrayList<>();
        try {
            InputStream is = context.getAssets().open("employees.xml");
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(is);
            doc.getDocumentElement().normalize();

            NodeList nodeList = doc.getElementsByTagName("employee");
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    String id = element.getAttribute("id");
                    String title = element.getAttribute("title");
                    String name = element.getElementsByTagName("name").item(0).getTextContent();
                    String phone = element.getElementsByTagName("phone").item(0).getTextContent();

                    employees.add(new Employee(id, title, name, phone));
                }
            }
        } catch (Exception e) {
            Log.e("XMLDomParser", "Error parsing XML", e);
        }
        return employees;
    }

    public static ArrayList<String> getUniqueTitles(ArrayList<Employee> employees) {
        HashSet<String> uniqueTitles = new HashSet<>();
        for (Employee e : employees) {
            uniqueTitles.add(e.getTitle());
        }
        return new ArrayList<>(uniqueTitles);
    }
}