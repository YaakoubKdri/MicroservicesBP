package com.kadri.api_gateway.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;


@Component("CustomLoggingFilter")
public class CustomLoggingFilter implements GlobalFilter, Ordered {

    private static final Logger logger = LoggerFactory.getLogger(CustomLoggingFilter.class);

    @Override
    public int getOrder(){
        return -1;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String correlationId = exchange.getRequest().getHeaders()
                .getFirst("X-Correlation-Id");
        logger.info("Incoming request: method={} path={} correlationId={}",
                exchange.getRequest().getMethod(),
                exchange.getRequest().getPath(),
                correlationId);
        return chain.filter(exchange)
                .doOnSuccess(aVoid -> logger.info("Outgoing response: status={} correlationId={}",
                        exchange.getResponse().getStatusCode(),
                        correlationId));
    }
}
