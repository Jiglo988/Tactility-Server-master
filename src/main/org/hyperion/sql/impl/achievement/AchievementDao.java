package org.hyperion.sql.impl.achievement;

import org.hyperion.rs2.model.joshyachievementsv2.tracker.AchievementTaskProgress;
import org.hyperion.sql.dao.SqlDao;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;

import java.util.List;

/**
 * Created by Gilles on 3/02/2016.
 */
@RegisterMapper(AchievementMapper.class)
public interface AchievementDao extends SqlDao {

    @SqlUpdate("INSERT INTO achievement_data (playerName, achievementId, taskId, progress, startTime, finishTime) VALUES (:playerName, :achievementId, :taskId, :progress, :startTime, :finishTime)")
    int insertProgress(@Bind("playerName") final String playerName, @Bind("achievementId") final int achievementId, @Bind("taskId") final int taskId, @Bind("progress") final int progress, @Bind("startTime") final String startTime, @Bind("finishTime") final String finishTime);

    @SqlUpdate("UPDATE achievement_data SET progress = :progress, startTime = :startTime, finishTime = :finishTime WHERE playerName = :playerName AND achievementId = :achievementId AND taskId = :taskId")
    int updateProgress(@Bind("playerName") final String playerName, @Bind("achievementId") final int achievementId, @Bind("taskId") final int taskId, @Bind("progress") final int progress, @Bind("startTime") final String startTime, @Bind("finishTime") final String finishTime);

    @SqlQuery("SELECT * FROM achievement_data WHERE playerName = :playerName")
    List<AchievementTaskProgress> getProgress(@Bind("playerName") final String playerName);
}
