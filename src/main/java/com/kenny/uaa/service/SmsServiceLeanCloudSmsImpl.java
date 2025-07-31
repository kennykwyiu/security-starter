package com.kenny.uaa.service;

import com.kenny.uaa.config.AppProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Slf4j
@Service
@ConditionalOnProperty(prefix = "mooc.sms-provider", name = "name", havingValue = "lean-cloud")
public class SmsServiceLeanCloudSmsImpl implements SmsService {
    private final AppProperties appProperties;
    @Override
    public void send(String mobile, String msg) {
        String smsProvider = appProperties.getLeanCloud().getName();
        log.info("SmsServiceLeanCloudSmsImpl - {} sent out the sms : {} from {}",
                smsProvider,
                mobile,
                msg);
    }
}
