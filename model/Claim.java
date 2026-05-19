package model;

public class Claim {
    private int id;
    private int itemId;
    private String itemName;
    private String claimantName;
    private String proof;
    private String status;

    public Claim(int id, int itemId, String itemName, String claimantName, String proof, String status) {
        this.id = id;
        this.itemId = itemId;
        this.itemName = itemName;
        this.claimantName = claimantName;
        this.proof = proof;
        this.status = status;
    }

    public int getId() { return id; }
    public int getItemId() { return itemId; }
    public String getItemName() { return itemName; }
    public String getClaimantName() { return claimantName; }
    public String getProof() { return proof; }
    public String getStatus() { return status; }

    public void setStatus(String status) { this.status = status; }
}
