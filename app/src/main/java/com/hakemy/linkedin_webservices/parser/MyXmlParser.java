package com.hakemy.linkedin_webservices.parser;

import com.hakemy.linkedin_webservices.MyJsonClass.JsonFromPojo;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

public class MyXmlParser {

    public static JsonFromPojo[] parseFeed(String content) {

        try {

            boolean inItemTag = false;
            String currentTagName = "";
            JsonFromPojo currentItem = null;
            List<JsonFromPojo> itemList = new ArrayList<>();

            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = factory.newPullParser();
            parser.setInput(new StringReader(content));

            int eventType = parser.getEventType();

            while (eventType != XmlPullParser.END_DOCUMENT) {

                switch (eventType) {
                    case XmlPullParser.START_TAG:
                        currentTagName = parser.getName();
                        if (currentTagName.equals("item")) {
                            inItemTag = true;
                            currentItem = new JsonFromPojo();
                            itemList.add(currentItem);
                        }
                        break;

                    case XmlPullParser.END_TAG:
                        if (parser.getName().equals("item")) {
                            inItemTag = false;
                        }
                        currentTagName = "";
                        break;

                    case XmlPullParser.TEXT:
                        String text = parser.getText();
                        if (inItemTag && currentItem != null) {
                            try {
                                switch (currentTagName) {
                                    case "itemName":
                                        currentItem.setItemName(text);
                                        break;
                                    case "description":
                                        currentItem.setDescription(text);
                                        break;
                                    case "category":
                                        currentItem.setCategory(text);
                                        break;
                                    case "price":
                                        currentItem.setPrice(Double.parseDouble(text));
                                        break;
                                    case "sort":
                                        currentItem.setSort(Integer.parseInt(text));
                                        break;
                                    case "image":
                                        currentItem.setImage(text);
                                    default:
                                        break;
                                }
                            } catch (NumberFormatException e) {
                                e.printStackTrace();
                            }
                        }
                        break;
                }

                eventType = parser.next();

            } // end while loop

            JsonFromPojo[] JsonFromPojos = new JsonFromPojo[itemList.size()];
            return itemList.toArray(JsonFromPojos);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }
}