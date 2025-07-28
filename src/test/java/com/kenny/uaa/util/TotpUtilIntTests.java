package com.kenny.uaa.util;

import com.kenny.uaa.common.BaseIntegrationTest;
import lombok.val;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.security.Key;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

public class TotpUtilIntTests extends BaseIntegrationTest {
    @Autowired
    private TotpUtil totpUtil;


    @Test
    public void givenKey_whenEncodeAndDecode_thenSuccess() {
        Key key = totpUtil.generateKey();
        String strKey = totpUtil.encodeKeyToString(key);
        Key decodedKey = totpUtil.decodeKeyFromString(strKey);
        assertEquals(key, decodedKey, "The decoded key from string should be equal to the original key");
    }

    @Test
    public void givenSameKeyAndTotp_whenValidateTwice_thenFail() throws Exception {
        Instant now = Instant.now();
        Instant validFuture = now.plus(totpUtil.getTimeStep());
        Key key = totpUtil.generateKey();
        String first = totpUtil.createTotp(key, now);
        Key newKey = totpUtil.generateKey();
        assertTrue(totpUtil.verifyTotp(key, first), "First validation should succeed");
        String second = totpUtil.createTotp(key, Instant.now());
        assertEquals(first, second, "Two TOTPs generated within the same time window should be equal");
        String afterTimeStep = totpUtil.createTotp(key, validFuture);
        assertNotEquals(first, afterTimeStep, "TOTP after time step should not match the original TOTP");
        assertFalse(totpUtil.verifyTotp(newKey, first), "Validation with a new key should fail");
    }
}
