/* =====================================================================
 *
 * Copyright (c) 2011 David Blevins.  All rights reserved.
 *
 * =====================================================================
 */
package io.superbiz.sec;

import io.superbiz.util.Ascii85Coder;
import org.tomitribe.crest.api.Command;
import org.tomitribe.crest.api.Default;
import org.tomitribe.crest.api.Option;
import org.tomitribe.crest.api.StreamingOutput;
import org.tomitribe.util.Binary;
import org.tomitribe.util.Hex;
import org.tomitribe.util.IO;
import org.tomitribe.util.Ints;
import org.tomitribe.util.Longs;
import org.tomitribe.util.PrintString;
import org.tomitribe.util.hash.Slices;
import org.tomitribe.util.hash.XxHash32;
import org.tomitribe.util.hash.XxHash64;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Supplier;

public class Hash {

    private final Map<String, Hasher> hashes = new LinkedHashMap<>();

    public Hash() {
        hashes.put("XxHash32", this::xxHash32);
        hashes.put("XxHash64", this::xxHash64);
        hashes.put("MD5", this::md5);
        hashes.put("SHA-1", this::sha1);
        hashes.put("SHA-256", this::sha256);
        hashes.put("SHA-512", this::sha512);
    }

    @Command
    public StreamingOutput all(final File file, @Option("highlight") @Default("222") int highlight) {
        final Supplier<Map<Integer, String>> colors = new Maps(highlight);
        return stream -> {
            final PrintStream out = new PrintStream(stream);

            for (final Map.Entry<String, Hasher> entry : hashes.entrySet()) {
                try {
                    byte[] byts = entry.getValue().hash(file);
                    final String s = Hex.toString(byts);
                    if (s.length() == 128) {
                        final String s1 = s.substring(0, 64);
                        final String s2 = s.substring(64);
                        final Map<Integer, String> map = colors.get();
                        out.printf("%-10s  %s%n", entry.getKey(), color(s1, map));
                        out.printf("%-10s  %s%n%n", "", color(s2, map));
                    } else {
                        out.printf("%-10s  %s%n%n", entry.getKey(), color(s, colors.get()));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
    }

    @Command
    public StreamingOutput sha256(final File file, @Option("highlight") @Default("222") int highlight) {
        final Supplier<Map<Integer, String>> colors = new Maps(highlight);
        return stream -> {
            try {
                final PrintStream out = new PrintStream(stream);

                final Hasher hasher = hashes.get("SHA-256");

                byte[] byts = hasher.hash(file);

                print(colors, out, Binary.toString(byts), "Binary");
                print(colors, out, Hex.toString(byts), "Hex");
                print(colors, out, base64(byts), "Base64");
                print(colors, out, Ascii85Coder.encode(byts), "Base85");
            } catch (Exception e) {
                e.printStackTrace();
            }
        };
    }

    private String base64(byte[] byts) {
        final java.util.Base64.Encoder encoder = java.util.Base64.getUrlEncoder();
        return encoder.withoutPadding().encodeToString(byts);
    }

    private void print(Supplier<Map<Integer, String>> colors, PrintStream out, final String string, String label) {
        final String format = "%-10s %s%n";

        final Map<Integer, String> map = colors.get();

        final Match matcher = new Match("(.{1,64})");

        for (final String match : matcher.match(string)) {
            out.printf(format, label, color(match, map));
            label = "";
        }

        System.out.println();
    }

    public static class Maps implements Supplier<Map<Integer, String>> {
        private final Map<Integer, String> colors = new HashMap<>();

        public Maps(final int highlight) {
            String h = highlight + "";
            if (highlight < 100) h = "0" + h;
            if (highlight < 10) h = "0" + h;

            for (int i = 0; i < 254; i++) {
                colors.put(i, "098");
            }

            for (int i = 65; i < 90; i++) {
                colors.put(i, "067");
            }
            for (int i = 97; i < 122; i++) {
                colors.put(i, "067");
            }

            for (final int i : new int[]{48}) {
                colors.put(i, "229");
            }

//            for (final int i : new int[]{49, 50, 51, 52, 53, 54, 55, 56, 57,}) {
//                colors.put(i, h);
//            }
        }

        @Override
        public Map<Integer, String> get() {
            return colors;
        }
    }

    private String color(String s, final Map<Integer, String> colors) {

        final PrintString out = new PrintString();

        for (final byte b : s.getBytes()) {
            final int i = b % colors.size();
            out.format("\033[38;5;%sm%s\033[0m", colors.get(i), (char) b);
        }

        return out.toString();
    }

    public interface Hasher {
        byte[] hash(final File file) throws Exception;
    }

    private byte[] xxHash64(File file) throws IOException {
        final byte[] bytes = IO.readBytes(file);
        final long hash = XxHash64.hash(Slices.wrappedBuffer(bytes));
        return Longs.toBytes(hash);
    }

    private byte[] xxHash32(File file) throws IOException {
        final byte[] bytes = IO.readBytes(file);
        final int hash = XxHash32.hash(Slices.wrappedBuffer(bytes));
        return Ints.toBytes(hash);
    }

    private byte[] md5(File file) throws Exception {
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(IO.readBytes(file));
        return md.digest();
    }

    private byte[] sha1(File file) throws Exception {
        MessageDigest md = MessageDigest.getInstance("SHA1");
        md.update(IO.readBytes(file));
        return md.digest();
    }

    private byte[] sha256(File file) throws Exception {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(IO.readBytes(file));
        return md.digest();
    }

    private byte[] sha512(File file) throws Exception {
        MessageDigest md = MessageDigest.getInstance("SHA-512");
        md.update(IO.readBytes(file));
        return md.digest();
    }

}
