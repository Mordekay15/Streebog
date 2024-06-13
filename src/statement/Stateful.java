package statement;


public interface Stateful {
    State getState();


    void initialize(byte[] key);

    void seed(byte[] plainText);

    void finalize(byte[] cipherText);


    void addStep(String name, String id, byte[] before, byte[] additional);

    void addStep(String name, String id, byte[] before);

    void finalizeStep(byte[] after);

    void nextStep(String name, String id, byte[] before, byte[] additional);

    void nextStep(String name, String id, byte[] before);
}
