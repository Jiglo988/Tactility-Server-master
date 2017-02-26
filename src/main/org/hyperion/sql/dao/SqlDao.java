package org.hyperion.sql.dao;

import java.io.Closeable;

public interface SqlDao extends Closeable {

    void close();
}
