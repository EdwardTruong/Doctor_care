package com.example.doctorcare.core.cqrs.utils;

import java.nio.ByteBuffer;
import java.security.SecureRandom;
import java.util.UUID;

/**
 * Lớp tiện ích để tạo UUID phiên bản 7 (UUIDv7) theo chuẩn RFC 9562.
 * <p>
 * UUIDv7 kết hợp một timestamp 48-bit (Unix-time milliseconds) với 74 bit dữ liệu ngẫu nhiên.
 * Đặc tính quan trọng nhất của nó là các UUID được tạo ra gần nhau về mặt thời gian cũng sẽ gần nhau
 * về mặt thứ tự sắp xếp (lexicographically).
 * <p>
 * Điều này làm cho UUIDv7 trở thành một lựa chọn tuyệt vời để làm khóa chính (primary key)
 * trong các hệ cơ sở dữ liệu quan hệ (RDBMS) như PostgreSQL, MySQL. Nó giúp giảm đáng kể
 * tình trạng phân mảnh chỉ mục (index fragmentation) và cải thiện hiệu năng ghi (INSERT)
 * so với UUIDv4 (hoàn toàn ngẫu nhiên).
 */
public final class UUIDv7 {
    
    private static final SecureRandom random = new SecureRandom();

    /**
     * Constructor riêng tư để ngăn việc khởi tạo lớp tiện ích.
     */
    private UUIDv7() {}
    
    /**
     * Tạo và trả về một chuỗi UUIDv7.
     *
     * @return Chuỗi biểu diễn của một UUIDv7 mới.
     */
    public static String asString() {
        return getUuid().toString();
    }

    /**
     * Tạo và trả về một đối tượng {@link UUID} phiên bản 7.
     *
     * @return Một đối tượng UUIDv7 mới.
     */
    public static UUID getUuid() {
        byte[] value = generateBytes();
        ByteBuffer buf = ByteBuffer.wrap(value);
        long high = buf.getLong();
        long low = buf.getLong();
        return new UUID(high, low);
    }

    /**
     * Tạo ra mảng 16 byte cho một UUIDv7.
     *
     * @return Mảng byte chứa dữ liệu của UUIDv7.
     */
    public static byte[] generateBytes() {
        // 1. Bắt đầu với 16 byte ngẫu nhiên
        byte[] value = new byte[16];
        random.nextBytes(value);

        // 2. Lấy timestamp hiện tại (số mili giây từ Unix epoch)
        long timestamp = System.currentTimeMillis();

        // 3. Ghi đè 6 byte đầu tiên (48 bit) bằng giá trị timestamp
        value[0] = (byte) ((timestamp >> 40) & 0xFF);
        value[1] = (byte) ((timestamp >> 32) & 0xFF);
        value[2] = (byte) ((timestamp >> 24) & 0xFF);
        value[3] = (byte) ((timestamp >> 16) & 0xFF);
        value[4] = (byte) ((timestamp >> 8) & 0xFF);
        value[5] = (byte) (timestamp & 0xFF);

        // 4. Thiết lập 4 bit phiên bản (version) là 7 (0b0111)
        value[6] = (byte) ((value[6] & 0x0F) | 0x70);
        // 5. Thiết lập 2 bit biến thể (variant) là 10 (theo RFC 4122)
        value[8] = (byte) ((value[8] & 0x3F) | 0x80);

        return value;
    }

}
