package org.hyperion.sql.impl.log;

import org.hyperion.sql.dao.SqlDao;
import org.hyperion.sql.impl.log.mapper.BindIPLog;
import org.hyperion.sql.impl.log.mapper.BindTaskLog;
import org.hyperion.sql.impl.log.type.IPLog;
import org.hyperion.sql.impl.log.type.TaskLog;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlBatch;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.BatchChunkSize;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;

import java.util.Iterator;
import java.util.List;

/**
 * Created by Gilles on 29/02/2016.
 */
public interface LogDao extends SqlDao {

    @SqlUpdate("INSERT INTO player_ips (last_visit, playername, ip) VALUES (:last_visit, :playername, :ip) ON DUPLICATE KEY UPDATE last_visit = :last_visit")
    void addIp(@Bind("last_visit") final String timestamp, @Bind("playername") final String playerName, @Bind("ip") final String ip);

    @SqlQuery("SELECT * FROM player_ips WHERE playername = :playername")
    @RegisterMapper(BindIPLog.class)
    List<IPLog> getIpForPlayer(@Bind("playername") final String playerName);

    @SqlQuery("SELECT * FROM player_ips WHERE ip = :ip")
    @RegisterMapper(BindIPLog.class)
    List<IPLog> getAltsForIp(@Bind("ip") final String ip);

    @SqlBatch("INSERT INTO task_logs " +
            "(timestamp, taskname, classname, executetime) VALUES " +
            "(:timestamp, :taskname, :classname, :executetime)")
    @BatchChunkSize(LogManager.BUFFER_SIZE)
    int[] insertValues(@BindTaskLog final Iterator<TaskLog> logs);

    @SqlUpdate("CREATE TABLE IF NOT EXISTS task_logs (" +
            "  timestamp text NOT NULL," +
            "  taskname text NOT NULL," +
            "  classname text NOT NULL," +
            "  executetime float NOT NULL" +
            ")")
    void initTasks();
}
