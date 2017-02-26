package org.hyperion.rs2.model.joshyachievementsv2.io;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public abstract class IOManager<T, W, I extends IO>{

    protected final String tag;

    protected final Function<Collection<T>, W> wrapper;
    protected final Function<W, Collection<T>> unwrapper;

    protected final Map<Class<? extends T>, I> map;

    protected final String childTag;

    protected IOManager(final String tag, final Function<Collection<T>, W> wrapper, final Function<W, Collection<T>> unwrapper){
        this.tag = tag;
        this.wrapper = wrapper;
        this.unwrapper = unwrapper;

        map = new HashMap<>();

        populate();

        childTag = map.values().iterator().next().tag();
    }

    public Element out(final Document doc, final W w){
        return encodeList(doc, unwrapper.apply(w));
    }

    public W in(final Element root){
        return wrapper.apply(decodeList(root));
    }

    protected Element encodeList(final Document doc, final Collection<T> list){
        final Element root = doc.createElement(tag);
        list.stream()
                .map(o -> encode(doc, o))
                .forEach(root::appendChild);
        return root;
    }

    protected Element encode(final Document doc, final T obj){
        return map.get(obj.getClass()).encode(doc, obj);
    }

    protected Collection<T> decodeList(final Element root){
        final Element e = root.getTagName().equals(tag) ? root : (Element)root.getElementsByTagName(tag).item(0);
        final Collection<T> list = new ArrayList<>();
        final NodeList nodes = e.getElementsByTagName(childTag);
        for(int i = 0; i < nodes.getLength(); i++){
            final Node node = nodes.item(i);
            if(node.getNodeType() != Node.ELEMENT_NODE)
                continue;
            final Element oe = (Element) node;
            Optional.ofNullable(decode(oe))
                    .ifPresent(list::add);
        }
        return list;
    }

    protected T decode(final Element root){
        if(root.getAttribute("class") == null || root.getAttribute("class").isEmpty())
            return null;
        try{
            return (T) map.get(Class.forName(root.getAttribute("class"))).decode(root);
        }catch(Exception ex){
            ex.printStackTrace();
            return null;
        }
    }

    protected void put(final Class<? extends T> clazz, final I io){
        map.put(clazz, io);
    }

    protected abstract void populate();
}
