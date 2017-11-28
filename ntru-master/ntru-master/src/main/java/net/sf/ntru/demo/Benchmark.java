/**
 * Copyright (c) 2011, Tim Buktu
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */

package net.sf.ntru.demo;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Security;
import java.security.spec.ECGenParameterSpec;
import java.text.DecimalFormat;

import javax.crypto.Cipher;
import javax.crypto.KeyAgreement;

import net.sf.ntru.encrypt.EncryptionKeyPair;
import net.sf.ntru.encrypt.EncryptionParameters;
import net.sf.ntru.encrypt.EncryptionPublicKey;
import net.sf.ntru.encrypt.NtruEncrypt;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import djb.Curve25519;

/**
 * Benchmarks NTRUEncrypt against ECC and RSA.
 */
public class Benchmark {
    private static final int PLAIN_TEXT_SIZE = 32;
    
    private static void printUsage() {
        System.out.println("Usage: Benchmark [alg] [alg]...");
        System.out.println();
        System.out.println("alg can be one of:");
        System.out.println("  rsa3072gen");
        System.out.println("  rsa3072enc");
        System.out.println("  rsa3072dec");
        System.out.println("  rsa15360gen");
        System.out.println("  rsa15360enc");
        System.out.println("  rsa15360dec");
        System.out.println("  curve25519gen");
        System.out.println("  curve25519enc");
        System.out.println("  curve25519dec");
        System.out.println("  ecc256gen");
        System.out.println("  ecc256enc");
        System.out.println("  ecc256dec");
        System.out.println("  ecc521gen");
        System.out.println("  ecc521enc");
        System.out.println("  ecc521dec");
        System.out.println("  ntru439gen");
        System.out.println("  ntru439enc");
        System.out.println("  ntru439dec");
        System.out.println("  ntru743gen");
        System.out.println("  ntru743enc");
        System.out.println("  ntru743dec");
        System.out.println("If alg is not specified, all algorithms except rsa15360* are benchmarked.");
    }
    
    public static void main(String[] args) throws Exception {
        Security.addProvider(new BouncyCastleProvider());
        
        if (args.length < 1) {
            rsa3072gen();
            rsa3072enc();
            rsa3072dec();
            curve25519gen();
            curve25519enc();
            curve25519dec();
            ecc256gen();
            ecc256enc();
            ecc256dec();
            ecc521gen();
            ecc521enc();
            ecc521dec();
            ntru439gen();
            ntru439enc();
            ntru439dec();
            ntru743gen();
            ntru743enc();
            ntru743dec();
        }
        else {
            boolean allArgsInvalid = true;
            
            for (String arg: args) {
                boolean argInvalid = false;
                
                if ("rsa3072gen".equals(arg))       rsa3072gen();
                else if ("rsa3072enc".equals(arg))  rsa3072enc();
                else if ("rsa3072dec".equals(arg))  rsa3072dec();
                else if ("rsa15360gen".equals(arg)) rsa15360gen();
                else if ("rsa15360enc".equals(arg)) rsa15360enc();
                else if ("rsa15360dec".equals(arg)) rsa15360dec();
                else if ("curve25519gen".equals(arg))  curve25519gen();
                else if ("curve25519enc".equals(arg))  curve25519enc();
                else if ("curve25519dec".equals(arg))  curve25519dec();
                else if ("ecc256gen".equals(arg))   ecc256gen();
                else if ("ecc256enc".equals(arg))   ecc256enc();
                else if ("ecc256dec".equals(arg))   ecc256dec();
                else if ("ecc521gen".equals(arg))   ecc521gen();
                else if ("ecc521enc".equals(arg))   ecc521enc();
                else if ("ecc521dec".equals(arg))   ecc521dec();
                else if ("ntru439gen".equals(arg))  ntru439gen();
                else if ("ntru439enc".equals(arg))  ntru439enc();
                else if ("ntru439dec".equals(arg))  ntru439dec();
                else if ("ntru743gen".equals(arg))  ntru743gen();
                else if ("ntru743enc".equals(arg))  ntru743enc();
                else if ("ntru743dec".equals(arg))  ntru743dec();
                else
                    argInvalid = true;
                
                allArgsInvalid &= argInvalid;
            }
            
            if (allArgsInvalid)
                printUsage();
        }
    }
    
