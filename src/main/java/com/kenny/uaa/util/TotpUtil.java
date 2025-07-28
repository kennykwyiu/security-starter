package com.kenny.uaa.util;

import com.eatthepath.otp.TimeBasedOneTimePasswordGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.crypto.KeyGenerator;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.time.Duration;
import java.time.Instant;
import java.util.Base64;

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

    public String createTotp(Key key, Instant time) throws InvalidKeyException {
        int password = totp.generateOneTimePassword(key, time);
        String format = "%0" + PASSWORD_LENGTH + "d";
        return String.format(format, password);
    }

    public boolean verifyTotp(Key key, String code) throws InvalidKeyException {
        Instant now = Instant.now();
        return code.equals(createTotp(key, now));
    }

    public Key getTotpKey() throws NoSuchAlgorithmException {
        return keyGenerator.generateKey();
    }

    public String encodeKeyToString(Key key) {
        return Base64.getEncoder().encodeToString(key.getEncoded());
    }
}
