import java.util.HashSet;
import java.util.Set;

public abstract class Line {
    protected String lineId;
    protected double maxCapacity;
    protected Set<String> qualityMarks;
    protected boolean isSpecialLine;

    public Line(String lineId, double maxCapacity, boolean isSpecialLine) {
        this.lineId = lineId;
        this.maxCapacity = maxCapacity;
        this.isSpecialLine = isSpecialLine;
        this.qualityMarks = new HashSet<>();
    }

    public abstract boolean addPackage(Object pkg); // override with proper type in subclasses

    public String getLineId() {
        return lineId;
    }

    public abstract String getLineType();
}

