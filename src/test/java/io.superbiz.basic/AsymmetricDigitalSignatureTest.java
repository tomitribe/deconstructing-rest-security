package io.superbiz.basic;

import org.junit.Test;
import org.tomitribe.auth.signatures.PEM;
import org.tomitribe.util.Hex;
import org.tomitribe.util.IO;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.util.Base64;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class AsymmetricDigitalSignatureTest {

    private final String privateKeyPem = "-----BEGIN RSA PRIVATE KEY-----\n" +
            "MIICXgIBAAKBgQDCFENGw33yGihy92pDjZQhl0C36rPJj+CvfSC8+q28hxA161QF\n" +
            "NUd13wuCTUcq0Qd2qsBe/2hFyc2DCJJg0h1L78+6Z4UMR7EOcpfdUE9Hf3m/hs+F\n" +
            "UR45uBJeDK1HSFHD8bHKD6kv8FPGfJTotc+2xjJwoYi+1hqp1fIekaxsyQIDAQAB\n" +
            "AoGBAJR8ZkCUvx5kzv+utdl7T5MnordT1TvoXXJGXK7ZZ+UuvMNUCdN2QPc4sBiA\n" +
            "QWvLw1cSKt5DsKZ8UETpYPy8pPYnnDEz2dDYiaew9+xEpubyeW2oH4Zx71wqBtOK\n" +
            "kqwrXa/pzdpiucRRjk6vE6YY7EBBs/g7uanVpGibOVAEsqH1AkEA7DkjVH28WDUg\n" +
            "f1nqvfn2Kj6CT7nIcE3jGJsZZ7zlZmBmHFDONMLUrXR/Zm3pR5m0tCmBqa5RK95u\n" +
            "412jt1dPIwJBANJT3v8pnkth48bQo/fKel6uEYyboRtA5/uHuHkZ6FQF7OUkGogc\n" +
            "mSJluOdc5t6hI1VsLn0QZEjQZMEOWr+wKSMCQQCC4kXJEsHAve77oP6HtG/IiEn7\n" +
            "kpyUXRNvFsDE0czpJJBvL/aRFUJxuRK91jhjC68sA7NsKMGg5OXb5I5Jj36xAkEA\n" +
            "gIT7aFOYBFwGgQAQkWNKLvySgKbAZRTeLBacpHMuQdl1DfdntvAyqpAZ0lY0RKmW\n" +
            "G6aFKaqQfOXKCyWoUiVknQJAXrlgySFci/2ueKlIE1QqIiLSZ8V8OlpFLRnb1pzI\n" +
            "7U1yQXnTAEFYM560yJlzUpOb1V4cScGd365tiSMvxLOvTA==\n" +
            "-----END RSA PRIVATE KEY-----\n";

    private final String publicKeyPem = "-----BEGIN PUBLIC KEY-----\n" +
            "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDCFENGw33yGihy92pDjZQhl0C3\n" +
            "6rPJj+CvfSC8+q28hxA161QFNUd13wuCTUcq0Qd2qsBe/2hFyc2DCJJg0h1L78+6\n" +
            "Z4UMR7EOcpfdUE9Hf3m/hs+FUR45uBJeDK1HSFHD8bHKD6kv8FPGfJTotc+2xjJw\n" +
            "oYi+1hqp1fIekaxsyQIDAQAB\n" +
            "-----END PUBLIC KEY-----\n";

    @Test
    public void sign() throws Exception {

        // Read the content
        final InputStream stream = this.getClass().getClassLoader().getResourceAsStream("bottles-of-beer.txt");
        final String content = IO.slurp(stream);

        // Load our Private Key
        final PrivateKey privateKey = PEM.readPrivateKey(new ByteArrayInputStream(privateKeyPem.getBytes()));

        // Sign the content
        final Signature signer = Signature.getInstance("SHA256withRSA");
        signer.initSign(privateKey);
        signer.update(content.getBytes());

        // The magic line
        final byte[] signedBytes = signer.sign();

        // Our signature is 128 bytes long (1024 bits)
        assertEquals(128, signedBytes.length);

        // Usually signedBytes are encoded

        // Convert hash to base64
        final String asBase64 = Base64.getUrlEncoder().encodeToString(signedBytes);
        assertEquals("s4C1-87Fah4lCHKFtcOvnzeGXLeSFahS3Y4hQWpm8y" +
                "RiWl4tWi89DNnq6G0D6wDPD5UztACpiF3S2pb2GI7LCotFM" +
                "o6ZEhAzAbEd4nRtFt8eLCBdZl2gBXM--32S8M52-kRe7w7Y" +
                "BVsoFBFNpfcvXmwwHG9shK81xVlQzFIpEG4=", asBase64);

        // Convert hash to hex
        final String asHex = Hex.toString(signedBytes);
        assertEquals("b380b5fbcec56a1e25087285b5c3af9f37865cb792" +
                "15a852dd8e21416a66f324625a5e2d5a2f3d0cd9eae86d0" +
                "3eb00cf0f9533b400a9885dd2da96f6188ecb0a8b45328e" +
                "9912103301b11de2746d16df1e2c205d665da005733efb7" +
                "d92f0ce76fa445eef0ed8055b2814114da5f72f5e6c301c" +
                "6f6c84af35c55950cc5229106e", asHex);

    }

    @Test
    public void verify() throws Exception {

        // Read the content
        final InputStream stream = this.getClass().getClassLoader().getResourceAsStream("bottles-of-beer.txt");
        final String content = IO.slurp(stream);

        // Load our Private Key
        final PublicKey publicKey = PEM.readPublicKey(new ByteArrayInputStream(publicKeyPem.getBytes()));

        // Sign the content
        final Signature signer = Signature.getInstance("SHA256withRSA");
        signer.initVerify(publicKey);
        signer.update(content.getBytes());

        // Usually signedBytes are encoded

        // Base64 -> bytes
        final byte[] signedBytes = Base64.getUrlDecoder().decode("" +
                "s4C1-87Fah4lCHKFtcOvnzeGXLeSFahS3Y4hQWpm8y" +
                "RiWl4tWi89DNnq6G0D6wDPD5UztACpiF3S2pb2GI7LCotFM" +
                "o6ZEhAzAbEd4nRtFt8eLCBdZl2gBXM--32S8M52-kRe7w7Y" +
                "BVsoFBFNpfcvXmwwHG9shK81xVlQzFIpEG4=");

        // Our signature is 128 bytes long (1024 bits)
        assertEquals(128, signedBytes.length);

        final boolean isValid = signer.verify(signedBytes);
        assertTrue(isValid);
    }
}
