
import java.util.*;

public class LogisticsManager {
    private final List<Warehouse> warehouses;
    private final Map<String, Package> packageHistory;
    private final Map<String, LocationInfo> packageLocationMap;

    public LogisticsManager() {
        this.warehouses = new ArrayList<>();
        this.packageHistory = new LinkedHashMap<>();
        this.packageLocationMap = new HashMap<>();
    }

    public void addWarehouse(Warehouse warehouse) {
        warehouses.add(warehouse);
    }

    public void updatePackageLocation(String serialNumber, String warehouseName, String lineId) {
        packageLocationMap.put(serialNumber, new LocationInfo(warehouseName, lineId));
    }

    public void removePackageLocation(String serialNumber) {
        packageLocationMap.remove(serialNumber);
    }

    public void printPackageLocation(String serialNumber) {
        if (packageLocationMap.containsKey(serialNumber)) {
            System.out.println("Package [" + serialNumber + "] is located at: " + packageLocationMap.get(serialNumber));
        } else {
            System.out.println("Location not found for package [" + serialNumber + "]");
        }
    }

    public void loadPackageToLine(String warehouseName, String lineId, Package pkg) {
        for (Warehouse warehouse : warehouses) {
            if (warehouse.getName().equalsIgnoreCase(warehouseName)) {
                Line line = warehouse.getLine(lineId);
                if (line == null) {
                    System.out.println("Line not found in warehouse.");
                    return;
                }

                boolean added;
                if (pkg instanceof CartonPackage && line instanceof CartonLine cartonLine) {
                    added = cartonLine.addPackage((CartonPackage) pkg);
                } else if (pkg instanceof LoosePackage && line instanceof PalletLine palletLine) {
                    Pallet pallet = findOrCreateMatchingPalletInLine(palletLine, pkg.getQualityMark());
                    added = pallet.addPackage((LoosePackage) pkg);
                    if (!palletLine.getPallets().contains(pallet)) {
                        palletLine.addPackage(pallet);
                    }
                } else {
                    System.out.println("Package and line type mismatch.");
                    return;
                }

                if (added) {
                    updatePackageLocation(pkg.getSerialNumber(), warehouseName, lineId);
                    addToPackageHistory(pkg);
                    System.out.println("Package loaded successfully.");
                    return;
                } else {
                    System.out.println("Package could not be loaded.");
                    return;
                }
            }
        }
        System.out.println("Warehouse not found.");
    }

    public void offloadPackage(String warehouseName, String lineId, String serialNumber) {
        for (Warehouse warehouse : warehouses) {
            if (warehouse.getName().equalsIgnoreCase(warehouseName)) {
                Line line = warehouse.getLine(lineId);
                if (line == null) {
                    System.out.println("Line not found in warehouse.");
                    return;
                }

                if (line instanceof CartonLine cartonLine) {
                    Iterator<CartonPackage> iterator = cartonLine.getPackages().iterator();
                    while (iterator.hasNext()) {
                        CartonPackage cp = iterator.next();
                        if (cp.getSerialNumber().equalsIgnoreCase(serialNumber)) {
                            iterator.remove();
                            removePackageLocation(serialNumber);
                            System.out.println("Carton package offloaded: " + serialNumber);
                            return;
                        }
                    }
                } else if (line instanceof PalletLine palletLine) {
                    for (Pallet pallet : palletLine.getPallets()) {
                        Iterator<LoosePackage> iterator = pallet.getPackages().iterator();
                        while (iterator.hasNext()) {
                            LoosePackage lp = iterator.next();
                            if (lp.getSerialNumber().equalsIgnoreCase(serialNumber)) {
                                iterator.remove();
                                removePackageLocation(serialNumber);
                                System.out.println("Loose package offloaded from pallet: " + serialNumber);
                                return;
                            }
                        }
                    }
                }
                System.out.println("Package not found in specified line.");
                return;
            }
        }
        System.out.println("Warehouse not found.");
    }

    public void discardPackage(String serialNumber) {
        for (Warehouse warehouse : warehouses) {
            Optional<Package> pkgOpt = warehouse.searchPackageBySerial(serialNumber);
            if (pkgOpt.isPresent()) {
                Package pkg = pkgOpt.get();
                pkg.discard();
                System.out.println("Package [" + serialNumber + "] has been discarded.");
                return;
            }
        }
        System.out.println("Package not found to discard.");
    }

    public void changeOffloadOrder(String lineId, Warehouse warehouse, boolean useFIFO) {
        Line line = warehouse.getLine(lineId);
        if (line == null) {
            System.out.println("Line not found in warehouse.");
            return;
        }

        if (line instanceof CartonLine cartonLine) {
            List<CartonPackage> pkgs = cartonLine.getPackages();
            if (!useFIFO) Collections.reverse(pkgs);
            System.out.println("Carton Line [" + lineId + "] reordered by " + (useFIFO ? "FIFO" : "LIFO"));
        } else if (line instanceof PalletLine palletLine) {
            for (Pallet pallet : palletLine.getPallets()) {
                List<LoosePackage> loosePkgs = pallet.getPackages();
                if (!useFIFO) Collections.reverse(loosePkgs);
            }
            System.out.println("Pallet Line [" + lineId + "] reordered by " + (useFIFO ? "FIFO" : "LIFO"));
        }
    }

    public void printPackageHistory() {
        System.out.println("=== PACKAGE HISTORY ===");
        for (Package pkg : packageHistory.values()) {
            System.out.println(pkg.toString());
        }
    }

    public void searchAndPrintPackage(String serialNumber) {
        for (Warehouse warehouse : warehouses) {
            Optional<Package> pkgOpt = warehouse.searchPackageBySerial(serialNumber);
            if (pkgOpt.isPresent()) {
                Package pkg = pkgOpt.get();
                System.out.println("Package Found: " + pkg);
                printPackageLocation(serialNumber);
                return;
            }
        }
        System.out.println("Package with serial [" + serialNumber + "] not found.");
    }

    public void searchAndPrintPallet(String palletSerialNumber) {
        for (Warehouse warehouse : warehouses) {
            Pallet pallet = warehouse.searchPalletBySerial(palletSerialNumber);
            if (pallet != null) {
                System.out.println("Pallet Found in Warehouse: " + warehouse.getName());
                System.out.println("Pallet Details: " + pallet);
                for (LoosePackage lp : pallet.getPackages()) {
                    System.out.println("   -> " + lp);
                }
                return;
            }
        }
        System.out.println("Pallet with serial [" + palletSerialNumber + "] not found.");
    }

    private Pallet findOrCreateMatchingPalletInLine(PalletLine line, String qualityMark) {
        for (Pallet p : line.getPallets()) {
            if (p.getQualityMark().equalsIgnoreCase(qualityMark) && !p.isFull()) {
                return p;
            }
        }
        return new Pallet("PL" + (line.getPallets().size() + 1), 5, qualityMark);
    }

    public void addToPackageHistory(Package pkg) {
        packageHistory.put(pkg.getSerialNumber(), pkg);
    }

}
