package testclient;

import brave.Tracing;
import zipkin2.Span;
import zipkin2.reporter.AsyncReporter;
import zipkin2.reporter.urlconnection.URLConnectionSender;

/**
 * @author <a href="mailto:jorg.heymans@ext.ec.europa.eu">Jorg Heymans</a>
 * @version $Id$
 */
public class TracingFactory {


  public static Tracing create(String serviceName) {
    URLConnectionSender sender = URLConnectionSender.create("http://158.166.125.155:9411/api/v2/spans");
    AsyncReporter<Span> reporter = AsyncReporter.create(sender);
    return Tracing.newBuilder().localServiceName(serviceName).spanReporter(reporter).build();
  }
}
