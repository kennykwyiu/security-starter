package com.kenny.uaa.service;

import com.kenny.uaa.config.AppProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "kenny.sms-provider", name = "name", havingValue = "ali")
public class SmsServiceAliSmsImpl implements SmsService {
    private final AppProperties appProperties;
    @Override
    public void send(String mobile, String msg) {
        String smsProvider = appProperties.getSmsProvider().getName();
        log.info("SmsServiceAliSmsImpl - {} sent out the sms : {} from {}", smsProvider, msg, mobile);
    }
}

