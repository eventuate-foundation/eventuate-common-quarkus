package io.eventuate.common.quarkus.jdbc;

import javax.inject.Singleton;
import javax.transaction.Transactional;
import java.util.function.Supplier;

@Singleton
public class EventuateQuarkusTransactionTemplate {

  @Transactional
  public <T> T executeInTransaction(Supplier<T> callback) {
    return callback.get();
  };
}
