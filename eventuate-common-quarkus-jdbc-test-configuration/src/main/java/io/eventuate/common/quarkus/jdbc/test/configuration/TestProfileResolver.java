package io.eventuate.common.quarkus.jdbc.test.configuration;

import io.quarkus.test.junit.QuarkusTestProfile;

public class TestProfileResolver implements QuarkusTestProfile {
  @Override
  public String getConfigProfile() {
    return System.getenv("EVENTUATEDATABASE");
  }
}
