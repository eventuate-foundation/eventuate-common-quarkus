package io.eventuate.common.quarkus.id;

import io.eventuate.common.id.ApplicationIdGenerator;
import io.eventuate.common.id.DatabaseIdGenerator;
import io.eventuate.common.id.IdGenerator;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.inject.Singleton;
import java.util.Optional;

@Singleton
public class IdGeneratorConfiguration {
  @Singleton
  public IdGenerator idGenerator(@ConfigProperty(name = "eventuate.outbox.id") Optional<Long> id) {
    return id
            .map(DatabaseIdGenerator::new)
            .map(generator -> (IdGenerator)generator)
            .orElseGet(ApplicationIdGenerator::new);
  }
}