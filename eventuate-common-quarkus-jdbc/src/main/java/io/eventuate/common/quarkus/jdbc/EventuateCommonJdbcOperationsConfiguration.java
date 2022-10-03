package io.eventuate.common.quarkus.jdbc;

import io.eventuate.common.jdbc.*;
import io.eventuate.common.jdbc.sqldialect.EventuateSqlDialect;
import io.eventuate.common.jdbc.sqldialect.SqlDialectSelector;
import io.quarkus.arc.DefaultBean;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.enterprise.inject.Instance;
import javax.inject.Singleton;
import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.Optional;

@Singleton
public class EventuateCommonJdbcOperationsConfiguration {

  @Singleton
  @DefaultBean
  public OutboxPartitioningSpec outboxPartitioningSpec() {
    return OutboxPartitioningSpec.DEFAULT;
  }

  @Singleton
  public EventuateCommonJdbcOperations eventuateCommonJdbcOperations(EventuateJdbcStatementExecutor eventuateJdbcStatementExecutor,
                                                                     SqlDialectSelector sqlDialectSelector,
                                                                     @ConfigProperty(name = "eventuateDatabase") String dbName,
                                                                     OutboxPartitioningSpec outboxPartitioningSpec) {

    EventuateSqlDialect eventuateSqlDialect = sqlDialectSelector.getDialect(dbName, Optional.empty());

    return new EventuateCommonJdbcOperations(new EventuateJdbcOperationsUtils(eventuateSqlDialect), eventuateJdbcStatementExecutor, eventuateSqlDialect, outboxPartitioningSpec);
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
