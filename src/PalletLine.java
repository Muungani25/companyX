import java.util.ArrayList;
import java.util.List;

public class PalletLine extends Line {
    private final List<Pallet> pallets;

    public PalletLine(String lineId, double maxWeight, boolean isSpecialLine) {
        super(lineId, maxWeight, isSpecialLine);
        this.pallets = new ArrayList<>();
    }

    @Override
    public boolean addPackage(Object obj) {
        if (!(obj instanceof Pallet pallet)) return false;

        double palletWeight = pallet.getPackages().stream()
                .mapToDouble(LoosePackage::getMass)
                .sum();

        double currentWeight = getTotalWeight();

        if (currentWeight + palletWeight > maxCapacity) {
            System.out.println("Exceeded pallet line weight capacity.");
            return false;
        }

        if (!isSpecialLine && !qualityMarks.isEmpty() && !qualityMarks.contains(pallet.getQualityMark())) {
            System.out.println("Pallet quality mismatch.");
            return false;
        }

        qualityMarks.add(pallet.getQualityMark());
        if (!isSpecialLine && qualityMarks.size() > 1) {
            qualityMarks.remove(pallet.getQualityMark());
            System.out.println("Cannot mix quality types on standard pallet line.");
            return false;
        }

        if (isSpecialLine && qualityMarks.size() > 3) {
            qualityMarks.remove(pallet.getQualityMark());
            System.out.println("Exceeded max quality types on special pallet line.");
            return false;
        }

        pallets.add(pallet);
        return true;
    }

    public double getTotalWeight() {
        return pallets.stream()
                .flatMap(p -> p.getPackages().stream())
                .mapToDouble(LoosePackage::getMass)
                .sum();
    }

    public List<Pallet> getPallets() {
        return pallets;
    }

    @Override
    public String getLineType() {
        return "PALLET_LINE";
    }
}
