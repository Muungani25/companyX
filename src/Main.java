
import java.util.*;

public class Main {
    private static final List<Warehouse> sampleWarehouses = new ArrayList<>();
    private static final LogisticsManager manager = new LogisticsManager();

    public static void main(String[] args) {
        initializeSampleData();

        // 1. Load a new package onto a line (rack and line)
        System.out.println("\n--- LOADING NEW PACKAGES ---");
        CartonPackage newCarton = new CartonPackage("CP999", 9.5, "Z");
        manager.loadPackageToLine("Warehouse-A", "C-103", newCarton);
        LoosePackage newLoose = new LoosePackage("LP999", 10.5, "W");
        manager.loadPackageToLine("Warehouse-A", "P-203", newLoose);

        // 2. Offload a package from a line (rack and line)
        System.out.println("\n--- OFFLOADING PACKAGES ---");
        manager.offloadPackage("Warehouse-A", "C-101", "CP001");
        manager.offloadPackage("Warehouse-A", "P-201", "LP001");

        // 3. Load a rack (pallet) into warehouse manually
        System.out.println("\n--- MANUAL PALLET LOADING ---");
        Pallet newPallet = new Pallet("PL999", 4, "M");
        newPallet.addPackage(new LoosePackage("LP777", 12.0, "M"));
        newPallet.addPackage(new LoosePackage("LP778", 13.0, "M"));
        PalletLine palletLine = (PalletLine) sampleWarehouses.get(0).getLine("P-202");
        palletLine.addPackage(newPallet);
        System.out.println("Manual pallet loaded to line P-202");

        // 4. Manipulate warehouse and line capacity
        System.out.println("\n--- CAPACITY CHECK ---");
        Warehouse w = sampleWarehouses.get(0);
        System.out.println("Warehouse Capacity: " + w.getMaxCapacityKg());
        System.out.println("Warehouse Used Capacity: " + w.getCurrentUtilizedCapacity());
        System.out.printf("Utilization: %.2f%%\n", w.getUtilizationPercentage());

        // 5. Discard a package
        System.out.println("\n--- DISCARDING PACKAGE ---");
        manager.discardPackage("CP002");

        // 6. Change offload order: FIFO or LIFO
        System.out.println("\n--- CHANGING OFFLOAD ORDER ---");
        manager.changeOffloadOrder("C-102", sampleWarehouses.get(0), false); // LIFO

        // 7. Record of all packages ever placed (History)
        System.out.println("\n--- PACKAGE HISTORY ---");
        manager.printPackageHistory();

        // 8. Warehouse Snapshot
        System.out.println("\n--- WAREHOUSE SNAPSHOTS ---");
        for (Warehouse wh : sampleWarehouses) {
            wh.printWarehouseSnapshot();
        }

        // 9. Search for a package by serial number
        System.out.println("\n--- SEARCH PACKAGE BY SERIAL ---");
        manager.searchAndPrintPackage("LP002");
        manager.searchAndPrintPackage("CP003");

        // 10. Search for a rack (pallet) by serial number
        System.out.println("\n--- SEARCH RACK BY SERIAL ---");
        manager.searchAndPrintPallet("PL999");
    }

    private static void initializeSampleData() {
        Warehouse warehouseA = new Warehouse("Warehouse-A", 7000);
        CartonLine cl1 = new CartonLine("C-101", 10, false);
        PalletLine pl1 = new PalletLine("P-201", 3000, false);
        CartonLine cl2 = new CartonLine("C-102", 8, true);
        PalletLine pl2 = new PalletLine("P-202", 2500, false);
        CartonLine cl3 = new CartonLine("C-103", 5, false);
        PalletLine pl3 = new PalletLine("P-203", 2000, true);

        warehouseA.addLine(cl1);
        warehouseA.addLine(pl1);
        warehouseA.addLine(cl2);
        warehouseA.addLine(pl2);
        warehouseA.addLine(cl3);
        warehouseA.addLine(pl3);
        manager.addWarehouse(warehouseA);
        sampleWarehouses.add(warehouseA);

        // Load packages
        manager.loadPackageToLine("Warehouse-A", "C-101", new CartonPackage("CP001", 12.5, "A"));
        manager.loadPackageToLine("Warehouse-A", "C-101", new CartonPackage("CP002", 10.0, "A"));
        manager.loadPackageToLine("Warehouse-A", "C-102", new CartonPackage("CP003", 11.0, "B"));
        manager.loadPackageToLine("Warehouse-A", "P-201", new LoosePackage("LP001", 15.0, "X"));
        manager.loadPackageToLine("Warehouse-A", "P-201", new LoosePackage("LP002", 14.5, "X"));
    }
}
