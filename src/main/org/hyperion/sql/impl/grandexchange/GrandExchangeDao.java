package org.hyperion.sql.impl.grandexchange;

import org.hyperion.rs2.model.content.jge.entry.Entry;
import org.hyperion.sql.dao.SqlDao;
import org.hyperion.sql.impl.grandexchange.mapper.GrandExchangeMapper;
import org.hyperion.sql.impl.grandexchange.mapper.UnitPriceMapper;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.Mapper;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;

import java.util.List;

/**
 * Created by Gilles on 3/02/2016.
 */

@RegisterMapper(GrandExchangeMapper.class)
public interface GrandExchangeDao extends SqlDao {
    @SqlQuery("SELECT * FROM ge_entries")
    List<Entry> load();

    @SqlUpdate("UPDATE ge_entries SET claims = :claims WHERE playerName = :playerName AND slot = :slot")
    int updateClaims(@Bind("claims") final String claims, @Bind("playerName") final String playerName, @Bind("slot") final byte slot);

    @SqlUpdate("UPDATE ge_entries SET progress = :progress WHERE playerName = :playerName AND slot = :slot")
    int updateProgress(@Bind("progress") final String progress, @Bind("playerName") final String playerName, @Bind("slot") final byte slot);

    @SqlUpdate("UPDATE ge_entries SET progress = :progress, claims = :claims WHERE playerName = :playerName AND slot = :slot")
    int updateProgressAndClaims(@Bind("progress") final String progress, @Bind("claims") final String claims, @Bind("playerName") final String playerName, @Bind("slot") final byte slot);

    @SqlUpdate("UPDATE ge_entries SET cancelled = :cancelled, claims = :claims WHERE playerName = :playerName AND slot = :slot")
    int updateCancelAndClaims(@Bind("cancelled") final boolean cancelled, @Bind("claims") final String claims, @Bind("playerName") final String playerName, @Bind("slot") final byte slot);

    @SqlUpdate("INSERT INTO ge_entries (created, playerName, type, slot, itemId, itemQuantity, unitPrice, currency, progress, claims, cancelled) VALUES (:created, :playerName, :type, :slot, :itemId, :itemQuantity, :unitPrice, :currency, :progress, :claims, :cancelled)")
    int insert(@Bind("created") final String created, @Bind("playerName") final String playerName, @Bind("type") final String type, @Bind("slot") final byte slot, @Bind("itemId") final int itemId, @Bind("itemQuantity") final int itemQuantity, @Bind("unitPrice") final int unitPrice, @Bind("currency") final String currency, @Bind("progress") final String progress, @Bind("claims") final String claims, @Bind("cancelled") final boolean cancelled);

    @SqlUpdate("DELETE FROM ge_entries WHERE playerName = :playerName AND slot = :slot")
    int delete(@Bind("playerName") final String playerName, @Bind("slot") final byte slot);

    @SqlUpdate("INSERT INTO ge_history (created, playerName, type, slot, itemId, itemQuantity, unitPrice, currency, progress, cancelled) VALUES (:created, :playerName, :type, :slot, :itemId, :itemQuantity, :unitPrice, :currency, :progress, :cancelled)")
    int insertHistory(@Bind("created") final String created, @Bind("playerName") final String playerName, @Bind("type") final String type, @Bind("slot") final byte slot, @Bind("itemId") final int itemId, @Bind("itemQuantity") final int itemQuantity, @Bind("unitPrice") final int unitPrice, @Bind("currency") final String currency, @Bind("progress") final String progress, @Bind("cancelled") final boolean cancelled);

    @SqlQuery("SELECT unitPrice FROM ge_history WHERE itemId = :itemId AND type = :type AND currency = :currency AND cancelled = 0")
    @Mapper(UnitPriceMapper.class)
    List<Integer> averagePrice(@Bind("itemId") final int itemId, @Bind("type") final String type, @Bind("currency") final String currency);
}
