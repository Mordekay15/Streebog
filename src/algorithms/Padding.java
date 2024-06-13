package algorithms;

import java.util.Arrays;


public enum Padding {
    _OOOOOO, _8OOOOO, NO_3;
//    NO_1, NO_2, NO_1
    public byte[] pad(byte[] data, int blockSize) {
        int padSize = 0;

        if (data.length < blockSize) padSize = blockSize - data.length;
        else if (data.length % blockSize != 0) padSize = blockSize - data.length % blockSize;
        if (this == _8OOOOO) {
            padSize += blockSize;
        }

        byte[] result = new byte[data.length + padSize];
        System.arraycopy(data, 0, result, 0, data.length);

        if (this == _OOOOOO) {
            Arrays.fill(result, data.length, result.length, (byte) 0x0);
        } else {
            result[data.length] = (byte) 0x80;
            Arrays.fill(result, data.length + 1, result.length, (byte) 0x0);
        }

        return result;
    }

    public byte[] unpad(byte[] data) {
        int padIndex = data.length - 1;

        while (data[padIndex] == (byte) 0x0) {
            padIndex -= 1;
        }
//        if (this == _8OOOOO || this == NO_3) {
        if (this == _8OOOOO) {
            padIndex -= 1;
//            if (data[padIndex] != (byte) 0x80) {
//                throw new RuntimeException(String.format("Неожиданный байт %h в начале дополнения (padding) (%d)", data[padIndex], padIndex));
//            }
        }

        return Arrays.copyOfRange(data, 0, padIndex + 1);
    }
}
