package com.linchtech.boot.starter.utils;

import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.RSA;
import lombok.extern.slf4j.Slf4j;

import java.util.Base64;

@Slf4j
public class RSAUtils {

    public static final String PRIVATE_KEY = "MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBAI0De565asuzAbKvcw1wyCyjfIdKz3IShsuwcDiBSbfOUqCOLlS2M/IC+msH30DC+RIhXoJWw7gfLeiYLBAhIvVaAVKBW08ipYVAnIn9pLK1IAZc+x7kJTf49t6/WyGJfVvKDNqN2LW63pcT9FST9qrvTXWOCqpVGD2DJKy51JULAgMBAAECgYBOmjlnQW2hPBffNWNJg6thDoRUmcPGj0Mp+SCkrpSb61stefOjbqjH5xcVYB7253eUvpsCxkB0upSJiYFOQFpOV8EniGCCrHDA5wdl6dK1Mn8brkHYy1EEkwJwewOvaJIEF1U8hJinFvuuI2AcwLf/OU2EITsx/IA2E/2r05Yu0QJBAORpabnc3+XstSNNgZiYrrmhhuVGsICKElKYPPDsqDrUZN7bBArtuq/Z3+Pocr+k7bC9UoQP/VD0N9h2Eyvf7XkCQQCeC66pCbTEthWjzmm7E6IBctX2LxZieNvi2nn9/7wGtyvuDTIKbriyPSe170aPVjez4a0NIy3ZRd9caEHg+CmjAkAkRHCbKG0Mrwcb8eSP/D5SD0nCAfVok4PRRYb3ojwcCzlxAUmlCpZH8cpQ/8mokGGFXKpLedZ5Ei4D2su0fbvRAkAHgnKYIZfvSN+aWvJ3KyWlf/leqzobD7cS/mKRiRc8EnQJ6aKZEE3CWlmKsYXexa/74htA5o0FJ+bHE0UeFWyVAkApNaqsFPhmaZL7Fk3oRO/rqfSUgevWT1HYR/IZwqeKQJuoBs3dR3Mg5mfCGkgy7UpgOCjDZ9mYUF8//3eMmGcw";
    public static final String PUBLIC_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCNA3ueuWrLswGyr3MNcMgso3yHSs9yEobLsHA4gUm3zlKgji5UtjPyAvprB99AwvkSIV6CVsO4Hy3omCwQISL1WgFSgVtPIqWFQJyJ/aSytSAGXPse5CU3+Pbev1shiX1bygzajdi1ut6XE/RUk/aq7011jgqqVRg9gySsudSVCwIDAQAB";

    /**
     * 获取公钥私钥密钥对
     */
    static String getKeyPair(RSA rsa) {
        StringBuilder rtnStb = new StringBuilder();
        //获得私钥
        //System.out.println(rsa.getPrivateKey());
        rtnStb.append("privateKey: ");
        rtnStb.append(rsa.getPrivateKeyBase64());
        rtnStb.append("\n");
        //获得公钥
        //System.out.println(rsa.getPublicKey());
        rtnStb.append("publicKey: ");
        rtnStb.append(rsa.getPublicKeyBase64());
        return rtnStb.toString();
    }

    /**
     * 加密
     * @param str 需要加密的内容
     * @param rsa rsa对象
     * @return 密文
     */
    private static String getEncryptString(String str, RSA rsa) {
        byte[] encrypt = rsa.encrypt(StrUtil.bytes(str, CharsetUtil.CHARSET_UTF_8), KeyType.PublicKey);
        return Base64.getEncoder().encodeToString(encrypt);
    }

    /**
     * 解密
     * @param str 加密的密文
     * @param rsa rsa对象
     * @return 原文
     */
    private static String getDecryptString(String str, RSA rsa) {
        byte[] aByte = Base64.getDecoder().decode(str);
        byte[] decrypt = rsa.decrypt(aByte, KeyType.PrivateKey);
        return new String(decrypt, CharsetUtil.CHARSET_UTF_8);
    }

    /**
     * 解密指定key的密文
     * @param str 密文
     * @return 原文
     */
    public static String decrypt(String str) {
        RSA rsa = new RSA(PRIVATE_KEY, PUBLIC_KEY);
        return getDecryptString(str, rsa);
    }

    public static void main(String[] args) {
        // RSA rsa1 = new RSA();
        // System.out.println("private key:" + rsa1.getPrivateKeyBase64());
        // System.out.println("public key:" + rsa1.getPublicKeyBase64());
        RSA rsa = new RSA(PRIVATE_KEY, PUBLIC_KEY);
        String content = "123456";
        String encryptTxt = getEncryptString(content, rsa);

        // String aesEncrypt = PasswordUtils.aesEncrypt(encryptTxt);
        System.out.println("加密后:"+ encryptTxt);
        String decryptTxt = getDecryptString(encryptTxt, rsa);
        System.out.println(decryptTxt);
    }
}