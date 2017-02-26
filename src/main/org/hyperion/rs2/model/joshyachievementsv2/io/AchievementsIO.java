package org.hyperion.rs2.model.joshyachievementsv2.io;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.hyperion.rs2.model.joshyachievementsv2.Achievement;
import org.hyperion.rs2.model.joshyachievementsv2.Achievements;
import org.hyperion.rs2.model.joshyachievementsv2.Instructions;
import org.hyperion.rs2.model.joshyachievementsv2.Interval;
import org.hyperion.rs2.model.joshyachievementsv2.reward.Rewards;
import org.hyperion.rs2.model.joshyachievementsv2.task.Tasks;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class AchievementsIO extends IOManager<Achievement, Achievements, AchievementsIO.AchievementIO>{

    public interface AchievementIO extends IO<Achievement>{

        default String tag(){
            return "achievement";
        }
    }

    public static final File FILE = new File("./data/achievementsv2.xml");

    protected AchievementsIO(){
        super("achievements", Achievements::new, Achievements::values);
    }

    protected void populate(){
        put(Achievement.class, new AchievementIO(){
            public void encode(final Document doc, final Element root, final Achievement a){
                attr(root, "id", a.id);
                attr(root, "title", a.title);
                attr(root, "difficulty", a.difficulty.name());
                if(a.interval != null){
                    final Element ie = create(doc, "interval");
                    attr(ie, "interval", a.interval.interval);
                    attr(ie, "unit", a.interval.unit.name());
                    root.appendChild(ie);
                }
                root.appendChild(IO.instructions.out(doc, a.instructions));
                root.appendChild(IO.tasks.out(doc, a.tasks));
                root.appendChild(IO.rewards.out(doc, a.rewards));
            }

            public Achievement decode(final Element root){
                final int id = intAttr(root, "id");
                final Achievement.Difficulty difficulty = Achievement.Difficulty.valueOf(attr(root, "difficulty"));
                final String title = attr(root, "title");
                final Interval interval = Optional.ofNullable(child(root, "interval"))
                        .map(e -> {
                            final int time = intAttr(e, "interval");
                            final TimeUnit unit = TimeUnit.valueOf(attr(e, "unit"));
                            return new Interval(time, unit);
                        })
                        .orElse(null);
                final Instructions instructions = IO.instructions.in(root);
                final Tasks tasks = IO.tasks.in(root);
                tasks.values().forEach(t -> t.achievementId = id);
                final Rewards rewards = IO.rewards.in(root);
                return new Achievement(id, difficulty, title, interval, instructions, tasks, rewards);
            }
        });
    }

    public Achievements load(){
        if(!FILE.exists()){
            System.out.println("Achievements file not found: " + FILE);
            return null;
        }
        final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try{
            final DocumentBuilder bldr = factory.newDocumentBuilder();
            final Document doc = bldr.parse(FILE);
            return in(doc.getDocumentElement());
        }catch(Exception ex){
            ex.printStackTrace();
            return null;
        }
    }

    public boolean save(final Achievements achievements){
        final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try{
            final DocumentBuilder bldr = factory.newDocumentBuilder();
            final Document doc = bldr.newDocument();
            doc.appendChild(out(doc, achievements));
            final Transformer tr = TransformerFactory.newInstance().newTransformer();
            tr.setOutputProperty(OutputKeys.INDENT, "yes");
            tr.setOutputProperty(OutputKeys.METHOD, "xml");
            tr.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            tr.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
            tr.transform(new DOMSource(doc), new StreamResult(new FileOutputStream(FILE)));
            return true;
        }catch(Exception ex){
            ex.printStackTrace();
            return false;
        }
    }
}
