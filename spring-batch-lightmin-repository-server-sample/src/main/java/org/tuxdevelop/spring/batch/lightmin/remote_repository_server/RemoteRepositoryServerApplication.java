package org.tuxdevelop.spring.batch.lightmin.remote_repository_server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.tuxdevelop.spring.batch.lightmin.repository.server.configuration.EnableSpringBatchLightminRemoteRepositoryServer;

import javax.sql.DataSource;

@SpringBootApplication
@EnableSpringBatchLightminRemoteRepositoryServer
public class RemoteRepositoryServerApplication {

    public static void main(final String[] args) {
        SpringApplication.run(RemoteRepositoryServerApplication.class, args);
    }

    @Bean
    public DataSource dataSource() {
        final EmbeddedDatabaseBuilder embeddedDatabaseBuilder = new EmbeddedDatabaseBuilder();
        return embeddedDatabaseBuilder
                .addScript("classpath:sql/drop_modified_schema_h2.sql")
                .addScript("classpath:sql/modified_schema_h2.sql")
                .addScript("classpath:sql/inserts.sql")
                .setType(EmbeddedDatabaseType.H2).build();

    }

}
