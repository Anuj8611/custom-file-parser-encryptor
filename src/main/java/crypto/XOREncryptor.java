package crypto;

public class XOREncryptor {

    private final byte[] key;

    public XOREncryptor(String key) {
        if (key == null || key.isEmpty()) {
            throw new IllegalArgumentException("Encryption key cannot be empty");
        }
        this.key = key.getBytes();
    }

    // Encrypt or decrypt (XOR is symmetric)
    public byte[] apply(byte[] data) {
        byte[] result = new byte[data.length];

        for (int i = 0; i < data.length; i++) {
            result[i] = (byte) (data[i] ^ key[i % key.length]);
        }

        return result;
    }
}
