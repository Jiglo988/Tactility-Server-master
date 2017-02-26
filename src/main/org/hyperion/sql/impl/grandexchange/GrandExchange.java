package org.hyperion.sql.impl.grandexchange;

import org.hyperion.rs2.model.content.jge.entry.Entry;
import org.hyperion.sql.DbHub;
import org.hyperion.sql.dao.SqlDaoManager;
import org.hyperion.sql.db.Db;

import java.util.List;

/**
 * Created by Gilles on 3/02/2016.
 */
public class GrandExchange extends SqlDaoManager<GrandExchangeDao> {
    public GrandExchange(Db db) {
        super(db, GrandExchangeDao.class);
    }

    public List<Entry> load() {
        try(final GrandExchangeDao dao = open()) {
            return dao.load();
        } catch(Exception ex) {
            if (DbHub.isConsoleDebug())
                ex.printStackTrace();
            return null;
        }
    }

    public boolean updateClaims(final Entry entry) {
        try(final GrandExchangeDao dao = open()) {
            return dao.updateClaims(entry.claims.toSaveString(), entry.playerName, (byte)entry.slot) == 1;
        } catch(Exception ex) {
            if (DbHub.isConsoleDebug())
                ex.printStackTrace();
            return false;
        }
    }

    public boolean updateProgress(final Entry entry) {
        try(final GrandExchangeDao dao = open()) {
            return dao.updateProgress(entry.progress.toSaveString(), entry.playerName, (byte)entry.slot) == 1;
        } catch(Exception ex) {
            if (DbHub.isConsoleDebug())
                ex.printStackTrace();
            return false;
        }
    }

    public boolean updateProgressAndClaims(final Entry entry) {
        try(final GrandExchangeDao dao = open()) {
            return dao.updateProgressAndClaims(entry.progress.toSaveString(), entry.claims.toSaveString(), entry.playerName, (byte)entry.slot) == 1;
        } catch(Exception ex) {
            if (DbHub.isConsoleDebug())
                ex.printStackTrace();
            return false;
        }
    }

    public boolean updateCancelAndClaims(final Entry entry) {
        try(final GrandExchangeDao dao = open()) {
            return dao.updateCancelAndClaims(entry.cancelled, entry.claims.toSaveString(), entry.playerName, (byte)entry.slot) == 1;
        } catch(Exception ex) {
            if (DbHub.isConsoleDebug())
                ex.printStackTrace();
            return false;
        }
    }

    public boolean insert(final Entry entry) {
        try(final GrandExchangeDao dao = open()) {
            return dao.insert(entry.date.toString(), entry.playerName, entry.type.name(), (byte)entry.slot, (short)entry.itemId, entry.itemQuantity, entry.unitPrice, entry.currency.name(), entry.progress.toSaveString(), entry.claims.toSaveString(), entry.cancelled) == 1;
        } catch(Exception ex) {
            if (DbHub.isConsoleDebug())
                ex.printStackTrace();
            return false;
        }
    }

    public boolean delete(final Entry entry) {
        try(final GrandExchangeDao dao = open()) {
            return dao.delete(entry.playerName, (byte)entry.slot) == 1 && dao.insertHistory(entry.date.toString(), entry.playerName, entry.type.name(), (byte)entry.slot, (short)entry.itemId, entry.itemQuantity, entry.unitPrice, entry.currency.name(), entry.progress.toSaveString(), entry.cancelled) == 1;
        } catch(Exception ex) {
            if (DbHub.isConsoleDebug())
                ex.printStackTrace();
            return false;
        }
    }

    public double averagePrice(final int itemId, final Entry.Type type, final Entry.Currency currency) {
        try(final GrandExchangeDao dao = open()) {
            List<Integer> valueList = dao.averagePrice(itemId, type.name(), currency.name());
            return valueList.stream().mapToInt(Integer::intValue).average().orElse(1000.0);
        } catch(Exception ex) {
            if (DbHub.isConsoleDebug())
                ex.printStackTrace();
            return 1000.0;
        }
    }
}
