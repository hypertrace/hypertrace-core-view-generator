package org.hypertrace.core.viewgenerator.generators;

import com.google.common.collect.Lists;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.avro.Schema;
import org.hypertrace.core.datamodel.Event;
import org.hypertrace.core.datamodel.StructuredTrace;
import org.hypertrace.core.viewgenerator.api.RawTraceView;

public class RawTraceViewGenerator extends BaseViewGenerator<RawTraceView> {
  @Override
  List<RawTraceView> generateView(
      StructuredTrace structuredTrace, Map<ByteBuffer, ByteBuffer> childToParentEventIds) {
    RawTraceView.Builder builder = RawTraceView.newBuilder();
    builder.setTenantId(structuredTrace.getCustomerId());
    builder.setTraceId(structuredTrace.getTraceId());

    String transactionName = getTransactionName(structuredTrace);
    if (transactionName != null) {
      builder.setTransactionName(transactionName);
    }

    builder.setStartTimeMillis(structuredTrace.getStartTimeMillis());
    builder.setEndTimeMillis(structuredTrace.getEndTimeMillis());
    builder.setDurationMillis(
        structuredTrace.getEndTimeMillis() - structuredTrace.getStartTimeMillis());
    Set<String> services = new HashSet<>();
    for (Event event : structuredTrace.getEventList()) {
      String serviceName = SpanEventViewGenerator.getServiceName(event);
      if (serviceName != null) {
        services.add(serviceName);
      }
    }
    builder.setNumSpans(structuredTrace.getEventList().size());
    builder.setNumServices(services.size());
    builder.setServices(new ArrayList<>(services));
    return Lists.newArrayList(builder.build());
  }

  @Override
  public String getViewName() {
    return RawTraceView.class.getName();
  }

  @Override
  public Schema getSchema() {
    return RawTraceView.getClassSchema();
  }

  @Override
  public Class<RawTraceView> getViewClass() {
    return RawTraceView.class;
  }
}
