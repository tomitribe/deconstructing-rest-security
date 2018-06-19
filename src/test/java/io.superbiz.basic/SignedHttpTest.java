/* =====================================================================
 *
 * Copyright (c) 2011 David Blevins.  All rights reserved.
 *
 * =====================================================================
 */
package io.superbiz.basic;

import org.junit.Assert;
import org.junit.Test;
import org.tomitribe.auth.signatures.Signature;
import org.tomitribe.auth.signatures.Signer;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.HashMap;
import java.util.Map;

public class SignedHttpTest extends Assert {

    @Test
    public void sign() throws Exception {

        final Signature signature = new Signature("key-alias", "hmac-sha256", null, "(request-target)",  "digest", "date");
        final Key key = new SecretKeySpec("DevNexus".getBytes(), "HmacSHA256");
        final Signer signer = new Signer(key, signature);


        final String method = "GET";

        final String uri = "/foo/Bar";

        final Map<String, String> headers = new HashMap<String, String>();
        headers.put("Host", "example.org");
        headers.put("Date", "Thu, 23 Feb 2017 12:51:35 GMT");
        headers.put("Content-Type", "application/json");
        headers.put("Digest", "SHA-256=X48E9qOokqqrvdts8nOJRJN3OWDUoyWxBf7kbu9DBPE=");
        headers.put("Accept", "*/*");
        headers.put("Content-Length", "18");

        // Here it is!
        final Signature signed = signer.sign(method, uri, headers);

        // Plain Signature
        assertEquals("sL3aRpsfjeugX3ZKo97YiKDPxUcW04GYZjhybVtWfk8=", signed.getSignature());

        // Authorization: Header
        assertEquals("Signature " +
                "keyId=\"key-alias\"," +
                "algorithm=\"hmac-sha256\"," +
                "headers=\"(request-target) digest date\"," +
                "signature=\"sL3aRpsfjeugX3ZKo97YiKDPxUcW04GYZjhybVtWfk8=\"",
                signed.toString()
        );
    }
}
