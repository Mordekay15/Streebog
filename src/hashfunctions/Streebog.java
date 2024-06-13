package hashfunctions;

import statement.StateManager;
import java.nio.ByteBuffer;
import java.util.Arrays;
import static utils.Computing.XOR;
import static utils.StreebogConstants.*;

public final class Streebog extends HashFunction{
    private static final int BLOCK_SIZE = 64;
    private byte[] buffer = new byte[BLOCK_SIZE];
    private byte[] hash = new byte[BLOCK_SIZE];
    private byte[] h = new byte[BLOCK_SIZE];
    private byte[] N = new byte[BLOCK_SIZE];
    private byte[] Sigma = new byte[BLOCK_SIZE];
    private byte[] v_0 = new byte[BLOCK_SIZE];
    private byte[] v_512 = new byte[BLOCK_SIZE];
    private int buf_size;

    public Streebog(int hashSize) { super(hashSize); }
    public Streebog(int hashSize, StateManager manager ) {super(hashSize, manager);}

    private void init() {
        if (hash_size == 256) {
            java.util.Arrays.fill(this.h, (byte) 0x01);
        } else {
            java.util.Arrays.fill(this.h, (byte) 0x00);
        }
        addStep("Init","init", this.h);
        finalizeStep(this.h);

        this.v_512[1] = 0x02;
    }

    private void add512(byte[] a, byte[] b, byte[] c) {
        int carry = 0;
        for (int i = 0; i < BLOCK_SIZE; i++) {
            int sum = (a[i] & 0xff) + (b[i] & 0xff) + carry;
            c[i] = (byte) (sum & 0xff);
            carry = sum >> 8;
        }
    }

    private void HashP(byte[] state) {
        if (state == null || state.length != BLOCK_SIZE) {
            throw new IllegalArgumentException("State array must not be null and must have a length of " + BLOCK_SIZE);
        }

        byte[] internal = new byte[BLOCK_SIZE];
        for (int i = BLOCK_SIZE - 1; i >= 0; i--) {
            internal[i] = state[Tau[i]];
        }
        System.arraycopy(internal, 0, state, 0, BLOCK_SIZE);
    }

    private void HashS(byte[] state) {
        if (state == null || state.length != BLOCK_SIZE) {
            throw new IllegalArgumentException("State array must not be null and must have a length of " + BLOCK_SIZE);
        }

        byte[] internal = new byte[BLOCK_SIZE];
        for (int i = BLOCK_SIZE - 1; i >= 0; i--) {
            internal[i] = (byte)Pi[state[i] & 0xFF]; // & 0xFF to treat byte as unsigned
        }
        System.arraycopy(internal, 0, state, 0, BLOCK_SIZE);
    }

    private void HashL(byte[] state) {
        if (state == null || state.length != BLOCK_SIZE) {
            throw new IllegalArgumentException("State array must not be null and must have a length of " + BLOCK_SIZE);
        }

        long[] internalIn = new long[8];
        long[] internalOut = new long[8];

        // Convert byte array to long array
        ByteBuffer buffer = ByteBuffer.wrap(state);
        for (int i = 0; i < internalIn.length; i++) {
            internalIn[i] = buffer.getLong();
        }

        //addStep("L_G", "g_l", internalIn);
        // Perform the transformation
        for (int i = 7; i >= 0; i--) {
            for (int j = 63; j >= 0; j--) {
                if (((internalIn[i] >>> j) & 1) != 0) {
                    internalOut[i] ^= A[63 - j];
                }
            }
        }

        // Convert back to byte array and copy to state
        ByteBuffer outBuffer = ByteBuffer.wrap(state);
        for (long val : internalOut) {
            outBuffer.putLong(val);
        }
    }


    private void getKey(byte[] K, int iteration) {
        XOR(K, C[iteration], K);
        HashS(K);
        HashP(K);
        HashL(K);
    }

    private void HashE(byte[] K, byte[] m, byte[] state) {
        XOR(m, K, state);

        for (int i = 0; i < 12; i++) {
            HashS(state);
            HashP(state);
            HashL(state);
            getKey(K, i);
            XOR(state, K, state);
        }
    }

