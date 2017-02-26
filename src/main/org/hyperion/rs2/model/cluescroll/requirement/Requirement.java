package org.hyperion.rs2.model.cluescroll.requirement;

import org.hyperion.rs2.model.Player;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public abstract class Requirement {

    public enum Type{
        LOCATION{
            public LocationRequirement parse(final Element element){
                return LocationRequirement.parse(element);
            }

            public LocationRequirement createDefault(){
                return new LocationRequirement(1, 1, -1);
            }
        },
        EXPERIENCE{
            public ExperienceRequirement parse(final Element element){
                return ExperienceRequirement.parse(element);
            }

            public ExperienceRequirement createDefault(){
                return new ExperienceRequirement(0, 1);
            }
        },
        ITEM{
            public ItemRequirement parse(final Element element){
                return ItemRequirement.parse(element);
            }

            public ItemRequirement createDefault(){
                return new ItemRequirement(1, 1);
            }
        },
        COMBAT_LEVEL{
            public CombatLevelRequirement parse(final Element element){
                return CombatLevelRequirement.parse(element);
            }

            public CombatLevelRequirement createDefault(){
                return new CombatLevelRequirement(1);
            }
        },
        EQUIPMENT{
            public EquipmentRequirement parse(final Element element){
                return EquipmentRequirement.parse(element);
            }

            public EquipmentRequirement createDefault(){
                return new EquipmentRequirement(0, 1);
            }
        };

        public abstract Requirement parse(final Element element);

        public abstract Requirement createDefault();
    }

    private final Type type;

    protected Requirement(final Type type){
        this.type = type;
    }

    public Type getType(){
        return type;
    }

    public Element toElement(final Document doc){
        final Element element = doc.createElement("requirement");
        element.setAttribute("type", type.name());
        append(doc, element);
        return element;
    }

    protected abstract void append(final Document doc, final Element root);

    public abstract boolean apply(final Player player);

    public String toString(){
        return type.name();
    }

}
