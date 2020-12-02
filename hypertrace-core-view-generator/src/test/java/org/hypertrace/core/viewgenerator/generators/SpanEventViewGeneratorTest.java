package org.hypertrace.core.viewgenerator.generators;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.hypertrace.core.datamodel.AttributeValue;
import org.hypertrace.core.datamodel.Event;
import org.hypertrace.traceenricher.enrichedspan.constants.EnrichedSpanConstants;
import org.hypertrace.traceenricher.enrichedspan.constants.v1.Api;
import org.hypertrace.traceenricher.enrichedspan.constants.v1.BoundaryTypeValue;
import org.hypertrace.traceenricher.enrichedspan.constants.v1.CommonAttribute;
import org.junit.jupiter.api.Test;

public class SpanEventViewGeneratorTest {

  @Test
  public void spanKind() {
    Event mockEvent = MockUtils.createEvent();
    assertEquals("UNKNOWN", SpanEventViewGenerator.getSpanKindFromEnrichedSpan(mockEvent));

    mockEvent
        .getEnrichedAttributes()
        .getAttributeMap()
        .put(EnrichedSpanConstants.getValue(CommonAttribute.COMMON_ATTRIBUTE_SPAN_TYPE),
            buildAttributeValue(EnrichedSpanConstants.getValue(BoundaryTypeValue.BOUNDARY_TYPE_VALUE_ENTRY)));
    assertEquals("ENTRY", SpanEventViewGenerator.getSpanKindFromEnrichedSpan(mockEvent));

    mockEvent
        .getEnrichedAttributes()
        .getAttributeMap()
        .put(EnrichedSpanConstants.getValue(CommonAttribute.COMMON_ATTRIBUTE_SPAN_TYPE),
            buildAttributeValue(EnrichedSpanConstants.getValue(BoundaryTypeValue.BOUNDARY_TYPE_VALUE_EXIT)));
    assertEquals("EXIT", SpanEventViewGenerator.getSpanKindFromEnrichedSpan(mockEvent));

    mockEvent
        .getEnrichedAttributes()
        .getAttributeMap()
        .put(EnrichedSpanConstants.getValue(CommonAttribute.COMMON_ATTRIBUTE_SPAN_TYPE),
            buildAttributeValue(EnrichedSpanConstants.getValue(BoundaryTypeValue.BOUNDARY_TYPE_VALUE_UNSPECIFIED)));
    assertEquals("UNKNOWN", SpanEventViewGenerator.getSpanKindFromEnrichedSpan(mockEvent));

    mockEvent
        .getAttributes()
        .getAttributeMap()
        .put("span.kind", buildAttributeValue("consumer"));
    assertEquals("UNKNOWN", SpanEventViewGenerator.getSpanKindFromEnrichedSpan(mockEvent));
  }

  @Test
  public void statusCode() {
    Event mockEvent = MockUtils.createEvent();
    assertEquals(null, SpanEventViewGenerator.getStatusCodeFromEnrichedSpan(mockEvent));

    mockEvent
        .getEnrichedAttributes()
        .getAttributeMap()
        .put(EnrichedSpanConstants.getValue(Api.API_STATUS_CODE),
            buildAttributeValue("200"));
    assertEquals("200", SpanEventViewGenerator.getStatusCodeFromEnrichedSpan(mockEvent));

  }

  static AttributeValue buildAttributeValue(String value) {
    return AttributeValue.newBuilder().setValue(value).build();
  }
}
