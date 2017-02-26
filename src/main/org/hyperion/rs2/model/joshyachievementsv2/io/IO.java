package org.hyperion.rs2.model.joshyachievementsv2.io;

import java.util.ArrayList;
import java.util.List;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public interface IO<T>{

    RewardsIO rewards = new RewardsIO();
    TasksIO tasks = new TasksIO();
    ConstraintsIO constraints = new ConstraintsIO();
    InstructionsIO instructions = new InstructionsIO();
    AchievementsIO achievements = new AchievementsIO();

    String tag();

    default Element encode(final Document doc, final T obj){
        final Element e = doc.createElement(tag());
        e.setAttribute("class", obj.getClass().getName());
        encode(doc, e, obj);
        return e;
    }

    void encode(final Document doc, final Element root, final T obj);

    T decode(final Element root);

    default void attr(final Element e, final String attr, final Object val){
        e.setAttribute(attr, val.toString());
    }

    default Element child(final Element root, final String tag){
        return (Element) root.getElementsByTagName(tag).item(0);
    }

    default String attr(final Element e, final String attr){
        return e.getAttribute(attr);
    }

    default int intAttr(final Element e, final String attr){
        return Integer.parseInt(attr(e, attr));
    }

    default boolean boolAttr(final Element e, final String attr){
        return Boolean.parseBoolean(attr(e, attr));
    }

    default Element create(final Document doc, final String tag){
        return doc.createElement(tag);
    }

    default Element ints(final Document doc, final String rootTag, final String childTag, final String childTagAttr, final List<Integer> list){
        final Element root = create(doc, rootTag);
        list.stream()
                .map(i -> {
                    final Element child = create(doc, childTag);
                    attr(child, childTagAttr, i);
                    return child;
                })
                .forEach(root::appendChild);
        return root;
    }

    default List<Integer> ints(final Element root, final String rootTag, final String childTag, final String childTagAttr){
        final List<Integer> list = new ArrayList<>();
        final Element realRoot = root.getTagName().equals(rootTag) ? root : child(root, rootTag);
        final NodeList children = realRoot.getElementsByTagName(childTag);
        for(int i = 0; i < children.getLength(); i++){
            final Node node = children.item(i);
            if(node.getNodeType() != Node.ELEMENT_NODE)
                continue;
            final Element e = (Element) node;
            list.add(intAttr(e, childTagAttr));
        }
        return list;
    }
}
