import java.util.ArrayList;
import java.util.List;

public class Pallet {
    private final String serialNumber;
    private final int maxCapacity;
    private final String qualityMark;
    private final List<LoosePackage> packages;

    public Pallet(String serialNumber, int maxCapacity, String qualityMark) {
        this.serialNumber = serialNumber;
        this.maxCapacity = maxCapacity;
        this.qualityMark = qualityMark;
        this.packages = new ArrayList<>();
    }

    public boolean addPackage(LoosePackage loosePackage) {
        if (packages.size() >= maxCapacity) {
            System.out.println("Pallet is full. Cannot add package: " + loosePackage.getSerialNumber());
            return false;
        }

        if (!loosePackage.getQualityMark().equalsIgnoreCase(this.qualityMark)) {
            System.out.println("Package quality does not match the pallet quality.");
            return false;
        }

        packages.add(loosePackage);
        loosePackage.setPalletSerialNumber(this.serialNumber);
        return true;
    }



    public boolean isFull() {
        return packages.size() >= maxCapacity;
    }


    public List<LoosePackage> getPackages() {
        return packages;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public String getQualityMark() {
        return qualityMark;
    }


    @Override
    public String toString() {
        return "Pallet{" +
                "serialNumber='" + serialNumber + '\'' +
                ", maxCapacity=" + maxCapacity +
                ", qualityMark='" + qualityMark + '\'' +
                ", currentPackageCount=" + packages.size() +
                '}';
    }
}