    private static void rsa3072gen() throws Exception {
        new RsaBenchmark(3072, 1, 2).keyGenBench();
    }
    
    private static void rsa3072enc() throws Exception {
        new RsaBenchmark(3072, 1000, 2000).encryptBench();
    }
    
    private static void rsa3072dec() throws Exception {
        new RsaBenchmark(3072, 20, 40).decryptBench();
    }
    
    private static void rsa15360gen() throws Exception {
        new RsaBenchmark(15360, 1, 2).keyGenBench();
    }
    
    private static void rsa15360enc() throws Exception {
        new RsaBenchmark(15360, 100, 200).encryptBench();
    }
    
    private static void rsa15360dec() throws Exception {
        new RsaBenchmark(15360, 3, 6).decryptBench();
    }
    
    private static void curve25519gen() throws Exception {
        new Curve25519Benchmark(3000, 6000).keyGenBench();
    }
    
    private static void curve25519enc() throws Exception {
        new Curve25519Benchmark(1500, 3000).encryptBench();
    }
    
    private static void curve25519dec() throws Exception {
        new Curve25519Benchmark(3000, 6000).decryptBench();
    }
    
    private static void ecc256gen() throws Exception {
        new EcdhBenchmark("P-256", 256, 100, 200).keyGenBench();
    }
    
    private static void ecc256enc() throws Exception {
        new EcdhBenchmark("P-256", 256, 50, 100).encryptBench();
    }
    
    private static void ecc256dec() throws Exception {
        new EcdhBenchmark("P-256", 256, 100, 200).decryptBench();
    }
    
    private static void ecc521gen() throws Exception {
        new EcdhBenchmark("P-521", 521, 20, 40).keyGenBench();
    }
    
    private static void ecc521enc() throws Exception {
        new EcdhBenchmark("P-521", 521, 10, 20).encryptBench();
    }
    
    private static void ecc521dec() throws Exception {
        new EcdhBenchmark("P-521", 521, 20, 40).decryptBench();
    }
    
    private static void ntru439gen() throws Exception {
        new NtruEncryptBenchmark(EncryptionParameters.APR2011_439_FAST, 100, 200).keyGenBench();
    }
    
    private static void ntru439enc() throws Exception {
        new NtruEncryptBenchmark(EncryptionParameters.APR2011_439_FAST, 2000, 4000).encryptBench();
    }
    
    private static void ntru439dec() throws Exception {
        new NtruEncryptBenchmark(EncryptionParameters.APR2011_439_FAST, 4000, 8000).decryptBench();
    }
    
    private static void ntru743gen() throws Exception {
        new NtruEncryptBenchmark(EncryptionParameters.APR2011_743_FAST, 40, 80).keyGenBench();
    }
    
    private static void ntru743enc() throws Exception {
        new NtruEncryptBenchmark(EncryptionParameters.APR2011_743_FAST, 750, 1500).encryptBench();
    }
    
    private static void ntru743dec() throws Exception {
        new NtruEncryptBenchmark(EncryptionParameters.APR2011_743_FAST, 2000, 4000).decryptBench();
    }
    
    private static void printResults(String alg, long duration, int iterations) {
        DecimalFormat format = new DecimalFormat("0.00");
        System.out.println("-------------------------------------------------------------------------------");
        System.out.println("Result for " + alg + ": " + format.format(duration/1000000.0) + "ms total, " +
                format.format(duration/1000000.0/iterations) + "ms/op, " +
                format.format(iterations*1000000000.0/duration) + " ops/sec");
        System.out.println("-------------------------------------------------------------------------------");
    }
    
    private static byte[] generatePlainText() {
        SecureRandom rng = new SecureRandom();
        byte[] plainText = new byte[PLAIN_TEXT_SIZE];
        rng.nextBytes(plainText);
        return plainText;
    }
    
    private static class RsaBenchmark {
        private int keySize;
        private int warmupIterations;
        private int benchIterations;
        private byte[] plainText;
        private Cipher ciph;
        private KeyPairGenerator keyGen;
        
