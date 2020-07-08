package org.hypertrace.core.viewgenerator.generators;

import java.nio.ByteBuffer;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.avro.generic.GenericRecord;
import org.hypertrace.core.datamodel.AttributeValue;
import org.hypertrace.core.datamodel.Attributes;
import org.hypertrace.core.datamodel.Event;
import org.hypertrace.core.datamodel.EventRef;
import org.hypertrace.core.datamodel.EventRefType;
import org.hypertrace.core.datamodel.StructuredTrace;
import org.hypertrace.core.datamodel.shared.SpanAttributeUtils;
import org.hypertrace.core.span.constants.RawSpanConstants;
import org.hypertrace.core.span.constants.v1.JaegerAttribute;
import org.hypertrace.core.viewgenerator.JavaCodeBasedViewGenerator;

/**
 * Pre-processing for View Generators, for data that are mostly needed by the the view generators
 */
public abstract class BaseViewGenerator<OUT extends GenericRecord>
    implements JavaCodeBasedViewGenerator<StructuredTrace, OUT> {
  private static final String JAEGER_SERVICE_NAME_ATTR_NAME =
      RawSpanConstants.getValue(JaegerAttribute.JAEGER_ATTRIBUTE_SERVICE_NAME);

  static String getTransactionName(StructuredTrace trace) {
    if (trace.getEventList().size() == 0) {
      return null;
    }

    return trace.getEventList().get(0).getEventName();
  }

  static String getServiceName(Event event) {
    return SpanAttributeUtils.getStringAttribute(event, JAEGER_SERVICE_NAME_ATTR_NAME);
  }

  @Override
  public List<OUT> process(StructuredTrace trace) {
    Map<ByteBuffer, Event> eventMap = new HashMap<>();
    Map<ByteBuffer, ByteBuffer> childToParentEventIds = new HashMap<>();

    for (Event event : trace.getEventList()) {
      eventMap.put(event.getEventId(), event);
    }

    for (Event event : trace.getEventList()) {
      ByteBuffer childEventId = event.getEventId();
      List<EventRef> eventRefs = eventMap.get(childEventId).getEventRefList();
      if (eventRefs != null) {
        eventRefs.stream()
            .filter(eventRef -> EventRefType.CHILD_OF == eventRef.getRefType())
            .forEach(
                eventRef -> {
                  ByteBuffer parentEventId = eventRef.getEventId();
                  childToParentEventIds.put(childEventId, parentEventId);
                });
      }
      // expected only 1 childOf relationship
    }

    return generateView(trace, Collections.unmodifiableMap(childToParentEventIds));
  }

  abstract List<OUT> generateView(
      StructuredTrace structuredTrace, final Map<ByteBuffer, ByteBuffer> childToParentEventIds);

  protected Map<String, String> getAttributeMap(Attributes attributes) {
    Map<String, String> resultMap = new HashMap<>();
    if (attributes == null || attributes.getAttributeMap() == null) {
      return resultMap;
    }
    for (Map.Entry<String, AttributeValue> entry : attributes.getAttributeMap().entrySet()) {
      resultMap.put(entry.getKey(), entry.getValue().getValue());
    }
    return resultMap;
  }
}
