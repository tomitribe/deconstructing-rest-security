package io.superbiz.basic;

import org.junit.Test;
import org.tomitribe.util.Hex;
import org.tomitribe.util.IO;
import org.tomitribe.util.Longs;
import org.tomitribe.util.hash.XxHash64;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.InputStream;
import java.util.Base64;

import static org.junit.Assert.assertEquals;

public class SymmetricDigitalSignatureTest {

    @Test
    public void signOrVerify() throws Exception {

        // Read the content
        final InputStream stream = this.getClass().getClassLoader().getResourceAsStream("bottles-of-beer.txt");
        final String content = IO.slurp(stream);

        // Load our Symmetric Key
        final SecretKeySpec symmetricKey = new SecretKeySpec("DevNexus".getBytes("UTF-8"), "HmacSHA256");

        // Sign the content
        final Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(symmetricKey);
        final byte[] signedBytes = mac.doFinal(content.getBytes());

        // Our signature is 32 bytes long (256 bits)
        assertEquals(32, signedBytes.length);

        // Usually signedBytes are encoded

        // Convert hash to base64
        final String asBase64 = Base64.getUrlEncoder().encodeToString(signedBytes);
        assertEquals("x9lOVNT5yb-IiFGPuOLmB2B_qOXlTQfnNXP7efrDw0U=", asBase64);

        // Convert hash to hex
        final String asHex = Hex.toString(signedBytes);
        assertEquals("c7d94e54d4f9c9bf8888518fb8e2e607607fa8e5e54d07e73573fb79fac3c345", asHex);

    }
}
