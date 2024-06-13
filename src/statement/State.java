package statement;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class State {
    private final String name;
    private final String id;

    protected State parent;
    protected byte[] beforeValue;
    protected byte[] additionalValue = null;
    protected byte[] afterValue;
    protected List<State> innerStates = new ArrayList<>();

    protected State(String name, String id, State parent) {
        this.name = name;
        this.id = id;
        this.parent = parent;
    }


    public String getName() {
        return name;
    }

    public String getID() {
        return parent == null ? id : parent.id + "_" + id;
    }

    public byte[] getBefore() {
        return beforeValue;
    }

    public boolean hasAdditional() {
        return additionalValue != null;
    }

    public byte[] getAdditional() {
        return additionalValue;
    }

    public byte[] getAfter() {
        return afterValue;
    }


    public boolean hasInner() {
        return innerStates == null;
    }

    public List<State> getInner() {
        return innerStates;
    }


    @Override
    public String toString() {
        return name;
    }
}
