package io.eventuate.common.quarkus.jdbc.test.configuration;

import io.quarkus.test.junit.QuarkusTestProfile;

import java.util.HashMap;
import java.util.Map;

public class EmbeddedDatabaseProfile implements QuarkusTestProfile {
  @Override
  public Map<String, String> getConfigOverrides() {
    return new HashMap<String, String>() {{
      put("eventuateDatabase", "h2");
      put("quarkus.datasource.db-kind", "h2");
      put("quarkus.datasource.jdbc.url", "jdbc:h2:mem:testdb");
      put("quarkus.datasource.username", "sa");
      put("quarkus.datasource.password", "");
    }};
  }
}
