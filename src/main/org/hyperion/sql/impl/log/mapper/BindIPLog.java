package org.hyperion.sql.impl.log.mapper;

import org.hyperion.sql.impl.log.type.IPLog;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by Gilles on 17/03/2016.
 */
public class BindIPLog implements ResultSetMapper<IPLog> {
    @Override
    public IPLog map(int i, ResultSet resultSet, StatementContext statementContext) throws SQLException {
        return new IPLog(resultSet.getTimestamp("last_visit"), resultSet.getString("playername"), resultSet.getString("ip"));
    }
}
