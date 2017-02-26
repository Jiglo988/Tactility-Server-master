package org.hyperion.sql.impl.achievement;

import org.hyperion.engine.EngineTask;
import org.hyperion.engine.GameEngine;
import org.hyperion.rs2.model.Player;
import org.hyperion.rs2.model.joshyachievementsv2.tracker.AchievementTaskProgress;
import org.hyperion.sql.DbHub;
import org.hyperion.sql.dao.SqlDaoManager;
import org.hyperion.sql.db.Db;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by Gilles on 3/02/2016.
 */
public class Achievement extends SqlDaoManager<AchievementDao> {

    public Achievement(final Db db) {
        super(db, AchievementDao.class);
    }

    public List<AchievementTaskProgress> loadTaskProgress(final Player player) {
        return GameEngine.submitSql(new EngineTask<List<AchievementTaskProgress>>("load task progress for " + player.getName(), 10, TimeUnit.SECONDS) {
            @Override
            public List<AchievementTaskProgress> call() throws Exception {
                try(final AchievementDao dao = open()) {
                    return dao.getProgress(player.getName().toLowerCase().replace("_", " "));
                } catch(Exception ex) {
                    if (DbHub.isConsoleDebug())
                        ex.printStackTrace();
                    return null;
                }
            }
        }).orElse(null);
    }

    public boolean updateTaskProgress(final Player player, final AchievementTaskProgress atp) {
        return GameEngine.submitSql(new EngineTask<Boolean>("save task progress for " + player.getName(), 2, TimeUnit.SECONDS) {
            @Override
            public Boolean call() throws Exception {
                try(final AchievementDao dao = open()){
                    return dao.updateProgress(player.getName().toLowerCase().replace("_", " "), atp.achievementId, atp.taskId, atp.progress, atp.startDate == null ? null : atp.startDate.toString(), atp.finishDate == null ? null : atp.finishDate.toString()) == 1;
                } catch(Exception ex) {
                    if (DbHub.isConsoleDebug())
                        ex.printStackTrace();
                    return false;
                }
            }
        }).get();
    }

    public boolean insertTaskProgress(final Player player, final AchievementTaskProgress atp){
        return GameEngine.submitSql(new EngineTask<Boolean>("insert task progress for " + player.getName(), 2, TimeUnit.SECONDS) {
            @Override
            public Boolean call() throws Exception {
                try(final AchievementDao dao = open()){
                    return dao.insertProgress(player.getName().toLowerCase().replace("_", " "), atp.achievementId, atp.taskId, atp.progress, atp.startDate == null ? null : atp.startDate.toString(), atp.finishDate == null ? null : atp.finishDate.toString()) == 1;
                } catch(Exception ex) {
                    if (DbHub.isConsoleDebug())
                        ex.printStackTrace();
                    return false;
                }
            }
        }).get();
    }
}
