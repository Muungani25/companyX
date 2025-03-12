import java.util.*;

public class Warehouse {
    private final String name;
    private final double maxCapacityKg;
    private final Map<String, Line> lines;

    public Warehouse(String name, double maxCapacityKg) {
        this.name = name;
        this.maxCapacityKg = maxCapacityKg;
        this.lines = new HashMap<>();
    }


    public void addLine(Line line) {
        if (lines.containsKey(line.getLineId())) {
            System.out.println("Line with ID " + line.getLineId() + " already exists.");
            return;
        }
        lines.put(line.getLineId(), line);
    }


    public Line getLine(String lineId) {
        return lines.get(lineId);
    }

    public String getName() {
        return name;
    }

    public double getMaxCapacityKg() {
        return maxCapacityKg;
    }

    public double getCurrentUtilizedCapacity() {
        double total = 0;
        for (Line line : lines.values()) {
            if (line instanceof PalletLine palletLine) {
                total += palletLine.getTotalWeight();
            } else if (line instanceof CartonLine cartonLine) {
                for (CartonPackage cp : cartonLine.getPackages()) {
                    total += cp.getMass();
                }
            }
        }
        return total;
    }

    public double getUtilizationPercentage() {
        return (getCurrentUtilizedCapacity() / maxCapacityKg) * 100;
    }


    public void printWarehouseSnapshot() {
        System.out.println("\n=== WAREHOUSE SNAPSHOT: " + name + " ===");
        System.out.println("Total Capacity: " + maxCapacityKg + " kg");
        System.out.printf("Utilized Capacity: %.2f kg (%.2f%%)\n", getCurrentUtilizedCapacity(), getUtilizationPercentage());
        System.out.println("Lines:");

        for (Line line : lines.values()) {
            System.out.println(" - Line ID: " + line.getLineId() + " (" + line.getLineType() + ")");
            if (line instanceof CartonLine cartonLine) {
                for (CartonPackage cp : cartonLine.getPackages()) {
                    System.out.println("   > Carton: " + cp.getSerialNumber() + ", Quality: " + cp.getQualityMark() + ", Mass: " + cp.getMass());
                }
            } else if (line instanceof PalletLine palletLine) {
                for (Pallet pallet : palletLine.getPallets()) {
                    System.out.println("   > Pallet: " + pallet.getSerialNumber() + " [Quality: " + pallet.getQualityMark() + "]");
                    for (LoosePackage lp : pallet.getPackages()) {
                        System.out.println("     - LoosePackage: " + lp.getSerialNumber() + ", Mass: " + lp.getMass() + ", Quality: " + lp.getQualityMark());
                    }
                }
            }
        }
        System.out.println(".................................................");
    }


    public Optional<Package> searchPackageBySerial(String serialNumber) {
        for (Line line : lines.values()) {
            if (line instanceof CartonLine cartonLine) {
                for (CartonPackage cp : cartonLine.getPackages()) {
                    if (cp.getSerialNumber().equalsIgnoreCase(serialNumber)) {
                        return Optional.of(cp);
                    }
                }
            } else if (line instanceof PalletLine palletLine) {
                for (Pallet pallet : palletLine.getPallets()) {
                    for (LoosePackage lp : pallet.getPackages()) {
                        if (lp.getSerialNumber().equalsIgnoreCase(serialNumber)) {
                            return Optional.of(lp);
                        }
                    }
                }
            }
        }
        return Optional.empty();
    }


    public Pallet searchPalletBySerial(String palletSerial) {
        for (Line line : lines.values()) {
            if (line instanceof PalletLine palletLine) {
                for (Pallet pallet : palletLine.getPallets()) {
                    if (pallet.getSerialNumber().equalsIgnoreCase(palletSerial)) {
                        return pallet;
                    }
                }
            }
        }
        return null;
    }
}
