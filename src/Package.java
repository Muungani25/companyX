import java.time.LocalDateTime;

public abstract class Package {
    protected String serialNumber;
    protected double mass;
    protected String qualityMark;
    protected boolean isDiscarded;
    protected LocalDateTime timestamp;

    public Package(String serialNumber, double mass, String qualityMark) {
        this.serialNumber = serialNumber;
        this.mass = mass;
        this.qualityMark = qualityMark;
        this.isDiscarded = false;
        this.timestamp = LocalDateTime.now();
    }

    public abstract String getPackageType();

    public String getSerialNumber() {
        return serialNumber;
    }

    public double getMass() {
        return mass;
    }

    public String getQualityMark() {
        return qualityMark;
    }


    public void discard() {
        this.isDiscarded = true;
    }



    @Override
    public String toString() {
        return getPackageType() + " Package {" +
                "serialNumber='" + serialNumber + '\'' +
                ", mass=" + mass +
                ", qualityMark='" + qualityMark + '\'' +
                ", isDiscarded=" + isDiscarded +
                ", timestamp=" + timestamp +
                '}';
    }
}