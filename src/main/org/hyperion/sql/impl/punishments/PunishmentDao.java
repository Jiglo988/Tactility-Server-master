package org.hyperion.sql.impl.punishments;

import org.hyperion.rs2.model.punishment.Punishment;
import org.hyperion.sql.dao.SqlDao;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;

import java.util.List;

/**
 * Created by Gilles on 4/02/2016.
 */
@RegisterMapper(PunishmentsMapper.class)
public interface PunishmentDao extends SqlDao {
    @SqlQuery("SELECT * FROM punishments WHERE active = 1")
    List<Punishment> load();

    @SqlUpdate("INSERT INTO punishments (issuer, victim, ip, mac, specialUid, target, type, time, duration, unit, reason, active) VALUES (:issuer, :victim, :ip, :mac, :specialUid, :target, :type, :time, :duration, :unit, :reason, 1)")
    void insert(@Bind("issuer") final String issues, @Bind("victim") final String victim, @Bind("ip") final String ip, @Bind("mac") final int mac, @Bind("specialUid") final String specialUid, @Bind("target") final String target, @Bind("type") final String type, @Bind("time") final long time, @Bind("duration") final long duration, @Bind("unit") final String unit, @Bind("reason") final String reason);

    @SqlUpdate("UPDATE punishments SET issuer = :issuer, time = :time, duration = :duration, unit = :unit, reason = :reason WHERE victim = :victim AND target = :target AND type = :type")
    void update(@Bind("issuer") final String issues, @Bind("time") final long time, @Bind("duration") final long duration, @Bind("unit") final String unit, @Bind("reason") final String reason, @Bind("victim") final String victim, @Bind("target") final String target, @Bind("type") final String type);

    @SqlUpdate("UPDATE punishments SET active = :active WHERE victim = :victim AND target = :target AND type = :type")
    void setActive(@Bind("active") final boolean active, @Bind("victim") final String victim, @Bind("target") final String target, @Bind("type") final String type);

    @SqlUpdate("DELETE FROM punishments WHERE victim = :victim AND target = :target AND type = :type")
    void delete(@Bind("victim") final String victim, @Bind("target") final String target, @Bind("type") final String type);

}
