package io.eventuate.common.quarkus.jdbc;

import io.eventuate.common.jdbc.EventuateCommonJdbcOperations;
import io.eventuate.common.jdbc.EventuateCommonJdbcStatementExecutor;
import io.eventuate.common.jdbc.EventuateJdbcOperationsUtils;
import io.eventuate.common.jdbc.EventuateJdbcStatementExecutor;
import io.eventuate.common.jdbc.EventuateSqlException;
import io.eventuate.common.jdbc.EventuateTransactionTemplate;
import io.eventuate.common.jdbc.sqldialect.EventuateSqlDialect;
import io.eventuate.common.jdbc.sqldialect.SqlDialectSelector;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.enterprise.inject.Instance;
import javax.inject.Singleton;
import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.Optional;

@Singleton
public class EventuateCommonJdbcOperationsConfiguration {

  @Singleton
  public EventuateCommonJdbcOperations eventuateCommonJdbcOperations(EventuateJdbcStatementExecutor eventuateJdbcStatementExecutor,
                                                                     SqlDialectSelector sqlDialectSelector,
                                                                     @ConfigProperty(name = "eventuateDatabase") String dbName) {

    EventuateSqlDialect eventuateSqlDialect = sqlDialectSelector.getDialect(dbName, Optional.empty());

    return new EventuateCommonJdbcOperations(new EventuateJdbcOperationsUtils(eventuateSqlDialect), eventuateJdbcStatementExecutor, eventuateSqlDialect);
  }

  @Singleton
  public EventuateJdbcStatementExecutor eventuateJdbcStatementExecutor(Instance<DataSource> dataSource) {
    return new EventuateCommonJdbcStatementExecutor(() -> {
      try {
        return dataSource.get().getConnection();
      } catch (SQLException e) {
        throw new EventuateSqlException(e);
      }
    });
  }

  @Singleton
  public EventuateTransactionTemplate eventuateTransactionTemplate(EventuateQuarkusTransactionTemplate eventuateQuarkusTransactionTemplate) {
    return eventuateQuarkusTransactionTemplate::executeInTransaction;
  }
}
