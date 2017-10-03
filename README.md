- Edit TracingFactory and change the ip address to a running zipkin instance (i always tested with an in-memory server)
- Do `clean install` to generate the protobuf definitions
- Load up the project in intellij, easier to run the main classes
- Execute `StartClientServerTogether`, it generates a root span with name "client-execution" containing both server calls. Note you need to stop execution manually before starting the next case.
![with root span](with-root-span.JPG "With root span")
- Execute `StartServers`, and then `StartClient` (i.e. both are in a different VM instance) the root span "client-execution" is no longer created 
![without root span](without-root-span.JPG "Without root span")
