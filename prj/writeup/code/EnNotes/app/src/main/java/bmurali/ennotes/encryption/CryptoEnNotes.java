/*
* This class uses java crypto ciphers to encrypt and decrypt content
* provided by the user. This uses a 256-bit AES algorithm along with the
* key set by the user.
* */
package bmurali.ennotes.encryption;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Base64;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import bmurali.ennotes.database.EnNotesUserDbHelper;

/*
This class provides Encryption and Decryption using a 256-bit AES algorithm in CBC mode.
It returns the encrypted data in Base64 and decrypted cipher text.
 */
public class CryptoEnNotes {

  public static String encrypt(Context context, String plainText) throws NoSuchAlgorithmException,
      NoSuchPaddingException, InvalidAlgorithmParameterException, InvalidKeyException,
      BadPaddingException, IllegalBlockSizeException, UnsupportedEncodingException {

    EnNotesUserDbHelper notesDbHelper = new EnNotesUserDbHelper(context);
    SQLiteDatabase userdb = notesDbHelper.getReadableDatabase();

    Cursor password_db = userdb.rawQuery("select content from UserKey", null);
    password_db.moveToFirst();

    final String key = password_db.getString(password_db.getColumnIndex("Content"));

    byte[] pText = plainText.getBytes();
    byte[] pKey = key.getBytes("UTF-8");

    int ivSize = 16;
    byte[] iv = new byte[ivSize];
    SecureRandom random = new SecureRandom();
    random.nextBytes(iv);
    IvParameterSpec ivSpec = new IvParameterSpec(iv);

    MessageDigest hash = MessageDigest.getInstance("SHA-256");
    hash.update(pKey);
    SecretKeySpec secretKey = new SecretKeySpec(hash.digest(), "AES");

    Cipher eCipher = Cipher.getInstance("AES_256/CBC/PKCS7Padding");
    eCipher.init(Cipher.ENCRYPT_MODE, secretKey, ivSpec);
    byte[] encrypted = eCipher.doFinal(pText);

    byte[] ivAndCipher = new byte[ivSize + encrypted.length];
    System.arraycopy(iv, 0, ivAndCipher, 0, ivSize);
    System.arraycopy(encrypted, 0, ivAndCipher, ivSize, encrypted.length);


    return Base64.encodeToString(ivAndCipher, Base64.DEFAULT);
  }

  public static String decrypt(Context context, String cipherText) throws Exception {

    EnNotesUserDbHelper notesUserDbHelper = new EnNotesUserDbHelper(context);
    SQLiteDatabase userDB = notesUserDbHelper.getReadableDatabase();

    Cursor password_db = userDB.rawQuery("select Content from UserKey", null);
    password_db.moveToFirst();

    final String key = password_db.getString(password_db.getColumnIndex("Content"));

    byte[] cText = Base64.decode(cipherText, Base64.DEFAULT);
    byte[] pKey = key.getBytes("UTF-8");

    int ivSize = 16;

    byte[] iv = new byte[ivSize];
    System.arraycopy(cText, 0, iv, 0, iv.length);
    IvParameterSpec ivSpec = new IvParameterSpec(iv);
    int cipherTextSize = cText.length - ivSize;
    byte[] cipher = new byte[cipherTextSize];
    System.arraycopy(cText, ivSize, cipher, 0, cipherTextSize);

    MessageDigest hash = MessageDigest.getInstance("SHA-256");
    hash.update(pKey);
    SecretKeySpec secretKey = new SecretKeySpec(hash.digest(), "AES");

    Cipher dCipher = Cipher.getInstance("AES_256/CBC/PKCS7Padding");
    dCipher.init(Cipher.DECRYPT_MODE, secretKey, ivSpec);
    byte[] decrypted = dCipher.doFinal(cipher);

    return new String(decrypted);
  }


}