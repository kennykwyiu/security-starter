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
}
