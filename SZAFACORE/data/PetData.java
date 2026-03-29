package dev.arab.SZAFACORE.data;

public class PetData {
  private String petId;
  
  private String uniqueId;
  
  private long expiryTimestamp;
  
  public PetData(String petId, String uniqueId, long expiryTimestamp) {
    this.petId = petId;
    this.uniqueId = uniqueId;
    this.expiryTimestamp = expiryTimestamp;
  }
  
  public String getPetId() {
    return this.petId;
  }
  
  public String getUniqueId() {
    return this.uniqueId;
  }
  
  public long getExpiryTimestamp() {
    return this.expiryTimestamp;
  }
  
  public void setExpiryTimestamp(long expiryTimestamp) {
    this.expiryTimestamp = expiryTimestamp;
  }
  
  public int getDaysRemaining() {
    long diff = this.expiryTimestamp - System.currentTimeMillis();
    return Math.max(0, (int)(diff / 86400000L));
  }
  
  public boolean isExpired() {
    return (System.currentTimeMillis() >= this.expiryTimestamp);
  }
  
  public void extendDays(int days) {
    this.expiryTimestamp += days * 24L * 60L * 60L * 1000L;
  }
  
  public String getDurationString() {
    long diff = this.expiryTimestamp - System.currentTimeMillis();
    if (diff <= 0L)
      return "0s"; 
    long days = diff / 86400000L;
    long hours = diff / 3600000L % 24L;
    long minutes = diff / 60000L % 60L;
    StringBuilder sb = new StringBuilder();
    if (days > 0L)
      sb.append(days).append("d "); 
    if (hours > 0L)
      sb.append(hours).append("h "); 
    if (minutes > 0L || sb.length() == 0)
      sb.append(minutes).append("m"); 
    return sb.toString().trim();
  }
}


/* Location:              C:\Users\bosiwo\Desktop\MINECRAFT\coreeveszafaitp.jar!\loluszek\pl\paczkiAnaeventowki\SZAFACORE\data\PetData.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */