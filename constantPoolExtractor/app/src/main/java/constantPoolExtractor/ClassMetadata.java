package constantPoolExtractor;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ClassMetadata {

    private static final Map<Integer, String> tagMapper = new HashMap<>();
    private static final int tagLength;

    static {
        tagMapper.put(1, "Utf-8");
        tagMapper.put(3, "Integer");
        tagMapper.put(4, "Float");
        tagMapper.put(5, "Long");
        tagMapper.put(6, "Double");
        tagMapper.put(7, "Class");
        tagMapper.put(8, "String");
        tagMapper.put(9, "Field");
        tagMapper.put(10, "MethodReference");
        tagMapper.put(11, "Interface");
        tagMapper.put(12, "NameAndType");
        tagMapper.put(15, "MethodHandle");
        tagMapper.put(16, "MethodType");
        tagMapper.put(17, "Dynamic");
        tagMapper.put(18, "InvokeDynamic");
        tagMapper.put(19, "ModuleInfo");
        tagMapper.put(20, "PackageInfo");
        tagLength = tagMapper.values().stream().map(String::length).reduce(Integer::max).orElse(0);
    }

    private int magic;
    private short minorVersion;
    private short majorVersion;
    private ConstantPoolItem[] constantPoolItems;

    public ClassMetadata(byte[] bytes) {
        ByteBuffer buffer = ByteBuffer.wrap(bytes);
        magic = buffer.getInt();
        minorVersion = buffer.getShort();
        majorVersion = buffer.getShort();
        int poolSize = buffer.getShort() - 1;
        constantPoolItems = new ConstantPoolItem[poolSize];
        for (int i = 0; i < poolSize; i++) {
            constantPoolItems[i] = new ConstantPoolItem(buffer.get(), buffer);
        }
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Magic number = ").append(Integer.toHexString(magic)).append("\n");
        builder.append("Minor version = ").append(minorVersion & 0xffff).append("\n");
        builder.append("Major version = ").append(majorVersion & 0xffff).append("\n");
        for (int i = 0; i < constantPoolItems.length; i++) {
            builder.append("#").append(i + 1).append("\t").append(constantPoolItems[i].toString()).append("\n");
        }
        return builder.toString();
    }

    public class ConstantPoolItem {
        private byte tag;
        private Object obj;
        private Integer link1;
        private Integer link2;

        ConstantPoolItem(byte tag, ByteBuffer buffer) {
            this.tag = tag;
            switch (tag) {
                case 1:// UTF-8
                    byte[] str = new byte[buffer.getShort()];
                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < str.length; i++) {
                        str[i] = buffer.get();
                    }
                    obj = new String(str);
                    break;
                case 3:// Int
                    obj = buffer.getInt();
                    break;
                case 4:// Float
                    obj = buffer.getFloat();
                    break;
                case 5:// Long
                    obj = buffer.getLong();
                    break;
                case 6:// Double
                    obj = buffer.getDouble();
                    break;
                case 7: // Class
                case 8: // String
                case 16: // Method type
                case 19: // Module info
                case 20: // Package info
                    link1 = buffer.getShort() & 0xffff;
                    break;
                case 9: // Field reference
                case 10: // Method reference
                case 11: // Interface
                case 12: // Name and type
                case 17: // Dynamic
                case 18: // InvokeDynamic
                    link1 = buffer.getShort() & 0xffff;
                    link2 = buffer.getShort() & 0xffff;
                    break;
                case 15: // Method handle
                    obj = buffer.get() & 0xff;
                    link1 = buffer.getShort() & 0xffff;
                    break;
                default:
                    throw new IllegalArgumentException("Invalid constant pool item tag: " + tag);
            }
        }

        @Override
        public String toString() {
            return this.toString(0);
        }

        public String toString(int offset) {
            StringBuilder sb = new StringBuilder();
            sb.append(" ".repeat(Math.max(0, offset)));
            sb.append(padRight(tagMapper.get((int) tag), tagLength)).append(" ");
            if (obj != null) {
                sb.append(obj).append(" ");
            }
            if (link1 != null) {
                sb.append("#").append(link1);
            }
            if (link2 != null) {
                sb.append(".#").append(link2);
            }
            String suffix1 = link1 == null ? "" : "\n" + constantPoolItems[link1 - 1].toString(offset + 4);
            String suffix2 = link2 == null ? "" : "\n" + constantPoolItems[link2 - 1].toString(offset + 4);
            return sb.toString() + suffix1 + suffix2;
        }

        private String padRight(String s, int n) {
            return String.format("%-" + n + "s", s);
        }
    }
}

