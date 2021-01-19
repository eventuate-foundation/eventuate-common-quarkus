package io.eventuate.common.quarkus.jdbc;

import io.eventuate.common.jdbc.EventuateSchema;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.inject.Singleton;
import java.util.Optional;

@Singleton
public class EventuateSchemaConfiguration {
  @Singleton
  public EventuateSchema eventuateSchema(@ConfigProperty(name = "eventuate.database.schema") Optional<String> eventuateDatabaseSchema) {
    return new EventuateSchema(eventuateDatabaseSchema.orElse(null));
  }
}
