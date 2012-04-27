package opower.finagle.server;

import com.twitter.finagle.Service;
import com.twitter.finagle.builder.ServerBuilder;
import com.twitter.finagle.http.Http;

import opower.finagle.TestRestEasyService;
import opower.finagle.http.RestEasyUtils;

import org.jboss.netty.handler.codec.http.HttpRequest;
import org.jboss.netty.handler.codec.http.HttpResponse;
import org.jboss.resteasy.core.Dispatcher;

import java.net.InetSocketAddress;

/**
 * Simple server class that does the following:
 *
 * <ol>
 *
 *     <li>Initializes RestEASY infrastructure, including all known <code>@Provider</code>
 *     instances for handling different types of content.</li>
 *
 *     <li>Creates a Finagle Service for handling REST requests.</li>
 *
 *     <li>Creates a Finagle Server to server it up.</li>
 *
 * </ol>
 *
 *
 * @author ed.peters
 */
public final class FinagleServer {

    private FinagleServer() {

    }

    public static void main(String [] args) throws Exception {

        // set up rest easy plumbing & add our test service
        Dispatcher dispatcher = RestEasyUtils.createDispatcher();
        dispatcher.getRegistry().addSingletonResource(new TestRestEasyService());

        // this is what Netty is going to serve up for us
        Service<HttpRequest,HttpResponse> service = RestEasyUtils.createDispatcherService(dispatcher);

        // and here we go!
        ServerBuilder.safeBuild(service, ServerBuilder.get()
                .sendBufferSize(256)
                .codec(Http.get())
                .name("HttpServer")
                .bindTo(new InetSocketAddress("localhost", 10000)));

    }

}