/*
 * Copyright 2002-2015 SCOOP Software GmbH
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.copperengine.core.persistent;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Timestamp;
import java.util.List;

import org.copperengine.core.Acknowledge;
import org.copperengine.core.DuplicateIdException;
import org.copperengine.core.Response;
import org.copperengine.core.Workflow;
import org.copperengine.core.batcher.BatchCommand;

/**
 * MySQL implementation of the {@link DatabaseDialect} interface.
 *
 * @author austermann
 */
public class MySqlDialect extends AbstractSqlDialect {

    protected PreparedStatement createUpdateStateStmt(final Connection c, final int max) throws SQLException {
        final Timestamp NOW = new Timestamp(System.currentTimeMillis());
        PreparedStatement pstmt = c.prepareStatement(queryUpdateQueueState + " LIMIT 0," + max);
        pstmt.setTimestamp(1, NOW);
        pstmt.setTimestamp(2, NOW);
        return pstmt;
    }

    protected PreparedStatement createDequeueStmt(final Connection c, final String ppoolId, final int max) throws SQLException {
        PreparedStatement dequeueStmt = c.prepareStatement("select id,priority,data,object_state,creation_ts from COP_WORKFLOW_INSTANCE where id in (select WORKFLOW_INSTANCE_ID from COP_QUEUE where ppool_id = ? order by priority, last_mod_ts) LIMIT 0," + max);
        dequeueStmt.setString(1, ppoolId);
        return dequeueStmt;
    }

    protected PreparedStatement createDeleteStaleResponsesStmt(final Connection c, final int MAX_ROWS) throws SQLException {
        PreparedStatement stmt = c.prepareStatement("delete from COP_RESPONSE where response_timeout < ? and not exists (select * from COP_WAIT w where w.correlation_id = COP_RESPONSE.correlation_id LIMIT " + MAX_ROWS + ")");
        stmt.setTimestamp(1, new Timestamp(System.currentTimeMillis()));
        return stmt;
    }

    @SuppressWarnings("rawtypes")
    @Override
    public BatchCommand createBatchCommand4error(Workflow<?> w, Throwable t, DBProcessingState dbProcessingState, Acknowledge ack) {
        return new SqlSetToError.Command((PersistentWorkflow<?>) w, t, dbProcessingState, System.currentTimeMillis() + dbBatchingLatencyMSec, ack);
    }

    @SuppressWarnings("rawtypes")
    @Override
    public BatchCommand createBatchCommand4NotifyNoEarlyResponseHandling(Response<?> response, Acknowledge ack) throws Exception {
        return new MySqlNotifyNoEarlyResponseHandling.Command(response, serializer, defaultStaleResponseRemovalTimeout, System.currentTimeMillis() + dbBatchingLatencyMSec, ack);
    }

    @Override
    public void insert(List<Workflow<?>> wfs, Connection con) throws DuplicateIdException, Exception {
        try {
            super.insert(wfs, con);
        } catch (SQLException e) {
            if (e instanceof SQLIntegrityConstraintViolationException || (e.getCause() != null && e.getCause() instanceof SQLIntegrityConstraintViolationException)) {
                throw new DuplicateIdException(e);
            }
            throw e;
        }
    }

    @Override
    public String getDialectDescription() {
        return "MySQL";
    }
}
