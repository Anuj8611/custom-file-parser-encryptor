package format;

import java.nio.ByteBuffer;
import java.util.zip.CRC32;

public class Header {

    public static final int MAGIC = 0x58454E43; // "XENC"
    public static final byte VERSION = 1;

    public static final int HEADER_SIZE =
            4 + // MAGIC
            1 + // VERSION
            1 + // FLAGS
            4 + // DATA_LEN
            4;  // CHECKSUM

    public byte version;
    public byte flags;
    public int dataLength;
    public int checksum;

    public Header(byte flags, int dataLength, int checksum) {
        this.version = VERSION;
        this.flags = flags;
        this.dataLength = dataLength;
        this.checksum = checksum;
    }

    private Header() {}

    // Serialize header → bytes
    public byte[] toBytes() {
        ByteBuffer buffer = ByteBuffer.allocate(HEADER_SIZE);
        buffer.putInt(MAGIC);
        buffer.put(version);
        buffer.put(flags);
        buffer.putInt(dataLength);
        buffer.putInt(checksum);
        return buffer.array();
    }

    // Parse header ← bytes
    public static Header fromBytes(byte[] bytes) {
        if (bytes.length < HEADER_SIZE) {
            throw new IllegalArgumentException("Invalid header size");
        }

        ByteBuffer buffer = ByteBuffer.wrap(bytes);

        int magic = buffer.getInt();
        if (magic != MAGIC) {
            throw new IllegalArgumentException("Invalid magic header");
        }

        Header h = new Header();
        h.version = buffer.get();
        h.flags = buffer.get();
        h.dataLength = buffer.getInt();
        h.checksum = buffer.getInt();

        if (h.version != VERSION) {
            throw new IllegalArgumentException("Unsupported version");
        }

        return h;
    }

    // Utility: checksum
    public static int computeChecksum(byte[] data) {
        CRC32 crc = new CRC32();
        crc.update(data);
        return (int) crc.getValue();
    }
}
