package rfm.qd.gateway.xsocket.crypt.des;

import pub.platform.advance.utils.PropertyManager;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Formatter;

/**
 * Created by IntelliJ IDEA.
 * User: zhangxiaobo
 * Date: 11-8-29
 * Time: 上午9:54
 * To change this template use File | Settings | File Templates.
 */
public class DesCrypter {

    private static final String STR_DEFAULT_KEY = PropertyManager.getProperty("fdc.socket.data.crypt.key");

    private static Cipher cipher;
    private static Key key;

    private static DesCrypter instance;

    private DesCrypter() throws InvalidKeyException, NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException {
        KeySpec keySpec = new DESKeySpec(STR_DEFAULT_KEY.getBytes());
        key = SecretKeyFactory.getInstance("DES").generateSecret(keySpec);

    }

    public static DesCrypter getInstance() throws InvalidKeyException, InvalidKeySpecException, NoSuchAlgorithmException, NoSuchPaddingException {
        if (instance == null) {
            instance = new DesCrypter();
        }
        return instance;
    }

    /**
     * 加密
     *
     * @param strMing
     * @return 密文
     * @throws Exception
     */
    public String encrypt(String strMing) throws Exception {
        cipher = Cipher.getInstance("DES");
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] encryptedBytes = cipher.doFinal(strMing.getBytes());
        StringBuilder miContent = new StringBuilder();
        Formatter formatter = new Formatter(miContent);
        for (byte b : encryptedBytes) {
            formatter.format("%02x", b);
        }
        return miContent.toString();
    }

    /**
     * 解密
     *
     * @param encrypedStr
     * @return 明文
     * @throws Exception
     */
    public String decrypt(String encrypedStr) throws Exception {

        cipher = Cipher.getInstance("DES/ECB/NOPADDING");
        byte[] encryptionBytes = new byte[encrypedStr.length() >> 1];
        for (int i = 0; i < encryptionBytes.length; i++) {
            encryptionBytes[i] = (byte)
                    Integer.parseInt(encrypedStr.substring(2 * i, 2 * i + 2), 16);
        }
        cipher.init(Cipher.DECRYPT_MODE, key);
        byte[] decryptBytes = cipher.doFinal(encryptionBytes);
        return new String(decryptBytes);
    }


}
