package com.example.nguyenthikieutrang_bai3_module3;

import android.content.Context;
import android.util.Log;

import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

public class XMLSaxParser extends DefaultHandler {
    private ArrayList<Employee> employees;
    private Employee currentEmployee;
    private StringBuilder content;

    public ArrayList<Employee> parseXML(Context context) {
        employees = new ArrayList<>();
        try {
            InputStream is = context.getAssets().open("employees.xml");
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();
            saxParser.parse(is, this);
        } catch (Exception e) {
            Log.e("XMLSaxParser", "Error parsing XML", e);
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

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) {
        content = new StringBuilder();
        if (qName.equals("employee")) {
            currentEmployee = new Employee(
                    attributes.getValue("id"),
                    attributes.getValue("title"),
                    "", ""
            );
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) {
        content.append(new String(ch, start, length));
    }

    @Override
    public void endElement(String uri, String localName, String qName) {
        if (currentEmployee != null) {
            switch (qName) {
                case "name":
                    currentEmployee.setName(content.toString().trim());
                    break;
                case "phone":
                    currentEmployee.setPhone(content.toString().trim());
                    break;
                case "employee":
                    employees.add(currentEmployee);
                    break;
            }
        }
    }
}