package hashfunctions;

import statement.State;
import statement.StateManager;
import statement.Stateful;

import java.lang.reflect.InvocationTargetException;


public abstract class HashFunction implements Stateful {
    protected int hash_size;
    private StateManager manager;

    public HashFunction(int size) {
        this.hash_size = size;
    }
    public HashFunction(int size, StateManager manager) {
        this.hash_size = size;
        this.manager = manager;
    }

    public abstract byte[] get_hash(byte[] data);

    @Override
    public State getState() {
        return manager == null ? null : manager.getState();
    }

    @Override
    public void initialize(byte[] key) {
        //if (manager != null) manager.initialize(key);
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
