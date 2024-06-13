package algorithms;

import ciphers.Cipher;
import ciphers.CipherHolder;

import java.lang.reflect.InvocationTargetException;


public final class AlgorithmHolder {
    private final Class<? extends Algorithm> algorithmClass;
    private final CipherHolder cipherHolder;

    public AlgorithmHolder(Class<? extends Algorithm> algorithm, CipherHolder cipherHolder) {
        this.algorithmClass = algorithm;
        this.cipherHolder = cipherHolder;
    }

    public AlgorithmHolder(Class<? extends Algorithm> algorithm, Class<? extends Cipher> cipher) {
        this(algorithm, new CipherHolder(cipher));
    }


    public String getName() {
        return String.format("%s<%s>", algorithmClass.getSimpleName(), cipherHolder.getName());
    }

    public Class<? extends Algorithm> getHolding() {
        return algorithmClass;
    }

    public CipherHolder getCipherHolder() {
        return cipherHolder;
    }


    @SuppressWarnings("unchecked")
    private <T> T getStaticOrNull(String fieldName) {
        try {
            return (T) algorithmClass.getDeclaredField(fieldName).get(null);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            return null;
        }
    }

    public Padding getDefaultPadding() {
        return getStaticOrNull("defaultPadding");
    }

    public byte[] getDefaultInitialVector() {
        return getStaticOrNull(String.format("defaultIV%s", cipherHolder.getName()));
    }


    public Algorithm instantiate(byte[] key, byte[] initialVector, Padding padding) {
        if (initialVector == null) {
            initialVector = getDefaultInitialVector();
        }
        if (padding == null) {
            padding = getDefaultPadding();
        }
        try {
            return algorithmClass.getDeclaredConstructor(CipherHolder.class, byte[].class, byte[].class, Padding.class).newInstance(cipherHolder, key, initialVector, padding);
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException |
                 NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    public Algorithm instantiate(byte[] key, byte[] initialVector) {
        return instantiate(key, initialVector, null);
    }

    public Algorithm instantiate(byte[] key, Padding padding) {
        return instantiate(key, null, padding);
    }

    public Algorithm instantiate(byte[] key) {
        return instantiate(key, null, null);
    }
}