        private RsaBenchmark(int keySize, int warmupIterations, int benchIterations) throws Exception {
            this.keySize = keySize;
            this.warmupIterations = warmupIterations;
            this.benchIterations = benchIterations;
            
            plainText = generatePlainText();
            ciph = Cipher.getInstance("RSA/NONE/PKCS1Padding", "BC");
            keyGen = KeyPairGenerator.getInstance("RSA", "BC");
            keyGen.initialize(keySize);
        }
        
        private void keyGenBench() throws Exception {
            System.out.println("Warming up RSA...");
            rsaKeyGenIterations(warmupIterations, keyGen);
            System.out.println("Finished warming up RSA");
            System.out.println("Benchmarking RSA key generation...");
            long t1 = System.nanoTime();
            rsaKeyGenIterations(benchIterations, keyGen);
            long t2 = System.nanoTime();
            printResults("RSA-" + keySize + " key generation", t2-t1, benchIterations);
        }
        
        private void encryptBench() throws Exception {
            KeyPair kp = keyGen.generateKeyPair();
            
            System.out.println("Warming up RSA...");
            rsaEncryptIterations(warmupIterations, keyGen, kp.getPublic());
            System.out.println("Finished warming up RSA");
            System.out.println("Benchmarking RSA encryption...");
            long t1 = System.nanoTime();
            rsaEncryptIterations(benchIterations, keyGen, kp.getPublic());
            long t2 = System.nanoTime();
            printResults("RSA-" + keySize + " encryption", t2-t1, benchIterations);
        }
        
        private void decryptBench() throws Exception {
            KeyPair kp = keyGen.generateKeyPair();
            
            ciph.init(Cipher.ENCRYPT_MODE, kp.getPublic());
            byte[] encryptedText = ciph.doFinal(plainText);
            
            System.out.println("Warming up RSA...");
            ecdhDecryptIterations(warmupIterations, encryptedText, kp.getPrivate());
            System.out.println("Finished warming up RSA");
            System.out.println("Benchmarking RSA decryption...");
            long t1 = System.nanoTime();
            ecdhDecryptIterations(benchIterations, encryptedText, kp.getPrivate());
            long t2 = System.nanoTime();
            printResults("RSA-" + keySize + " decryption", t2-t1, benchIterations);
        }
        
        private void rsaKeyGenIterations(int iterations, KeyPairGenerator keyGen) throws Exception {
            for (int i=0; i<iterations; i++)
                keyGen.generateKeyPair();
        }
        
        private void rsaEncryptIterations(int iterations, KeyPairGenerator keyGen, PublicKey pk) throws Exception {
            for (int i=0; i<iterations; i++) {
                ciph.init(Cipher.ENCRYPT_MODE, pk);
                ciph.doFinal(plainText);
            }
        }
        
        private void ecdhDecryptIterations(int iterations, byte[] encryptedText, PrivateKey pk) throws Exception {
            for (int i=0; i<iterations; i++) {
                ciph.init(Cipher.DECRYPT_MODE, pk);
                ciph.doFinal(encryptedText);
            }
        }
    }

    private static class Curve25519Benchmark {
        private int warmupIterations;
        private int benchIterations;

        private Curve25519Benchmark(int warmupIterations, int benchIterations) throws Exception {
            this.warmupIterations = warmupIterations;
            this.benchIterations = benchIterations;
        }
        
        private void keyGenBench() throws Exception {
            System.out.println("Warming up curve25519...");
            curve25519KeyGenIterations(warmupIterations);
            System.out.println("Finished warming up curve25519");
            System.out.println("Benchmarking curve25519 key generation...");
            long t1 = System.nanoTime();
            curve25519KeyGenIterations(benchIterations);
            long t2 = System.nanoTime();
            printResults("curve25519 key generation", t2-t1, benchIterations);
        }
        
        private void curve25519KeyGenIterations(int iterations) throws Exception {
            byte[] pub = new byte[32];
            byte[] priv = new byte[32];
            new SecureRandom().nextBytes(priv);
            
            for (int i=0; i<iterations; i++)
                Curve25519.keygen(pub, null, priv);
        }
        
