package testclient;

import brave.Tracing;
import brave.grpc.GrpcTracing;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import testclient.Hello.HelloRequest;
import testclient.Hello.HelloResponse;

/**
 * @author <a href="mailto:jorg.heymans@ext.ec.europa.eu">Jorg Heymans</a>
 * @version $Id$
 */
public class Server implements Runnable {

  private static final Logger LOG = LoggerFactory.getLogger(Server.class);
  private final String serviceName;
  private io.grpc.Server server;
  private Tracing tracing;
  private int port;

  public Server(String serviceName, int port) {
    this.serviceName = serviceName;
    tracing = TracingFactory.create(this.serviceName);
    this.port = port;
  }

  public void run() {
    try {
        /* The port on which the server should run */
      server = ServerBuilder.forPort(port).addService(new HelloImpl())
          .intercept(GrpcTracing.newBuilder(tracing).build().newServerInterceptor()).build().start();
      LOG.info("Server '{}'started, listening on {}", serviceName, port);
      server.awaitTermination();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void shutdown() {
    server.shutdown();
    tracing.close();
  }

  private static class HelloImpl extends HelloServiceGrpc.HelloServiceImplBase {

    @Override
    public void sayHello(HelloRequest request, StreamObserver<HelloResponse> responseObserver) {
      responseObserver.onNext(HelloResponse.newBuilder().setResponse("ECHO '" + request.getRequest() + "'").build());
      responseObserver.onCompleted();
    }
  }
}
