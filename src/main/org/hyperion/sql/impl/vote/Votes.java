package org.hyperion.sql.impl.vote;

import org.hyperion.rs2.model.Player;
import org.hyperion.sql.DbHub;
import org.hyperion.sql.dao.SqlDaoManager;
import org.hyperion.sql.db.Db;

import java.util.List;

public class Votes extends SqlDaoManager<VoteDao> {

    public Votes(final Db db) {
        super(db, VoteDao.class);
    }

    public List<WaitingVote> getWaiting(Player player) {
        try{
            return dao.getWaiting(player.getName());
        }catch(Exception ex){
            if(DbHub.isConsoleDebug())
                ex.printStackTrace();
            return null;
        }
    }

    public boolean processRunelocus(final WaitingVote vote) {
        try{
            return dao.processRunelocus(vote.index()) == 1;
        }catch(Exception ex){
            if(DbHub.isConsoleDebug())
                ex.printStackTrace();
            return false;
        }
    }

    public boolean processTopg(final WaitingVote vote) {
        try{
            return dao.processTopg(vote.index()) == 1;
        }catch(Exception ex){
            if(DbHub.isConsoleDebug())
                ex.printStackTrace();
            return false;
        }
    }

    public boolean processRspslist(final WaitingVote vote) {
        try{
            return dao.processRspslist(vote.index()) == 1;
        }catch(Exception ex){
            if(DbHub.isConsoleDebug())
                ex.printStackTrace();
            return false;
        }
    }

    public boolean process(final WaitingVote vote) {
        try{
            return dao.process(vote.index()) == 1;
        }catch(Exception ex){
            if(DbHub.isConsoleDebug())
                ex.printStackTrace();
            return false;
        }
    }

    public boolean delete(final WaitingVote vote) {
        try{
            return dao.delete(vote.index()) == 1;
        }catch(Exception ex){
            if(DbHub.isConsoleDebug())
                ex.printStackTrace();
            return false;
        }
    }

    public boolean insertVote(final String name, final int runelocus, final int top100, final int topg) {
        try{
            return dao.insertVote(name, runelocus, top100, topg) == 1;
        }catch(Exception ex){
            if(DbHub.isConsoleDebug())
                ex.printStackTrace();
            return false;
        }
    }

    public boolean insertVote(final Player player, final int runelocus, final int top100, final int topg) {
        return insertVote(player.getName().toLowerCase(), runelocus, top100, topg);
    }
}
