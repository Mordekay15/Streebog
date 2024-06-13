package analysis;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Iterator;


public final class OptionsIterator implements Iterator<byte[]> {
    private final byte[] option;
    private final BigDecimal length;
    private BigDecimal seen;
    private boolean depleted = false;

    OptionsIterator(int places) {
        this.option = new byte[places];
        this.length = BigDecimal.valueOf(Math.pow(256, places));
        this.seen = BigDecimal.ZERO;
    }


    private boolean underflows(byte question) {
        return question != -1;
    }

    private boolean hasSpace() {
        boolean hasSpace = false;
        for (byte opt : option) {
            hasSpace |= underflows(opt);
        }
        return hasSpace;
    }

    public void refill() {
        seen = BigDecimal.ZERO;
        depleted = false;
    }

    public BigDecimal getLength() {
        return length;
    }

    public BigDecimal getSeen() {
        return seen;
    }


    @Override
    public boolean hasNext() {
        return !depleted;
    }

    @Override
    public byte[] next() {
        depleted = !hasSpace();
        byte[] result = Arrays.copyOf(option, option.length);
        for (int i = 0; i < option.length; i++) {
            if (underflows(option[i])) {
                option[i] += 1;
                break;
            } else {
                option[i] = 0;
            }
        }
        seen = seen.add(BigDecimal.ONE);
        return result;
    }
}
