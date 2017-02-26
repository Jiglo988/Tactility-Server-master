package org.hyperion.sql.impl.vote;

import org.hyperion.sql.dao.SqlDao;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;

import java.util.List;

@RegisterMapper(WaitingVoteMapper.class)
public interface VoteDao extends SqlDao {

    @SqlQuery("SELECT * FROM waitingVotes WHERE processed = 0 AND realUsername = :username")
    List<WaitingVote> getWaiting(@Bind("username") final String username);

    @SqlUpdate("UPDATE waitingVotes SET runelocusProcessed = 1 WHERE `index` = :index")
    int processRunelocus(@Bind("index") final int index);

    @SqlUpdate("UPDATE waitingVotes SET topgProcessed = 1 WHERE `index` = :index")
    int processTopg(@Bind("index") final int index);

    @SqlUpdate("UPDATE waitingVotes SET rspslistProcessed = 1 WHERE `index` = :index")
    int processRspslist(@Bind("index") final int index);

    @SqlUpdate("UPDATE waitingVotes SET processed = 1 WHERE `index` = :index")
    int process(@Bind("index") final int index);

    @SqlUpdate("DELETE FROM waitingVotes WHERE 'index' = :index")
    int delete(@Bind("index") final int index);

    @SqlUpdate("INSERT INTO votes (name, runelocus, top100, topg) VALUES (:name, :runelocus, :top100, :topg) ON DUPLICATE KEY UPDATE runelocus = runelocus + :runelocus, top100 = top100 + :top100, topg = topg + :topg")
    int insertVote(@Bind("name") final String name, @Bind("runelocus") final int runelocus, @Bind("top100") final int top100, @Bind("topg") final int topg);
}
