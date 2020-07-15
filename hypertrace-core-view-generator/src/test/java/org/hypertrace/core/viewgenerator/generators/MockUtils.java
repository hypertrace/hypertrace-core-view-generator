package org.hypertrace.core.viewgenerator.generators;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;
import org.hypertrace.core.datamodel.AttributeValue;
import org.hypertrace.core.datamodel.Attributes;
import org.hypertrace.core.datamodel.Event;

public class MockUtils {
  public static Event createEvent() {
    Event e = mock(Event.class);

    Map<String, AttributeValue> attributes = new HashMap<>();
    when(e.getAttributes()).thenReturn(Attributes.newBuilder().setAttributeMap(attributes).build());
    when(e.getServiceName()).thenReturn("service1");

    return e;
  }
}
