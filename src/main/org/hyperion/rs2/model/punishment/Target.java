package org.hyperion.rs2.model.punishment;

import org.hyperion.rs2.model.Player;
import org.hyperion.rs2.model.World;

import java.util.Arrays;

public enum Target {

    ACCOUNT{
        public void apply(final Player player, final Type type){
            type.apply(player);
        }

        public boolean isApplied(final Player player, final Type type){
            return type.isApplied(player);
        }

        public void unapply(final Player player, final Type type){
            type.unapply(player);
        }
    },
    IP{
        public void apply(final Player player, final Type type){

            type.apply(player);
            for(final Player p : World.getPlayers())
                if(!player.equals(p) && player.getShortIP().equals(p.getShortIP()))
                    type.apply(p);

        }

        public boolean isApplied(final Player player, final Type type){
            if(!type.isApplied(player))
                return false;
            for(final Player p : World.getPlayers())
                if(!player.equals(p) && player.getShortIP().equals(p.getShortIP()) && !type.isApplied(p))
                    return false;
            return true;
        }

        public void unapply(final Player player, final Type type){
            type.unapply(player);
            for(final Player p : World.getPlayers())
                if(!player.equals(p) && player.getShortIP().equals(p.getShortIP()))
                    type.unapply(p);
        }
    },
    MAC{
        public void apply(final Player player, final Type type){
            type.apply(player);
            for(final Player p : World.getPlayers())
                if(!player.equals(p) && player.getUID() == p.getUID())
                    type.apply(p);
        }

        public boolean isApplied(final Player player, final Type type){
            if(!type.isApplied(player))
                return false;
            for(final Player p : World.getPlayers())
                if(!player.equals(p) && player.getUID() == p.getUID() && !type.isApplied(p))
                    return false;
            return true;
        }

        public void unapply(final Player player, final Type type){
            type.unapply(player);
            for(final Player p : World.getPlayers())
                if(!player.equals(p) && player.getUID() == p.getUID())
                    type.unapply(p);
        }
    },
    SPECIAL {
        public void apply(final Player player, final Type type){
            type.apply(player);
            for(final Player p : World.getPlayers())
                if(!player.equals(p) && Arrays.equals(player.specialUid, p.specialUid))
                    type.apply(p);
        }

        public boolean isApplied(final Player player, final Type type){
            if(!type.isApplied(player))
                return false;
            for(final Player p : World.getPlayers())
                if(!player.equals(p) && Arrays.equals(player.specialUid, p.specialUid) && !type.isApplied(p))
                    return false;
            return true;
        }

        public void unapply(final Player player, final Type type){
            type.unapply(player);
            for(final Player p : World.getPlayers())
                if(!player.equals(p) && Arrays.equals(player.specialUid, p.specialUid))
                    type.unapply(p);
        }
    };

    public void apply(final Player player, final Type type){
        throw new AbstractMethodError("will never happen");
    }

    public boolean isApplied(final Player player, final Type type){
        throw new AbstractMethodError("will never happen");
    }

    public void unapply(final Player player, final Type type){
        throw new AbstractMethodError("will never happen");
    }

}
