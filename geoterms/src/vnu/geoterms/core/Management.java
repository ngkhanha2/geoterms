/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vnu.geoterms.core;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.ArrayList;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import vnu.geoterms.core.Interface.*;
import vnu.geoterms.core.Dictionary;
import vnu.geoterms.core.structure.jspd.DictionaryBuilderSPDict;
import vnu.geoterms.core.structure.jspd.DictionarySPDict;

/**
 *
 * @author Khanh
 */
public class Management implements IManagement {

    private String language;
    private ArrayList<IDictionary> dictionaries = null;

    private String DICTIONARIES_DIRECTORY = System.getProperty("user.dir") + File.separator + "dict";
//    private String CONFIG_FILE = "dict/config.xml";
    private DictionaryReader dictionaryReader = new DictionaryReader();

    public Management() {
        this.dictionaries = new ArrayList<IDictionary>();
        initializeReader();
        loadDictionaries();
    }

    private void initializeReader() {
        this.dictionaryReader.addBuilder(new DictionaryBuilderSPDict());
    }

    private void loadDictionaries() {
        try {
            File[] directories = (new File(DICTIONARIES_DIRECTORY)).listFiles(new FileFilter() {

                @Override
                public boolean accept(File file) {
                    return file.isDirectory();
                }
            });
            for (int i = 0; i < directories.length; ++i) {
                IDictionary dictionary = this.dictionaryReader.read(directories[i].getName());
                if (dictionary != null) {
                    this.dictionaries.add(dictionary);
                }
            }
        } catch (Exception ex) {

        }
    }

//    private void loadConfiguration() {
//        File f = new File(CONFIG_FILE);
//        if (!f.exists()) {
//            return;
//        }
//        try {
//            Document doc = (Document) (DocumentBuilderFactory.newInstance().newDocumentBuilder()).parse(f);
//            doc.getDocumentElement().normalize();
//            NodeList nodes = doc.getElementsByTagName("dict");
//            for (int i = 0; i < nodes.getLength(); ++i) {
//                Node node = nodes.item(i);
//                if (node.getNodeType() == Node.ELEMENT_NODE) {
//                    Element element = (Element) node;
//                    try {
//                        String dir = element.getAttribute("dir");
//                        DictionarySPDict dict = new DictionarySPDict(dir);
//                        addDictionary(dict);
//                        dict.setSelected(Boolean.parseBoolean(element.getAttribute("selected")));
//                    } catch (Exception ex) {
//
//                    }
//                }
//            }
//        } catch (ParserConfigurationException | SAXException | IOException ex) {
//
//        }
//    }
    @Override
    public ArrayList<IDictionary> getDictionaries() {
        return (ArrayList<IDictionary>) this.dictionaries;
    }

    public int insert(String entry, int dictionaryIndex) {
        return 0;
    }

//    public String getEntryHTMLDefinition(String entry) {
//        String value = "<html><body><div>";
//        int realIndex;
//        for (int i = 0; i < this.dictionaries.size(); ++i) {
//            realIndex = this.dictionaries.get(i).indexOf(entry);
//            if (realIndex >= 0) {
//                value += "<div><b>" + this.dictionaries.get(i).getName() + "</b><hr></div>";
//                value += ((Dictionary) this.dictionaries.get(i)).getHTMLDivDefinition(realIndex);
//                value += "<div></div>";
//            }
//        }
//        value += "</div></body></html>";
//        return value;
//    }
//    public ArrayList<String> getEntriesWithKey(String key) {
//        ArrayList<String> list = new ArrayList<>();
//        key = key.trim();
//        if (!key.isEmpty()) {
//            key = key.toLowerCase();
//            for (int i = 0; i < this.dictionaries.size(); ++i) {
//                if (this.dictionaries.get(i).isSelected()) {
//                    int index = this.dictionaries.get(i).find(key);
//                    if (index == -1) {
//                        continue;
//                    }
//                    while (true) {
//                        String s = this.dictionaries.get(i).getEntry(index).toLowerCase().trim();
//                        if (s.startsWith(key)) {
//                            list.add(s);
//                        } else {
//                            break;
//                        }
//                        ++index;
//                    }
//                }
//            }
//        }
//        return list;
//    }
    @Override
    public void addDictionary(IDictionary dictionary) {
        this.dictionaries.add(dictionary);
    }

    @Override
    public void update() {
//        try {
//            Document doc = (Document) (DocumentBuilderFactory.newInstance().newDocumentBuilder()).newDocument();
//            Element dicts = (Element) doc.appendChild(doc.createElement("dicts"));
//            for (int i = 0; i < this.dictionaries.size(); ++i) {
//                Element dict = (Element) dicts.appendChild(doc.createElement("dict"));
//
//                Attr attr = doc.createAttribute("dir");
//                attr.setValue(((Dictionary) this.dictionaries.get(i)).getFileName());
//                dict.setAttributeNode(attr);
//
//                attr = doc.createAttribute("selected");
////                attr.setValue(Boolean.toString(this.dictionaries.get(i).isSelected()));
//                dict.setAttributeNode(attr);
//
//                attr = doc.createAttribute("type");
//                attr.setValue("jspd");
//                dict.setAttributeNode(attr);
//
//                TransformerFactory.newInstance().newTransformer().transform(new DOMSource(doc), new StreamResult(new File(CONFIG_FILE)));
//            }
//        } catch (Exception ex) {
//
//        }
    }

//    @Override
//    public String getCurrentDirectory() {
//        return System.getProperty("user.dir");
//    }
}
