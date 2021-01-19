package io.eventuate.common.quarkus.inmemorydatabase;

import io.eventuate.common.inmemorydatabase.EventuateDatabaseScriptSupplier;
import io.eventuate.common.inmemorydatabase.EventuateInMemoryDataSourceBuilder;

import javax.annotation.Priority;
import javax.enterprise.inject.Alternative;
import javax.enterprise.inject.Instance;
import javax.inject.Singleton;
import javax.sql.DataSource;
import java.util.stream.Collectors;

@Singleton
@Alternative
@Priority(0)
public class EventuateCommonInMemoryDatabaseConfiguration {
  @Singleton
  public DataSource dataSource(Instance<EventuateDatabaseScriptSupplier> scripts) {
    return new EventuateInMemoryDataSourceBuilder(scripts.stream().collect(Collectors.toList())).build();
  }
}