        public void encryptBench() throws Exception {
            byte[] pub = new byte[32];
            byte[] priv = new byte[32];
            new SecureRandom().nextBytes(priv);
            Curve25519.keygen(pub, null, priv);
            
            System.out.println("Warming up curve25519...");
            curve25519EncryptIterations(warmupIterations, pub);
            System.out.println("Finished warming up curve25519");
            System.out.println("Benchmarking curve25519 encryption...");
            long t1 = System.nanoTime();
            curve25519EncryptIterations(benchIterations, pub);
            long t2 = System.nanoTime();
            printResults("curve25519 encryption", t2-t1, benchIterations);
        }
        
        private void curve25519EncryptIterations(int iterations, byte[] pub) throws Exception {
            byte[] ephPub = new byte[32];
            byte[] ephPriv = new byte[32];
            new SecureRandom().nextBytes(ephPriv);
            byte[] sharedSecret = new byte[32];
            
            for (int i=0; i<iterations; i++) {
                // generate an ephemeral key and do a key agreement with pub
                Curve25519.keygen(ephPub, null, ephPriv);
                Curve25519.curve(sharedSecret, pub, ephPriv);
            }
        }
        
        public void decryptBench() throws Exception {
            byte[] pub = new byte[32];
            byte[] priv = new byte[32];
            new SecureRandom().nextBytes(priv);
            Curve25519.keygen(pub, null, priv);
            byte[] ephPub = new byte[32];
            byte[] ephPriv = new byte[32];
            new SecureRandom().nextBytes(ephPriv);
            Curve25519.keygen(ephPub, null, ephPriv);
            
            System.out.println("Warming up curve25519...");
            curve25519DecryptIterations(warmupIterations, priv, ephPub);
            System.out.println("Finished warming up curve25519");
            System.out.println("Benchmarking curve25519 decryption...");
            long t1 = System.nanoTime();
            curve25519DecryptIterations(benchIterations, priv, ephPub);
            long t2 = System.nanoTime();
            printResults("curve25519 decryption", t2-t1, benchIterations);
        }

        private void curve25519DecryptIterations(int iterations, byte[] priv, byte[] ephPub) throws Exception {
            byte[] sharedSecret = new byte[32];
            
            for (int i=0; i<iterations; i++)
                Curve25519.curve(sharedSecret, priv, ephPub);
        }
    }
    
    private static class EcdhBenchmark {
        private int keySize;
        private int warmupIterations;
        private int benchIterations;
        private KeyPairGenerator keyGen;
        
        private EcdhBenchmark(String curveName, int keySize, int warmupIterations, int benchIterations) throws Exception {
            this.keySize = keySize;
            this.warmupIterations = warmupIterations;
            this.benchIterations = benchIterations;
            keyGen = KeyPairGenerator.getInstance("ECDH", "BC");
            ECGenParameterSpec params = new ECGenParameterSpec(curveName);
            keyGen.initialize(params);
        }
        
        private void keyGenBench() throws Exception {
            System.out.println("Warming up ECDH...");
            ecdhKeyGenIterations(warmupIterations, keyGen);
            System.out.println("Finished warming up ECDH");
            System.out.println("Benchmarking ECDH key generation...");
            long t1 = System.nanoTime();
            ecdhKeyGenIterations(benchIterations, keyGen);
            long t2 = System.nanoTime();
            printResults("ECDH-" + keySize + " key generation", t2-t1, benchIterations);
        }
        
        private void encryptBench() throws Exception {
            KeyPair kp = keyGen.generateKeyPair();
            
            System.out.println("Warming up ECDH...");
            ecdhEncryptIterations(warmupIterations, keyGen, kp.getPublic());
            System.out.println("Finished warming up ECDH");
            System.out.println("Benchmarking ECDH encryption...");
            long t1 = System.nanoTime();
            ecdhEncryptIterations(benchIterations, keyGen, kp.getPublic());
            long t2 = System.nanoTime();
            printResults("ECDH-" + keySize + " encryption", t2-t1, benchIterations);
        }
        
        private void decryptBench() throws Exception {
            KeyPair kp = keyGen.generateKeyPair();
            KeyPair ephemKp = keyGen.generateKeyPair();
            
            System.out.println("Warming up ECDH...");
            ecdhDecryptIterations(warmupIterations, keyGen, kp.getPrivate(), ephemKp.getPublic());
            System.out.println("Finished warming up ECDH");
            System.out.println("Benchmarking ECDH decryption...");
            long t1 = System.nanoTime();
            ecdhDecryptIterations(benchIterations, keyGen, kp.getPrivate(), ephemKp.getPublic());
            long t2 = System.nanoTime();
            printResults("ECDH-" + keySize + " decryption", t2-t1, benchIterations);
        }
        
