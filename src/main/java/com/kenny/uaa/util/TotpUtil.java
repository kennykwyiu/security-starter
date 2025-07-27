package com.kenny.uaa.util;

import com.eatthepath.otp.TimeBasedOneTimePasswordGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.crypto.KeyGenerator;
import java.security.NoSuchAlgorithmException;
import java.time.Duration;

@Component
@RequiredArgsConstructor
@Slf4j
public class TotpUtil {
    private static final long TIME_STEP = 60 * 5L;
    private static final int PASSWORD_LENGTH = 6;
    private KeyGenerator keyGenerator;
    private TimeBasedOneTimePasswordGenerator totp;

    {
        try {
            totp = new TimeBasedOneTimePasswordGenerator(Duration.ofSeconds(TIME_STEP), PASSWORD_LENGTH);
            keyGenerator = KeyGenerator.getInstance(totp.getAlgorithm());
            keyGenerator.init(512);
        } catch (NoSuchAlgorithmException e) {
            log.error("Algorithm not found: {}", e.getLocalizedMessage());
        }
    }
}
