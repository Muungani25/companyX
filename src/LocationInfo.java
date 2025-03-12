
public class LocationInfo {
    private final String warehouseName;
    private final String lineId;

    public LocationInfo(String warehouseName, String lineId) {
        this.warehouseName = warehouseName;
        this.lineId = lineId;
    }


    @Override
    public String toString() {
        return "Warehouse: " + warehouseName + ", Line: " + lineId;
    }
}
