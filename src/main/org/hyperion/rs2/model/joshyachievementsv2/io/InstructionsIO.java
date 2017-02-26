package org.hyperion.rs2.model.joshyachievementsv2.io;

import org.hyperion.rs2.model.joshyachievementsv2.Instructions;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class InstructionsIO extends IOManager<String, Instructions, InstructionsIO.InstructionIO>{

    public interface InstructionIO extends IO<String>{

        default String tag(){
            return "line";
        }

    }

    protected InstructionsIO(){
        super("instructions", Instructions::new, i -> i.list);
    }

    protected void populate(){
        put(String.class, new InstructionIO(){
            public void encode(final Document doc, final Element root, final String line){
                root.setTextContent(line);
            }

            public String decode(final Element root){
                return root.getTextContent();
            }
        });
    }
}
