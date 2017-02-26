package org.hyperion.rs2.util.rssfeed;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.XMLEvent;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class ReadRss {
    static final String ITEM = "item";
    static final String TITLE = "title";
    static final String LINK = "link";
    static final String DESCRIPTION = "description";
    static final String PUB_DATE = "pubDate";
    static final DateFormat format = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzzzz", Locale.getDefault());
    static XMLEvent event;

    public static List<Article> readFeed(String feedUrl) {
        URL url;
        try {
            url = new URL(feedUrl);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        List<Article> articles = new ArrayList<>();
        try {
            boolean isFeedHeader = true;
            // Set header values initial to the empty string
            String title = "";
            String link = "";
            String description = "";
            String date = "";

            // First create a new XMLInputFactory
            XMLInputFactory inputFactory = XMLInputFactory.newInstance();
            // Setup a new eventReader
            InputStream in = read(url);
            XMLEventReader eventReader = inputFactory.createXMLEventReader(in);
            // read the XML document
            while (eventReader.hasNext()) {
                 event = eventReader.nextEvent();
                if (event.isStartElement()) {
                    String localPart = event.asStartElement().getName()
                            .getLocalPart();
                    switch (localPart) {
                        case ITEM:
                            if (isFeedHeader) {
                                isFeedHeader = false;
                            }
                            event = eventReader.nextEvent();
                            break;
                        case TITLE:
                            title = getCharacterData(eventReader);
                            break;
                        case DESCRIPTION:
                            description = getCharacterData(eventReader);
                            break;
                        case LINK:
                            link = getCharacterData(eventReader);
                            break;
                        case PUB_DATE:
                            date = getCharacterData(eventReader);
                            break;
                    }
                } else if (event.isEndElement()) {
                    if (event.asEndElement().getName().getLocalPart() == (ITEM)) {
                        Date formattedDate = Calendar.getInstance().getTime();
                        try {
                             formattedDate = format.parse(date);
                        } catch(Exception e) {
                            e.printStackTrace();
                        }
                        Article message = new Article(formattedDate, link, title, description);
                        articles.add(message);
                        event = eventReader.nextEvent();
                        continue;
                    }
                }
            }
        } catch (XMLStreamException e) {
            throw new RuntimeException(e);
        }
        return articles;
    }

    private static String getCharacterData(XMLEventReader eventReader)
            throws XMLStreamException {
        String result = "";
        event = eventReader.nextEvent();
        if (event instanceof Characters) {
            result = event.asCharacters().getData();
        }
        return result;
    }

    private static InputStream read(URL url) {
        try {
            return url.openStream();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
} 
