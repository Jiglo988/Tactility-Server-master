package org.hyperion.sql.impl.grandexchange.mapper;

import org.hyperion.rs2.model.content.jge.entry.Entry;
import org.hyperion.rs2.model.content.jge.entry.claim.Claims;
import org.hyperion.rs2.model.content.jge.entry.progress.ProgressManager;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.OffsetDateTime;

/**
 * Created by Gilles on 3/02/2016.
 */
public class GrandExchangeMapper implements ResultSetMapper<Entry> {
    @Override
    public Entry map(int i, ResultSet resultSet, StatementContext statementContext) throws SQLException {
        Entry entry = new Entry(
                OffsetDateTime.parse(resultSet.getString("created")),
                resultSet.getString("playerName"),
                Entry.Type.valueOf(resultSet.getString("type")),
                resultSet.getByte("slot"),
                resultSet.getShort("itemId"),
                resultSet.getInt("itemQuantity"),
                resultSet.getInt("unitPrice"),
                Entry.Currency.valueOf(resultSet.getString("currency"))
        );
        entry.cancelled = resultSet.getBoolean("cancelled");
        entry.progress = ProgressManager.fromSaveString(entry, resultSet.getString("progress"));
        entry.claims = Claims.fromSaveString(entry, resultSet.getString("claims"));
        return entry;
    }
}
