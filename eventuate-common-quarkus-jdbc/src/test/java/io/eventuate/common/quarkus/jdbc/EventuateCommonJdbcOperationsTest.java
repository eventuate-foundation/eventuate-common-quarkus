package io.eventuate.common.quarkus.jdbc;

import io.eventuate.common.id.IdGenerator;
import io.eventuate.common.jdbc.EventuateCommonJdbcOperations;
import io.eventuate.common.jdbc.EventuateDuplicateKeyException;
import io.eventuate.common.jdbc.EventuateJdbcStatementExecutor;
import io.eventuate.common.jdbc.EventuateSchema;
import io.eventuate.common.jdbc.EventuateTransactionTemplate;
import io.eventuate.common.jdbc.sqldialect.EventuateSqlDialect;
import io.eventuate.common.jdbc.sqldialect.SqlDialectSelector;
import io.eventuate.common.jdbc.tests.AbstractEventuateCommonJdbcOperationsTest;
import io.eventuate.common.quarkus.jdbc.test.configuration.TestProfileResolver;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.TestProfile;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.Map;
import java.util.Optional;

@QuarkusTest
@TestProfile(TestProfileResolver.class)
public class EventuateCommonJdbcOperationsTest extends AbstractEventuateCommonJdbcOperationsTest {
  @Inject
  EventuateCommonJdbcOperations eventuateCommonJdbcOperations;

  @Inject
  EventuateJdbcStatementExecutor eventuateJdbcStatementExecutor;

  @Inject
  EventuateTransactionTemplate eventuateTransactionTemplate;

  @Inject
  Instance<DataSource> dataSource;

  @Inject
  IdGenerator idGenerator;

  @Inject
  SqlDialectSelector sqlDialectSelector;

  @ConfigProperty(name = "eventuateDatabase")
  String dbName;

  @Test
  @Override
  public void testEventuateDuplicateKeyException() {
    Assertions.assertThrows(EventuateDuplicateKeyException.class, super::testEventuateDuplicateKeyException);
  }

  @Test
  @Override
  public void testGeneratedIdOfEventsTableRow() {
    super.testGeneratedIdOfEventsTableRow();
  }

  @Test
  @Override
  public void testGeneratedIdOfMessageTableRow() {
    super.testGeneratedIdOfMessageTableRow();
  }

  @Test
  @Override
  public void testInsertIntoEventsTable() throws SQLException {
    super.testInsertIntoEventsTable();
  }

  @Test
  @Override
  public void testInsertIntoMessageTable() throws SQLException {
    super.testInsertIntoMessageTable();
  }

  @Override
  protected EventuateTransactionTemplate getEventuateTransactionTemplate() {
    return eventuateTransactionTemplate;
  }

  @Override
  protected IdGenerator getIdGenerator() {
    return idGenerator;
  }

  @Override
  protected DataSource getDataSource() {
    return dataSource.get();
  }

  @Override
  protected EventuateSqlDialect getEventuateSqlDialect() {
    return sqlDialectSelector.getDialect(dbName, Optional.empty());
  }

  @Override
  protected String insertIntoMessageTable(String payload, String destination, Map<String, String> headers) {
    return eventuateCommonJdbcOperations.insertIntoMessageTable(idGenerator, payload, destination, headers, eventuateSchema);
  }

  @Override
  protected String insertIntoEventsTable(String entityId, String eventData, String eventType, String entityType, Optional<String> triggeringEvent, Optional<String> metadata) {
    return eventuateCommonJdbcOperations.insertIntoEventsTable(idGenerator, entityId, eventData, eventType, entityType, triggeringEvent, metadata, eventuateSchema);
  }

  @Override
  protected void insertIntoEntitiesTable(String entityId, String entityType, EventuateSchema eventuateSchema) {
    String table = eventuateSchema.qualifyTable("entities");
    String sql = String.format("insert into %s values (?, ?, ?);", table);

    eventuateTransactionTemplate.executeInTransaction(() ->
            eventuateJdbcStatementExecutor.update(sql, entityId, entityType, System.nanoTime()));
  }
}