    private void HashG(byte[] h, byte[] N, byte[] m) {
        byte[] K = new byte[BLOCK_SIZE];
        byte[] internal = new byte[BLOCK_SIZE];

        addStep("XOR", "", this.h, N);
        XOR(this.h, this.N, K);

        nextStep("S", "", K);
        HashS(K);

        nextStep("P", "", K);
        HashP(K);

        nextStep("L", "", K);
        HashL(K);

        nextStep("E", "", K, m);
        HashE(K, m, internal);

        nextStep("XOR_h", "", internal, this.h);
        XOR(internal, this.h, internal);

        nextStep("XOR_m", "", internal, m);
        XOR(internal, m, h);
        finalizeStep(this.h);
    }

    private void HashPadding() {
        byte[] internal = new byte[BLOCK_SIZE];

        if (this.buf_size < BLOCK_SIZE) {
            System.arraycopy(this.buffer, 0, internal, 0, this.buf_size);

            internal[this.buf_size] = (byte) 0x01;

            System.arraycopy(internal, 0, this.buffer, 0, BLOCK_SIZE);
        }
    }

    private void Stage2(byte[] data ) {

        addStep("G", "2", data);
        HashG(this.h, this.N, data);
        finalizeStep(this.h);

        addStep("512_1", "", this.N, this.v_512);
        add512(this.N, this.v_512, this.N);
        finalizeStep(this.N);

        addStep("512_2", "", this.Sigma, data);
        add512(this.Sigma, data, this.Sigma);
        finalizeStep(this.Sigma);
    }

    private void Stage3() {
        byte[] internal = new byte[BLOCK_SIZE];

        internal[1] = (byte) (((this.buf_size * 8) >> 8) & 0xff);
        internal[0] = (byte) ((this.buf_size * 8) & 0xff);

        HashPadding();

        addStep("G", "3", this.buffer);
        HashG(this.h, this.N, this.buffer);
        finalizeStep(this.h);

        addStep("512_1", "", this.N, internal);
        add512(this.N, internal, this.N);
        finalizeStep(this.N);

        addStep("512_2", "", this.Sigma, this.buffer);
        add512(this.Sigma, this.buffer, this.Sigma);
        finalizeStep(this.Sigma);

        addStep("G", "3", this.N);
        HashG(this.h, this.v_0, this.N);
        finalizeStep(this.h);

        addStep("G", "3", this.Sigma);
        HashG(this.h, this.v_0, this.Sigma);
        finalizeStep(this.h);

        System.arraycopy(this.h, 0, this.hash, 0, BLOCK_SIZE);

        finalize(this.hash);
    }

    // Methods to replace GOSTHashUpdate
    private void update(byte[] data, int len) {
        int chk_size;

        int offset = 0; // To keep track of the current position in the input data

        // Process complete blocks directly from input data when buffer is empty
        while ((len > 63) && (this.buf_size == 0)) {
            byte[] block = new byte[64];
            System.arraycopy(data, offset, block, 0, 64);
            Stage2(block);
            offset += 64;
            len -= 64;
        }

        // Buffer remaining data and process when full blocks are formed
        while (len > 0) {
            chk_size = 64 - this.buf_size;
            chk_size = Math.min(chk_size, len);
            System.arraycopy(data, offset, this.buffer, this.buf_size, chk_size);
            this.buf_size += chk_size;
            len -= chk_size;
            offset += chk_size;

            // Process the buffer if it forms a complete block
            if (this.buf_size == 64) {
                Stage2(this.buffer);
                this.buf_size = 0;
            }
        }
    }

    private void finalizeHash() {
        Stage3();
        this.buf_size = 0;
    }

    public int getBlockSize(){
        return BLOCK_SIZE;
    }


    @Override
    public byte[] get_hash(byte[] buf) {
        init();
        update(buf, buf.length);
        finalizeHash();
        if(hash_size == 256) {
            return Arrays.copyOfRange(this.hash, 32, 64);
        }
        else {
            return this.hash;
        }
    }
}
