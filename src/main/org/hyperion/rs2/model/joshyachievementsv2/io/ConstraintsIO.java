package org.hyperion.rs2.model.joshyachievementsv2.io;

import java.util.List;
import org.hyperion.rs2.model.joshyachievementsv2.constraint.Constraint;
import org.hyperion.rs2.model.joshyachievementsv2.constraint.Constraints;
import org.hyperion.rs2.model.joshyachievementsv2.constraint.impl.DungeoneeringConstraint;
import org.hyperion.rs2.model.joshyachievementsv2.constraint.impl.EquipmentConstraint;
import org.hyperion.rs2.model.joshyachievementsv2.constraint.impl.LocationConstraint;
import org.hyperion.rs2.model.joshyachievementsv2.constraint.impl.PrayerBookConstraint;
import org.hyperion.rs2.model.joshyachievementsv2.constraint.impl.WildLevelConstraint;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class ConstraintsIO extends IOManager<Constraint, Constraints, ConstraintsIO.ConstraintIO>{

    public interface ConstraintIO<T extends Constraint> extends IO<T>{

        default String tag(){
            return "constraint";
        }

        default void encode(final Document doc, final Element root, final T c){
            attr(root, "desc", c.desc());
            encodeTask(doc, root, c);
        }

        void encodeTask(final Document doc, final Element root, final T c);
    }

    protected ConstraintsIO(){
        super("constraints", Constraints::new, c -> c.list);
    }

    protected void populate(){
        put(LocationConstraint.class, new ConstraintIO<LocationConstraint>(){
            public void encodeTask(final Document doc, final Element root, final LocationConstraint c){
                final Element min = create(doc, "min");
                attr(min, "x", c.minX);
                attr(min, "y", c.minY);
                final Element max = create(doc, "max");
                attr(max, "x", c.maxX);
                attr(max, "y", c.maxY);
                final Element area = create(doc, "area");
                attr(area, "height", c.height);
                area.appendChild(min);
                area.appendChild(max);
                root.appendChild(area);
            }

            public LocationConstraint decode(final Element root){
                final Element area = child(root, "area");
                final int height = intAttr(area, "height");
                final Element min = child(area, "min");
                final int minX = intAttr(min, "x");
                final int minY = intAttr(min, "y");
                final Element max = child(area, "max");
                final int maxX = intAttr(max, "y");
                final int maxY = intAttr(max, "y");
                return new LocationConstraint(minX, minY, maxX, maxY, height);
            }
        });

        put(EquipmentConstraint.class, new ConstraintIO<EquipmentConstraint>(){
            public void encodeTask(final Document doc, final Element root, final EquipmentConstraint c){
                final Element equipment = create(doc, "equipment");
                attr(equipment, "slot", c.slot);
                attr(equipment, "quantity", c.itemQuantity);
                equipment.appendChild(ints(doc, "items", "item", "id", c.itemIds));
                root.appendChild(equipment);
            }

            public EquipmentConstraint decode(final Element root){
                final Element equipment = child(root, "equipment");
                final int slot = intAttr(equipment, "slot");
                final int itemQuantity = intAttr(equipment, "quantity");
                final List<Integer> itemIds = ints(equipment, "items", "item", "id");
                return new EquipmentConstraint(slot, itemIds, itemQuantity);
            }
        });

        put(WildLevelConstraint.class, new ConstraintIO<WildLevelConstraint>(){
            public void encodeTask(final Document doc, final Element root, final WildLevelConstraint c){
                final Element wild = create(doc, "wild");
                attr(wild, "minLevel", c.minLevel);
                attr(wild, "maxLevel", c.maxLevel);
                root.appendChild(wild);
            }

            public WildLevelConstraint decode(final Element root){
                final Element wild = child(root, "wild");
                final int minLevel = intAttr(wild, "minLevel");
                final int maxLevel = intAttr(wild, "maxLevel");
                return new WildLevelConstraint(minLevel, maxLevel);
            }
        });

        put(DungeoneeringConstraint.class, new ConstraintIO<DungeoneeringConstraint>(){
            public void encodeTask(final Document doc, final Element root, final DungeoneeringConstraint obj){

            }

            public DungeoneeringConstraint decode(final Element root){
                return new DungeoneeringConstraint();
            }
        });

        put(PrayerBookConstraint.class, new ConstraintIO<PrayerBookConstraint>(){
            public void encodeTask(final Document doc, final Element root, final PrayerBookConstraint c){
                attr(root, "book", c.book.name());
            }

            public PrayerBookConstraint decode(final Element root){
                final PrayerBookConstraint.PrayerBook book = PrayerBookConstraint.PrayerBook.valueOf(attr(root, "book"));
                return new PrayerBookConstraint(book);
            }
        });
    }
}
