package org.hyperion.sql.impl.donation;

import org.hyperion.sql.dao.SqlDao;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;

import java.util.List;

@RegisterMapper(DonationMapper.class)
public interface DonationDao extends SqlDao {

    @SqlQuery("SELECT * FROM donator WHERE finished = 0 AND name = :name")
    List<Donation> getActiveForPlayer(@Bind("name") final String name);

    @SqlUpdate("UPDATE donator SET `finished` = 1 WHERE `index` = :index")
    int finish(@Bind("index") final int index);
}
