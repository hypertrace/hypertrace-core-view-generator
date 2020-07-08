package org.hypertrace.core.viewgenerator.generators;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.hypertrace.core.datamodel.AttributeValue;
import org.hypertrace.core.datamodel.Event;
import org.junit.jupiter.api.Test;

public class SpanEventViewGeneratorTest {
  @Test
  public void spanKind() {
    Event mockEvent = MockUtils.createEvent();
    assertEquals("UNKNOWN", SpanEventViewGenerator.getSpanKindFromRawSpan(mockEvent));

    mockEvent
        .getAttributes()
        .getAttributeMap()
        .put("span.kind", AttributeValue.newBuilder().setValue("server").build());
    assertEquals("ENTRY", SpanEventViewGenerator.getSpanKindFromRawSpan(mockEvent));

    mockEvent
        .getAttributes()
        .getAttributeMap()
        .put("span.kind", AttributeValue.newBuilder().setValue("client").build());
    assertEquals("EXIT", SpanEventViewGenerator.getSpanKindFromRawSpan(mockEvent));

    mockEvent
        .getAttributes()
        .getAttributeMap()
        .put("span.kind", AttributeValue.newBuilder().setValue("consumer").build());
    assertEquals("UNKNOWN", SpanEventViewGenerator.getSpanKindFromRawSpan(mockEvent));
  }

  @Test
  public void statusCode() {
    Event mockEvent = MockUtils.createEvent();
    assertEquals(null, SpanEventViewGenerator.getStatusCodeFromRawSpan(mockEvent));

    mockEvent
        .getAttributes()
        .getAttributeMap()
        .put("http.status_code", AttributeValue.newBuilder().setValue("200").build());
    assertEquals("200", SpanEventViewGenerator.getStatusCodeFromRawSpan(mockEvent));

    mockEvent
        .getAttributes()
        .getAttributeMap()
        .put("status.code", AttributeValue.newBuilder().setValue("ok").build());
    assertEquals("ok", SpanEventViewGenerator.getStatusCodeFromRawSpan(mockEvent));
  }
}
