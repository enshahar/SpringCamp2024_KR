package com.enshahar.SpringCampR2dbcExample

import io.asyncer.r2dbc.mysql.MySqlConnectionFactoryProvider
import org.springframework.boot.autoconfigure.r2dbc.ConnectionFactoryOptionsBuilderCustomizer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.transaction.interceptor.TransactionAttributeSource
import org.springframework.transaction.interceptor.TransactionInterceptor
import java.time.ZoneId

@Configuration
class Config {
    @Bean
    fun mysqlCustomizer(): ConnectionFactoryOptionsBuilderCustomizer =
        ConnectionFactoryOptionsBuilderCustomizer { builder ->
            builder.option(MySqlConnectionFactoryProvider.SERVER_ZONE_ID, ZoneId.of("UTC"))
        }
}