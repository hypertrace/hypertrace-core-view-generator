package org.hypertrace.core.viewgenerator.generators;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.hypertrace.core.datamodel.AttributeValue;
import org.hypertrace.core.datamodel.Event;
import org.junit.jupiter.api.Test;

public class BaseViewGeneratorTest {
  @Test
  public void getServiceName() {
    Event e = MockUtils.createEvent();
    assertEquals("service1", SpanEventViewGenerator.getServiceName(e));
  }
}
