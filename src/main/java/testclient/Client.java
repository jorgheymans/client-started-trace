package testclient;

import brave.Span;
import brave.Tracer.SpanInScope;
import brave.Tracing;
import brave.grpc.GrpcTracing;
import io.grpc.ClientInterceptor;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import testclient.Hello.HelloRequest;
import testclient.Hello.HelloResponse;

/**
 * @author <a href="mailto:jorg.heymans@ext.ec.europa.eu">Jorg Heymans</a>
 * @version $Id$
 */
public class Client {

  private static final Logger LOG = LoggerFactory.getLogger(Client.class);
  private final ManagedChannel server1;
  private final ManagedChannel server2;
  private final Tracing tracing = TracingFactory.create("client");
  private final GrpcTracing grpcTracing = GrpcTracing.create(tracing);

  public Client() {
    ClientInterceptor clientInterceptor = grpcTracing.newClientInterceptor();
    server1 = ManagedChannelBuilder.forAddress("localhost", 4220).intercept(clientInterceptor).usePlaintext(true)
        .build();
    server2 = ManagedChannelBuilder.forAddress("localhost", 4221).intercept(clientInterceptor).usePlaintext(true)
        .build();
  }

  public void start() {
    // invoke both servers in one root span
    Span clientSpan = tracing.tracer().newTrace().name("client-execution").start();
    try (SpanInScope spanInScope = tracing.tracer().withSpanInScope(clientSpan)) {
      String request1 = "first hello";
      LOG.info("sending to server1 {}", request1);
      HelloResponse response1 = HelloServiceGrpc.newBlockingStub(server1)
          .sayHello(HelloRequest.newBuilder().setRequest(request1).build());
      LOG.info("received from server1 {}", response1.getResponse());
      String request2 = "second hello";
      LOG.info("sending to server2 {}", request2);
      HelloResponse response2 = HelloServiceGrpc.newBlockingStub(server2)
          .sayHello(HelloRequest.newBuilder().setRequest(request2).build());
      LOG.info("received from server2 {}", response2.getResponse());
    } finally {
      clientSpan.finish();
    }
  }
}
