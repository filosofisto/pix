package com.xti.bank.client.antifraud;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

@HttpExchange("/api/antifraud")
public interface AntifraudClient {

    @PostExchange("/evaluate")
    AntifraudResponse evaluate(@RequestBody AntifraudRequest request);
}
