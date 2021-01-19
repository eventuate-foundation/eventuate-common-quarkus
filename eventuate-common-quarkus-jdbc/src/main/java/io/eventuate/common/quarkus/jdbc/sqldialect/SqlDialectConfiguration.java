package io.eventuate.common.quarkus.jdbc.sqldialect;

import io.eventuate.common.jdbc.sqldialect.DefaultEventuateSqlDialect;
import io.eventuate.common.jdbc.sqldialect.EventuateSqlDialect;
import io.eventuate.common.jdbc.sqldialect.MsSqlDialect;
import io.eventuate.common.jdbc.sqldialect.MySqlDialect;
import io.eventuate.common.jdbc.sqldialect.PostgresDialect;
import io.eventuate.common.jdbc.sqldialect.SqlDialectSelector;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.enterprise.inject.Instance;
import javax.inject.Singleton;
import java.util.Optional;
import java.util.stream.Collectors;

@Singleton
public class SqlDialectConfiguration {

  @Singleton
  public MySqlDialect mySqlDialect() {
    return new MySqlDialect();
  }

  @Singleton
  public PostgresDialect postgreSQLDialect() {
    return new PostgresDialect();
  }

  @Singleton
  public MsSqlDialect msSqlDialect() {
    return new MsSqlDialect();
  }

  @Singleton
  public DefaultEventuateSqlDialect defaultSqlDialect(@ConfigProperty(name = "eventuate.current.time.in.milliseconds.sql")
                                                              Optional<String> customCurrentTimeInMillisecondsExpression) {
    return new DefaultEventuateSqlDialect(customCurrentTimeInMillisecondsExpression.orElse(null));
  }

  @Singleton
  public SqlDialectSelector sqlDialectSelector(Instance<EventuateSqlDialect> dialects) {
    return new SqlDialectSelector(dialects.stream().collect(Collectors.toList()));
  }
}
