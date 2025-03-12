public class LoosePackage extends Package {
    private String palletSerialNumber;

    public LoosePackage(String serialNumber, double mass, String qualityMark) {
        super(serialNumber, mass, qualityMark);
        this.palletSerialNumber = null;
    }


    @Override
    public String getPackageType() {
        return "LOOSE";
    }


    public void setPalletSerialNumber(String palletSerialNumber) {
        this.palletSerialNumber = palletSerialNumber;
    }

    @Override
    public String toString() {
        return super.toString() + ", palletSerialNumber='" + palletSerialNumber + "'";
    }
}