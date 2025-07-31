package com.kenny.uaa.service;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

@Service
@ConditionalOnProperty(prefix = "kenny.sms-provider", name = "name", havingValue = "ali")
public class SmsServiceAliSmsImpl implements SmsService {
    @Override
    public void send(String mobile, String msg) {

    }
}

