package hashfunctions;


import ciphers.Cipher;
import statement.StateManager;

import java.lang.reflect.InvocationTargetException;

public class HashFunctionHolder {
    private final Class<? extends HashFunction> hashFunctionClass;

    public HashFunctionHolder(Class<? extends HashFunction> hashFunction) {
        this.hashFunctionClass = hashFunction;
    }
    public String getName() {
        return hashFunctionClass.getSimpleName();
    }

    public Class<? extends HashFunction> getHolding() {
        return hashFunctionClass;
    }

    public HashFunction instantiate(int hash_size) {
        try {
            return hashFunctionClass.getDeclaredConstructor(int.class).newInstance(hash_size);
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException |
                 NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }
    public HashFunction instantiateWithState(int hash_size) {
        try {
            StateManager manager = new StateManager(getName(), getName().toLowerCase());
            return hashFunctionClass.getDeclaredConstructor(int.class, StateManager.class).newInstance(hash_size, manager);
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException |
                 NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }
}
