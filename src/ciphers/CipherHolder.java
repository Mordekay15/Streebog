package ciphers;

import statement.StateManager;

import java.lang.reflect.InvocationTargetException;


public final class CipherHolder {
    private final Class<? extends Cipher> cipherClass;

    public CipherHolder(Class<? extends Cipher> cipher) {
        this.cipherClass = cipher;
    }


    public String getName() {
        return cipherClass.getSimpleName();
    }

    public Class<? extends Cipher> getHolding() {
        return cipherClass;
    }

    public int getKeySize() {
        try {
            return (int) cipherClass.getField("KEY_SIZE").get(null);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }

    public int getBlockSize() {
        try {
            return (int) cipherClass.getField("BLOCK_SIZE").get(null);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }


    public Cipher instantiate(byte[] key) {
        try {
            return cipherClass.getDeclaredConstructor(byte[].class).newInstance(key);
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException |
                 NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    public Cipher instantiateWithState(byte[] key) {
        try {
            StateManager manager = new StateManager(getName(), getName().toLowerCase());
            return cipherClass.getDeclaredConstructor(byte[].class, StateManager.class).newInstance(key, manager);
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException |
                 NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }
}
