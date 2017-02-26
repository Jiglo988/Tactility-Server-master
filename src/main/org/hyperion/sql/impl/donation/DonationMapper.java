package org.hyperion.sql.impl.donation;

import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class DonationMapper implements ResultSetMapper<Donation> {

    @Override
    public Donation map(final int i, final ResultSet rs, final StatementContext ctx) throws SQLException {
        return new Donation(rs.getString("TOKEN_ID"), rs.getString("name"), rs.getDouble("amount"), rs.getBoolean("finished"), rs.getInt("row"), rs.getTimestamp("currentTime"), rs.getBoolean("passed"), rs.getBoolean("recklesspk"), rs.getInt("index"), rs.getString("method"), rs.getBoolean("chargeBacked"), rs.getString("mail"));
    }
}
