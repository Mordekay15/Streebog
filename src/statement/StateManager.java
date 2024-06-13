package statement;

import java.util.Arrays;


public class StateManager implements Stateful {
    private final State rootState;
    private State currentState;

    public StateManager(String baseName, String id) {
        this.rootState = new State(baseName, id, null);
        this.currentState = rootState;
    }

    @Override
    public State getState() {
        return rootState;
    }


    @Override
    public void initialize(byte[] key) {
        rootState.additionalValue = key == null ? null : Arrays.copyOf(key, key.length);
    }

    @Override
    public void seed(byte[] plainText) {
        rootState.beforeValue = Arrays.copyOf(plainText, plainText.length);
    }

    @Override
    public void finalize(byte[] cipherText) {
        rootState.afterValue = Arrays.copyOf(cipherText, cipherText.length);
    }


    @Override
    public void addStep(String name, String id, byte[] before, byte[] additional) {
        State step = new State(name, id, currentState);
        step.beforeValue = Arrays.copyOf(before, before.length);
        step.additionalValue = additional == null ? null : Arrays.copyOf(additional, additional.length);
        currentState.innerStates.add(step);
        currentState = step;
    }

    @Override
    public void addStep(String name, String id, byte[] before) {
        addStep(name, id, before, null);
    }

    @Override
    public void finalizeStep(byte[] after) {
        currentState.afterValue = Arrays.copyOf(after, after.length);
        if (currentState != rootState) {
            currentState = currentState.parent;
        }
    }

    @Override
    public void nextStep(String name, String id, byte[] before, byte[] additional) {
        finalizeStep(before);
        addStep(name, id, before, additional);
    }

    @Override
    public void nextStep(String name, String id, byte[] before) {
        nextStep(name, id, before, null);
    }
}
