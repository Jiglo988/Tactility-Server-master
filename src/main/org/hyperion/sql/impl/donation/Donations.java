package org.hyperion.sql.impl.donation;

import org.hyperion.rs2.model.Player;
import org.hyperion.sql.DbHub;
import org.hyperion.sql.dao.SqlDaoManager;
import org.hyperion.sql.db.Db;

import java.util.List;

public class Donations extends SqlDaoManager<DonationDao> {

    public Donations(final Db db) {
        super(db, DonationDao.class);
    }

    public List<Donation> getActiveForPlayer(Player player) {
        try{
            return dao.getActiveForPlayer(player.getName());
        } catch(Exception ex){
            if(DbHub.isConsoleDebug())
                ex.printStackTrace();
            return null;
        }
    }

    public boolean finish(final Donation d) {
        try(final DonationDao dao = open()){
            return dao.finish(d.index()) == 1;
        }catch(Exception ex){
            if(DbHub.isConsoleDebug())
                ex.printStackTrace();
            return false;
        }
    }
}
