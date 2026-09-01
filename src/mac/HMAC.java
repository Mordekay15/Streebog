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

    // Function to compute HMAC
    public byte[] computeHMAC() {
        if (this.key.length > block_size) {
            this.key = this.hashfunction.get_hash(this.key); // If key is longer than block size, compress it
        } else if (this.key.length < block_size) {
            // Pad key with zeros to block size
            this.key = Arrays.copyOf(this.key, block_size);
        }

        byte[] o_key_pad = XOR(this.key, new byte[block_size]); // Outer wrapper
        byte[] i_key_pad = XOR(this.key, new byte[block_size]); // Inner wrapper

        for (int i = 0; i < block_size; i++) {
            o_key_pad[i] ^= 0x5c;
            i_key_pad[i] ^= 0x36;
        }

        // Inner hash
        byte[] innerHash = this.hashfunction.get_hash(join(i_key_pad, this.message));
        // Outer hash
        return this.hashfunction.get_hash(join(o_key_pad, innerHash));
    }

}
