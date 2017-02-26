package org.hyperion.sql.impl.achievement;

import org.hyperion.rs2.model.joshyachievementsv2.tracker.AchievementTaskProgress;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

/**
 * Created by Gilles on 3/02/2016.
 */
public class AchievementMapper implements ResultSetMapper<AchievementTaskProgress> {
    @Override
    public AchievementTaskProgress map(int i, ResultSet resultSet, StatementContext statementContext) throws SQLException {
        String startTime = resultSet.getString("startTime");
        String endTime = resultSet.getString("finishTime");
        return new AchievementTaskProgress(
                resultSet.getShort("achievementId"),
                resultSet.getByte("taskId"),
                resultSet.getInt("progress"),
                startTime == null ? null : Timestamp.valueOf(startTime),
                endTime == null ? null : Timestamp.valueOf(endTime)
        );
    }
}
