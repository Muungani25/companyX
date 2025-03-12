import java.util.ArrayList;
import java.util.List;

public class CartonLine extends Line {
    private final List<CartonPackage> packages;

    public CartonLine(String lineId, double maxCartonCount, boolean isSpecialLine) {
        super(lineId, maxCartonCount, isSpecialLine);
        this.packages = new ArrayList<>();
    }

    @Override
    public boolean addPackage(Object pkg) {
        if (!(pkg instanceof CartonPackage carton)) return false;

        if (packages.size() >= maxCapacity) {
            System.out.println("Carton line is full.");
            return false;
        }

        if (!isSpecialLine && !qualityMarks.isEmpty() && !qualityMarks.contains(carton.getQualityMark())) {
            System.out.println("Carton quality mismatch.");
            return false;
        }

        qualityMarks.add(carton.getQualityMark());
        if (!isSpecialLine && qualityMarks.size() > 1) {
            qualityMarks.remove(carton.getQualityMark());
            System.out.println("Cannot mix quality types on a standard carton line.");
            return false;
        }

        if (isSpecialLine && qualityMarks.size() > 3) {
            qualityMarks.remove(carton.getQualityMark());
            System.out.println("Exceeded max quality types on special carton line.");
            return false;
        }

        packages.add(carton);
        return true;
    }

    public List<CartonPackage> getPackages() {
        return packages;
    }

    @Override
    public String getLineType() {
        return "CARTON_LINE";
    }
}

