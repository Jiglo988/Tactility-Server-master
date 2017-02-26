package org.hyperion.rs2.model.punishment;

import org.hyperion.rs2.model.EntityHandler;
import org.hyperion.rs2.model.Player;
import org.hyperion.rs2.model.Position;
import org.hyperion.rs2.model.content.misc2.Edgeville;
import org.hyperion.rs2.model.content.misc2.Jail;

public enum Type {

    JAIL{
        public void apply(final Player player){
            player.setTeleportTarget(Jail.POSITION);
        }

        public boolean isApplied(final Player player){
            return Jail.inJail(player);
        }

        public void unapply(final Player player){
            player.setTeleportTarget(Edgeville.POSITION);
        }
    },
    YELL_MUTE{
        public void apply(final Player player){
            player.yellMuted = true;
        }

        public boolean isApplied(final Player player){
            return player.yellMuted;
        }

        public void unapply(final Player player){
            player.yellMuted = false;
        }
    },
    MUTE{
        public void apply(final Player player){
            player.isMuted = true;
        }

        public boolean isApplied(final Player player){
            return player.isMuted;
        }

        public void unapply(final Player player){
            player.isMuted = false;
        }
    },
    BAN{
        public void apply(final Player player){
            EntityHandler.deregister(player);
        }

        public boolean isApplied(final Player player){
            return true;
        }

        public void unapply(final Player player){
        }
    },
    WILDY_FORBID{
        public void apply(final Player player){
            player.setTeleportTarget(Jail.POSITION);
        }

        public boolean isApplied(final Player player){
            return !player.getPosition().inPvPArea();
        }

        public void unapply(final Player player){
            player.setTeleportTarget(Position.create(3087, 3493, 0));
        }
    };

    public void apply(final Player player){
        throw new AbstractMethodError("will never happen");
    }

    public boolean isApplied(final Player player){
        throw new AbstractMethodError("will never happen");
    }

    public void unapply(final Player player){
        throw new AbstractMethodError("will never happen");
    }
}
