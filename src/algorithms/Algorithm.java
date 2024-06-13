package algorithms;

import ciphers.Cipher;
import ciphers.CipherHolder;
import utils.Conversions;


public abstract class Algorithm {
    protected Cipher cipher;
    protected byte[] initialVector;
    protected Padding padding;

    protected int blocksTotal;
    protected int blocksProcessed;

    protected byte[] k;

    public Algorithm(CipherHolder holder, byte[] key, byte[] initialVector, Padding padding) {
        this.cipher = holder.instantiate(key);
        this.initialVector = initialVector;
        this.padding = padding;
        this.k = key;
    }


    public Cipher getCipher() {
        return cipher;
    }

    public byte[] getKey() {
        return k;
    }


    public int getBlocksTotal() {
        return blocksTotal;
    }

    public int getBlocksProcessed() {
        return blocksProcessed;
    }


    public abstract byte[] encrypt(byte[] data);

    public byte[] encryptString(String data) {
        return encrypt(Conversions.raw(data));
    }


    public abstract byte[] decrypt(byte[] data);

    public String decryptString(byte[] data) {
        return Conversions.raw(decrypt(data));
    }


    protected void rotateVector(byte[] vector, byte[] suffix, int blockSize) {
        System.arraycopy(vector, blockSize, vector, 0, vector.length - blockSize);
        System.arraycopy(suffix, 0, vector, vector.length - blockSize, blockSize);
    }
}
