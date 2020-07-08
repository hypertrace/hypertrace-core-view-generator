package org.hypertrace.core.viewgenerator.generators;

import java.nio.ByteBuffer;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.apache.avro.Schema;
import org.hypertrace.core.datamodel.Event;
import org.hypertrace.core.datamodel.StructuredTrace;
import org.hypertrace.core.datamodel.shared.SpanAttributeUtils;
import org.hypertrace.core.span.constants.RawSpanConstants;
import org.hypertrace.core.span.constants.v1.CensusResponse;
import org.hypertrace.core.span.constants.v1.Grpc;
import org.hypertrace.core.span.constants.v1.Http;
import org.hypertrace.core.span.constants.v1.OCAttribute;
import org.hypertrace.core.span.constants.v1.OCSpanKind;
import org.hypertrace.core.span.constants.v1.OTSpanTag;
import org.hypertrace.core.viewgenerator.api.SpanEventView;

public class SpanEventViewGenerator extends BaseViewGenerator<SpanEventView> {

  private static final String SPAN_KIND_KEY =
      RawSpanConstants.getValue(OCAttribute.OC_ATTRIBUTE_SPAN_KIND);

  private static final String SERVER_VALUE =
      RawSpanConstants.getValue(OCSpanKind.OC_SPAN_KIND_SERVER);

  private static final String CLIENT_VALUE =
      RawSpanConstants.getValue(OCSpanKind.OC_SPAN_KIND_CLIENT);

  // possible status code attribute names
  private static final List<String> STATUS_CODE_KEYS =
      List.of(
          RawSpanConstants.getValue(CensusResponse.CENSUS_RESPONSE_STATUS_CODE),
          RawSpanConstants.getValue(CensusResponse.CENSUS_RESPONSE_CENSUS_STATUS_CODE),
          RawSpanConstants.getValue(OTSpanTag.OT_SPAN_TAG_HTTP_STATUS_CODE),
          RawSpanConstants.getValue(Grpc.GRPC_STATUS_CODE),
          RawSpanConstants.getValue(Http.HTTP_RESPONSE_STATUS_CODE));

  // Hypertrace span kind values
  private static final String SPAN_KIND_UNKNOWN = "UNKNOWN";
  private static final String SPAN_KIND_ENTRY = "ENTRY";
  private static final String SPAN_KIND_EXIT = "EXIT";

  static String getSpanKindFromRawSpan(Event e) {
    String spanKind = SpanAttributeUtils.getStringAttribute(e, SPAN_KIND_KEY);
    if (SERVER_VALUE.equalsIgnoreCase(spanKind)) {
      return SPAN_KIND_ENTRY;
    }
    if (CLIENT_VALUE.equalsIgnoreCase(spanKind)) {
      return SPAN_KIND_EXIT;
    }

    return SPAN_KIND_UNKNOWN;
  }

  static String getStatusCodeFromRawSpan(Event e) {
    return SpanAttributeUtils.getFirstAvailableStringAttribute(e, STATUS_CODE_KEYS);
  }

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
      Event event, ByteBuffer traceId, Map<ByteBuffer, ByteBuffer> childToParentEventIds) {

    SpanEventView.Builder builder = SpanEventView.newBuilder();

    builder.setTenantId(event.getCustomerId());
    builder.setSpanId(event.getEventId());
    builder.setEventName(event.getEventName());
    builder.setSpanKind(getSpanKindFromRawSpan(event));

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
    builder.setStatusCode(getStatusCodeFromRawSpan(event));

    return builder;
  }
}
