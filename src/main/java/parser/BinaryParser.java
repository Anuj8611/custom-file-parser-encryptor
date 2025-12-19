package parser;

import format.Header;
import crypto.XOREncryptor;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class BinaryParser {

    // Encode: plain file → encrypted custom format
    public static void encode(String inputPath, String outputPath, String key) throws IOException {

        byte[] data = readAllBytes(inputPath);

        XOREncryptor encryptor = new XOREncryptor(key);
        byte[] encrypted = encryptor.apply(data);

        int checksum = Header.computeChecksum(encrypted);
        Header header = new Header((byte) 0, encrypted.length, checksum);

        try (FileOutputStream out = new FileOutputStream(outputPath)) {
            out.write(header.toBytes());
            out.write(encrypted);
        }
    }

    // Decode: encrypted custom format → original file
    public static void decode(String inputPath, String outputPath, String key) throws IOException {

        try (FileInputStream in = new FileInputStream(inputPath)) {

            byte[] headerBytes = new byte[Header.HEADER_SIZE];
            if (in.read(headerBytes) != Header.HEADER_SIZE) {
                throw new IOException("Failed to read header");
            }

            Header header = Header.fromBytes(headerBytes);

            byte[] encrypted = new byte[header.dataLength];
            if (in.read(encrypted) != header.dataLength) {
                throw new IOException("Corrupted file or invalid data length");
            }

            int computedChecksum = Header.computeChecksum(encrypted);
            if (computedChecksum != header.checksum) {
                throw new IOException("Checksum mismatch: data corrupted");
            }

            XOREncryptor encryptor = new XOREncryptor(key);
            byte[] decrypted = encryptor.apply(encrypted);

            try (FileOutputStream out = new FileOutputStream(outputPath)) {
                out.write(decrypted);
            }
        }
    }

    // Utility: read entire file
    private static byte[] readAllBytes(String path) throws IOException {
        try (FileInputStream in = new FileInputStream(path)) {
            return in.readAllBytes();
        }
    }
}
