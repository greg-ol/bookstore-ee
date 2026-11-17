package com.bookstore.go.bookstoreee;

import com.bookstore.go.bookstoreee.service.BookService;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.ws.rs.ApplicationPath;
import org.glassfish.jersey.inject.hk2.AbstractBinder;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.spi.Container;
import org.glassfish.jersey.server.spi.ContainerLifecycleListener;

@ApplicationPath("/api")
public class BookstoreApplication extends ResourceConfig {

    public BookstoreApplication() {
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("bookstore");

        BookService service = new BookService(factory);

        register(new AbstractBinder() {
            @Override
            protected void configure() {
                bind(service).to(BookService.class);
            }
        });

        register(new ContainerLifecycleListener() {
            @Override
            public void onStartup(Container container) {
            }

            @Override
            public void onReload(Container container) {
            }

            @Override
            public void onShutdown(Container container) {
                factory.close();
            }
        });

        register(BookResource.class);
    }
}