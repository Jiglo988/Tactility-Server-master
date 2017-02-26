package org.hyperion.sql.impl.grandexchange.mapper;

import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by Gilles on 4/02/2016.
 */
public class UnitPriceMapper implements ResultSetMapper<Integer> {
    public Integer map(int index, ResultSet rs, StatementContext ctx) throws SQLException {
        return rs.getInt("unitPrice");
    }
}
