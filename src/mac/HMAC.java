package mac;

import hashfunctions.HashFunction;
import hashfunctions.Streebog;

import java.util.Arrays;

import static utils.Computing.XOR;
import static utils.Computing.join;

public class HMAC {

    private Streebog hashfunction;
    private int block_size;
    private byte[] key = "secret".getBytes();;
    private byte[] message;

    public HMAC(Streebog function, byte[] key, byte[] message){
        this.hashfunction = function;
        this.block_size = this.hashfunction.getBlockSize();
        this.key = key;
        this.message = message;
    }

    // Функция для вычисления HMAC
    public byte[] computeHMAC() {
        if (this.key.length > block_size) {
            this.key = this.hashfunction.get_hash(this.key); // Если ключ длиннее размера блока, сжимаем его
        } else if (this.key.length < block_size) {
            // Дополняем ключ нулями до размера блока
            this.key = Arrays.copyOf(this.key, block_size);
        }

        byte[] o_key_pad = XOR(this.key, new byte[block_size]); // Внешняя оболочка
        byte[] i_key_pad = XOR(this.key, new byte[block_size]); // Внутренняя оболочка

        for (int i = 0; i < block_size; i++) {
            o_key_pad[i] ^= 0x5c;
            i_key_pad[i] ^= 0x36;
        }

        // Внутренний хеш
        byte[] innerHash = this.hashfunction.get_hash(join(i_key_pad, this.message));
        // Внешний хеш
        return this.hashfunction.get_hash(join(o_key_pad, innerHash));
    }

}
