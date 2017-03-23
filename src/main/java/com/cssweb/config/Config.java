package com.cssweb.config;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

/**
 * Created by chenhf on 14-1-15.
 * 需要换成xml java对象自动转换
 */
public class Config {
    private static Config ourInstance = new Config();
    private String server;
    private String service;

    public static Config getInstance() {
        return ourInstance;
    }

    private Config() {
    }

    public void readConfigXML() {
        XMLInputFactory factory = XMLInputFactory.newInstance();
        String tagContent = "";


        try {
            XMLStreamReader reader = factory.createXMLStreamReader(ClassLoader.getSystemResourceAsStream("config.xml"));

            while (reader.hasNext()) {
                int event = reader.next();

                switch (event) {

                    case XMLStreamConstants.START_ELEMENT:

                        // System.out.println(reader.getLocalName());

                        break;
                    case XMLStreamConstants.CHARACTERS:
                        tagContent = reader.getText().trim();
                        break;
                    case XMLStreamConstants.END_ELEMENT:
                        String elementName = reader.getLocalName();

                        if ("server".equals(elementName))
                            server = tagContent;

                        if ("service".equals(elementName))
                            service = tagContent;

                        break;
                } // end switch
            }//end while
        } catch (XMLStreamException e) {
            e.printStackTrace();
        }
    } // end readConfigXML()

    public String getService() {
        return service;
    }

    public String getServer() {
        return server;
    }

    public static void main(String[] args) {
        Config.getInstance().readConfigXML();
        System.out.println(Config.getInstance().getServer());
        System.out.println(Config.getInstance().getService());
    }
}

