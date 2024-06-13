package ciphers;


import statement.State;
import statement.StateManager;
import statement.Stateful;


public abstract class Cipher implements Stateful {
    protected byte[][] key;
    private StateManager manager;

    public Cipher(byte[] key) {
        this.key = expandKey(key);
    }

    public Cipher(byte[] key, StateManager manager) {
        this.manager = manager;
        this.key = expandKey(key);
        initialize(key);
    }


    protected abstract byte[][] expandKey(byte[] key);


    public abstract byte[] encrypt(byte[] data);

    public abstract byte[] decrypt(byte[] data);


    public abstract int getBlockSize();


    @Override
    public State getState() {
        return manager == null ? null : manager.getState();
    }


    @Override
    public void initialize(byte[] key) {
        if (manager != null) manager.initialize(key);
    }

    @Override
    public void seed(byte[] plainText) {
        if (manager != null) manager.seed(plainText);
    }

    @Override
    public void finalize(byte[] cipherText) {
        if (manager != null) manager.finalize(cipherText);
    }


    @Override
    public void addStep(String name, String id, byte[] before, byte[] additional) {
        if (manager != null) {
            manager.addStep(name, id, before, additional);
        }
    }

    @Override
    public void addStep(String name, String id, byte[] before) {
        if (manager != null) {
            manager.addStep(name, id, before);
        }
    }

    @Override
    public void finalizeStep(byte[] after) {
        if (manager != null) {
            manager.finalizeStep(after);
        }
    }

    @Override
    public void nextStep(String name, String id, byte[] before, byte[] additional) {
        if (manager != null) {
            manager.nextStep(name, id, before, additional);
        }
    }

    @Override
    public void nextStep(String name, String id, byte[] before) {
        if (manager != null) {
            manager.nextStep(name, id, before);
        }
    }
}