        private void ecdhKeyGenIterations(int iterations, KeyPairGenerator keyGen) throws Exception {
            for (int i=0; i<iterations; i++)
                keyGen.generateKeyPair();
        }
        
        private void ecdhEncryptIterations(int iterations, KeyPairGenerator keyGen, PublicKey pk) throws Exception {
            KeyAgreement ka = KeyAgreement.getInstance("ECDH");
            for (int i=0; i<iterations; i++) {
                KeyPair ephemKp = keyGen.generateKeyPair();
                ka.init(ephemKp.getPrivate());
                ka.doPhase(pk, true);
                ka.generateSecret();
            }
        }
        
        private void ecdhDecryptIterations(int iterations, KeyPairGenerator keyGen, PrivateKey pk, PublicKey ephemPk) throws Exception {
            KeyAgreement ka = KeyAgreement.getInstance("ECDH");
            for (int i=0; i<iterations; i++) {
                ka.init(pk);
                ka.doPhase(ephemPk, true);
                ka.generateSecret();
            }
        }
    }

    private static class NtruEncryptBenchmark {
        private EncryptionParameters params;
        private int warmupIterations;
        private int benchIterations;
        private NtruEncrypt ntru;
        
        private NtruEncryptBenchmark(EncryptionParameters params, int warmupIterations, int benchIterations) {
            this.params = params;
            this.warmupIterations = warmupIterations;
            this.benchIterations = benchIterations;
            ntru = new NtruEncrypt(params);
        }
        
        private void keyGenBench() {
            System.out.println("Warming up NTRU...");
            ntruKeyGenIterations(warmupIterations, ntru);
            System.out.println("Finished warming up NTRU");
            System.out.println("Benchmarking NTRU key generation...");
            long t1 = System.nanoTime();
            ntruKeyGenIterations(benchIterations, ntru);
            long t2 = System.nanoTime();
            printResults("NTRU-" + params.N + " key generation", t2-t1, benchIterations);
        }
        
        private void encryptBench() {
            byte[] plainText = generatePlainText();
            EncryptionKeyPair kp = ntru.generateKeyPair();
            System.out.println("Warming up NTRU...");
            ntruEncryptIterations(warmupIterations, plainText, ntru, kp.getPublic());
            System.out.println("Finished warming up NTRU");
            System.out.println("Benchmarking NTRU encryption...");
            long t1 = System.nanoTime();
            ntruEncryptIterations(benchIterations, plainText, ntru, kp.getPublic());
            long t2 = System.nanoTime();
            printResults("NTRU-" + params.N + " encryption", t2-t1, benchIterations);
        }
        
        private void decryptBench() {
            byte[] plainText = generatePlainText();
            EncryptionKeyPair kp = ntru.generateKeyPair();
            byte[] encryptedText = ntru.encrypt(plainText, kp.getPublic());
            System.out.println("Warming up NTRU...");
            ntruDecryptIterations(warmupIterations, encryptedText, ntru, kp);
            System.out.println("Finished warming up NTRU");
            System.out.println("Benchmarking NTRU decryption...");
            long t1 = System.nanoTime();
            ntruDecryptIterations(benchIterations, encryptedText, ntru, kp);
            long t2 = System.nanoTime();
            printResults("NTRU-" + params.N + " decryption", t2-t1, benchIterations);
        }
        
        private void ntruKeyGenIterations(int iterations, NtruEncrypt ntru) {
            for (int i=0; i<iterations; i++)
                ntru.generateKeyPair();
        }
        
        private void ntruEncryptIterations(int iterations, byte[] plainText, NtruEncrypt ntru, EncryptionPublicKey key) {
            for (int i=0; i<iterations; i++)
                ntru.encrypt(plainText, key);
        }
        
        private void ntruDecryptIterations(int iterations, byte[] encryptedText, NtruEncrypt ntru, EncryptionKeyPair kp) {
            for (int i=0; i<iterations; i++)
                ntru.decrypt(encryptedText, kp);
        }
    }
}