package org.hyperion.sql.impl.punishments;

import org.hyperion.rs2.model.punishment.*;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.TimeUnit;

/**
 * Created by Gilles on 4/02/2016.
 */
public class PunishmentsMapper implements ResultSetMapper<Punishment> {
    @Override
    public Punishment map(final int i, final ResultSet rs, final StatementContext ctx) throws SQLException {
        final String specialUidText = rs.getString("specialUid");
        final String[] specialUidParts = specialUidText == null ? new String[0] : specialUidText.split(",");
        int[] specialUid = null;
        if(specialUidParts.length == 20){
            specialUid = new int[20];
            for(int j = 0; j < 20; j++)
                specialUid[j] = Integer.parseInt(specialUidParts[j]);
        }
        return new Punishment(rs.getString("issuer"), rs.getString("victim"), rs.getString("ip"), rs.getInt("mac"), specialUid, Combination.of(Target.valueOf(rs.getString("target")), Type.valueOf(rs.getString("type"))), Time.create(rs.getLong("time"), rs.getLong("duration"), TimeUnit.valueOf(rs.getString("unit"))), rs.getString("reason"));
    }
}
