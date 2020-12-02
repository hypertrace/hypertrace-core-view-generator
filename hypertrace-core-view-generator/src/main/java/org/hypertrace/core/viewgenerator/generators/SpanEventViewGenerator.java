package org.hypertrace.core.viewgenerator.generators;

import java.nio.ByteBuffer;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.apache.avro.Schema;
import org.hypertrace.core.datamodel.Event;
import org.hypertrace.core.datamodel.StructuredTrace;
import org.hypertrace.core.datamodel.shared.SpanAttributeUtils;
import org.hypertrace.core.viewgenerator.api.SpanEventView;
import org.hypertrace.traceenricher.enrichedspan.constants.EnrichedSpanConstants;
import org.hypertrace.traceenricher.enrichedspan.constants.v1.Api;
import org.hypertrace.traceenricher.enrichedspan.constants.v1.BoundaryTypeValue;
import org.hypertrace.traceenricher.enrichedspan.constants.v1.CommonAttribute;

public class SpanEventViewGenerator extends BaseViewGenerator<SpanEventView> {

  @Override
  List<SpanEventView> generateView(
      StructuredTrace structuredTrace, Map<ByteBuffer, ByteBuffer> childToParentEventIds) {
    return structuredTrace.getEventList().stream()
        .map(
            event ->
                generateViewBuilder(event, structuredTrace.getTraceId(), childToParentEventIds)
                    .build())
        .collect(Collectors.toList());
  }

  @Override
  public String getViewName() {
    return SpanEventView.class.getName();
  }

  @Override
  public Schema getSchema() {
    return SpanEventView.getClassSchema();
  }

  @Override
  public Class<SpanEventView> getViewClass() {
    return SpanEventView.class;
  }

  private SpanEventView.Builder generateViewBuilder(
      Event event, ByteBuffer traceId,
      Map<ByteBuffer, ByteBuffer> childToParentEventIds) {

    SpanEventView.Builder builder = SpanEventView.newBuilder();

    builder.setTenantId(event.getCustomerId());
    builder.setSpanId(event.getEventId());
    builder.setEventName(event.getEventName());
    builder.setSpanKind(getSpanKindFromEnrichedSpan(event));

    // parent_span_id
    ByteBuffer parentEventId = childToParentEventIds.get(event.getEventId());
    if (parentEventId != null) {
      builder.setParentSpanId(parentEventId);
    }

    // trace_id
    builder.setTraceId(traceId);

    // service_name
    builder.setServiceName(getServiceName(event));

    builder.setTags(getAttributeMap(event.getAttributes()));

    // start_time_millis, end_time_millis, duration_millis
    builder.setStartTimeMillis(event.getStartTimeMillis());
    builder.setEndTimeMillis(event.getEndTimeMillis());
    builder.setDurationMillis(event.getEndTimeMillis() - event.getStartTimeMillis());

    // display_span_name
    builder.setDisplaySpanName(event.getEventName());

    // status_code
    builder.setStatusCode(getStatusCodeFromEnrichedSpan(event));

    return builder;
  }

  static String getStatusCodeFromEnrichedSpan(Event e) {
    return SpanAttributeUtils.getStringAttribute(
        e, EnrichedSpanConstants.getValue(Api.API_STATUS_CODE));
  }

  static String getSpanKindFromEnrichedSpan(Event e) {
    return SpanAttributeUtils.getStringAttributeWithDefault(
        e, EnrichedSpanConstants.getValue(CommonAttribute.COMMON_ATTRIBUTE_SPAN_TYPE),
        EnrichedSpanConstants.getValue(BoundaryTypeValue.BOUNDARY_TYPE_VALUE_UNSPECIFIED));
  }
}
