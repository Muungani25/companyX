public class CartonPackage extends Package {

    public CartonPackage(String serialNumber, double mass, String qualityMark) {
        super(serialNumber, mass, qualityMark);
    }

    @Override
    public String getPackageType() {
        return "CARTON";
    }

    @Override
    public String toString() {
        return super.toString();
    }
}