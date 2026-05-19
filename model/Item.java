package model;

public class Item {

    private int id;
    private String itemName;
    private String description;
    private String category;
    private String locationFound;
    private String status;
    private String reportedBy;
    private String imagePath; // NEW
    private boolean archived; // NEW

    public Item() {
    }

    public Item(String itemName, String description,
                String category, String locationFound,
                String status, String reportedBy,
                String imagePath, boolean archived) {

        this.itemName = itemName;
        this.description = description;
        this.category = category;
        this.locationFound = locationFound;
        this.status = status;
        this.reportedBy = reportedBy;
        this.imagePath = imagePath;
        this.archived = archived;
    }

    // GETTERS
    public int getId() { return id; }
    public String getItemName() { return itemName; }
    public String getDescription() { return description; }
    public String getCategory() { return category; }
    public String getLocationFound() { return locationFound; }
    public String getStatus() { return status; }
    public String getReportedBy() { return reportedBy; }
    public String getImagePath() { return imagePath; }
    //SETTERS
    public void setId(int id) { this.id = id; }
    public void setItemName(String itemName) { this.itemName = itemName; }
    public void setDescription(String description) { this.description = description; }
    public void setCategory(String category) { this.category = category; }
    public void setLocationFound(String locationFound) { this.locationFound = locationFound; }
    public void setStatus(String status) { this.status = status; }
    public void setReportedBy(String reportedBy) { this.reportedBy = reportedBy; }
}
