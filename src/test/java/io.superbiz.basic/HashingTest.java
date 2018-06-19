package io.superbiz.basic;

import org.junit.Test;
import org.tomitribe.util.Hex;
import org.tomitribe.util.IO;
import org.tomitribe.util.Longs;
import org.tomitribe.util.hash.XxHash64;

import java.io.InputStream;
import java.util.Base64;

import static org.junit.Assert.assertEquals;

public class HashingTest {

    @Test
    public void hashing() throws Exception {

        final InputStream stream = this.getClass().getClassLoader().getResourceAsStream("bottles-of-beer.txt");
        final String content = IO.slurp(stream);
        final long hash = XxHash64.hash(content);

        assertEquals(-2147582013778192549l, hash);

        // Our hash is 4 bytes long (64 bits)
        final byte[] hashBytes = Longs.toBytes(hash);
        assertEquals(4, hashBytes.length);

        // Usually hashes are encoded

        // Convert hash to base64
        final String hashAsBase64 = Base64.getUrlEncoder().encodeToString(hashBytes);
        assertEquals("4jJBiW67l1s=", hashAsBase64);

        // Convert hash to hex
        final String hashAsHex = Hex.toString(Longs.toBytes(hash));
        assertEquals("e23241896ebb975b", hashAsHex);

    }
}
