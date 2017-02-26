package org.hyperion.sql.impl.vote;

import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class WaitingVoteMapper implements ResultSetMapper<WaitingVote> {

    @Override
    public WaitingVote map(final int i, final ResultSet rs, final StatementContext ctx) throws SQLException {
        return new WaitingVote(rs.getInt("index"), rs.getString("fakeUsername"), rs.getString("realUsername"), rs.getBoolean("runelocus"), rs.getBoolean("topg"), rs.getBoolean("rspslist"), rs.getBoolean("runelocusProcessed"), rs.getBoolean("topgProcessed"), rs.getBoolean("rspslistProcessed"), rs.getBoolean("processed"), rs.getDate("timestamp"));
    }
}
