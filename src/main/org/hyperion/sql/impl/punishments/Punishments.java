package org.hyperion.sql.impl.punishments;

import org.hyperion.rs2.model.punishment.Punishment;
import org.hyperion.sql.DbHub;
import org.hyperion.sql.dao.SqlDaoManager;
import org.hyperion.sql.db.Db;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Gilles on 4/02/2016.
 */
public class Punishments extends SqlDaoManager<PunishmentDao> {

    public Punishments(Db db) {
        super(db, PunishmentDao.class);
    }

    public List<Punishment> load() {
        try(final PunishmentDao dao = open()) {
            return dao.load();
        } catch(Exception ex) {
            if (DbHub.isConsoleDebug())
                ex.printStackTrace();
            return new ArrayList<>();
        }
    }

    public void insert(Punishment punishment) {
        try(final PunishmentDao dao = open()) {
            dao.insert(punishment.getIssuerName(), punishment.getVictimName(), punishment.getVictimIp(), punishment.getVictimMac(),  punishment.getVictimSpecialUidAsString(), punishment.getCombination().getTarget().name(), punishment.getCombination().getType().name(), punishment.getTime().getStartTime(), punishment.getTime().getDuration(), punishment.getTime().getUnit().name(), punishment.getReason());
        } catch(Exception ex) {
            if (DbHub.isConsoleDebug())
                ex.printStackTrace();
        }
    }

    public void update(Punishment punishment) {
        try(final PunishmentDao dao = open()) {
            dao.update(punishment.getIssuerName(), punishment.getTime().getStartTime(), punishment.getTime().getDuration(), punishment.getTime().getUnit().name(), punishment.getReason(), punishment.getVictimName(), punishment.getCombination().getTarget().name(), punishment.getCombination().getType().name());
        } catch(Exception ex) {
            if (DbHub.isConsoleDebug())
                ex.printStackTrace();
        }
    }

    public void setActive(Punishment punishment, boolean isActive) {
        try(final PunishmentDao dao = open()) {
            dao.setActive(isActive, punishment.getVictimName(), punishment.getCombination().getTarget().name(), punishment.getCombination().getType().name());
        } catch(Exception ex) {
            if (DbHub.isConsoleDebug())
                ex.printStackTrace();
        }
    }

    public void delete(Punishment punishment) {
        try(final PunishmentDao dao = open()) {
            dao.delete(punishment.getVictimName(), punishment.getCombination().getTarget().name(), punishment.getCombination().getType().name());
        } catch(Exception ex) {
            if (DbHub.isConsoleDebug())
                ex.printStackTrace();
        }
    }
}
