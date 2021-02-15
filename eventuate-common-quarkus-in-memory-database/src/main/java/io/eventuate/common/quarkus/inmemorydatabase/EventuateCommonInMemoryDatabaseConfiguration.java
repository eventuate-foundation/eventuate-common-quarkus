package io.eventuate.common.quarkus.inmemorydatabase;

import io.eventuate.common.inmemorydatabase.EventuateDatabaseScriptSupplier;
import io.eventuate.common.jdbc.EventuateJdbcStatementExecutor;
import io.eventuate.common.jdbc.EventuateTransactionTemplate;
import io.quarkus.runtime.Startup;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.inject.Singleton;

@Startup
@Singleton
public class EventuateCommonInMemoryDatabaseConfiguration {

  @Inject
  Instance<EventuateDatabaseScriptSupplier> scripts;

  @Inject
  EventuateJdbcStatementExecutor eventuateJdbcStatementExecutor;

  @Inject
  EventuateTransactionTemplate eventuateTransactionTemplate;

  @PostConstruct
  public void dataSource() {
    new EmbeddedDatabaseBuilder(scripts.stream(), eventuateJdbcStatementExecutor, eventuateTransactionTemplate).build();
  }
}
