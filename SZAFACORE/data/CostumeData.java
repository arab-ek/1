package dev.arab.SZAFACORE.data;

import java.util.concurrent.TimeUnit;

public class CostumeData {
  private final String costumeId;
  
  private final String uniqueId;
  
  private long expiryTime;
  
  public CostumeData(String costumeId, String uniqueId, long expiryTime) {
    this.costumeId = costumeId;
    this.uniqueId = uniqueId;
    this.expiryTime = expiryTime;
  }
  
  public String getCostumeId() {
    return this.costumeId;
  }
  
  public String getUniqueId() {
    return this.uniqueId;
  }
  
  public long getExpiryTime() {
    return this.expiryTime;
  }
  
  public void setExpiryTime(long expiryTime) {
    this.expiryTime = expiryTime;
  }
  
  public boolean isExpired() {
    return (this.expiryTime != -1L && System.currentTimeMillis() > this.expiryTime);
  }
  
  public void extendDays(int days) {
    if (this.expiryTime == -1L)
      return; 
    this.expiryTime += days * 24L * 60L * 60L * 1000L;
  }
  
  public String getDurationString() {
    if (this.expiryTime == -1L)
      return "Pernamentny"; 
    long diff = this.expiryTime - System.currentTimeMillis();
    if (diff <= 0L)
      return "Wygasł"; 
    long days = TimeUnit.MILLISECONDS.toDays(diff);
    long hours = TimeUnit.MILLISECONDS.toHours(diff) % 24L;
    long minutes = TimeUnit.MILLISECONDS.toMinutes(diff) % 60L;
    StringBuilder sb = new StringBuilder();
    if (days > 0L)
      sb.append(days).append("d "); 
    if (hours > 0L)
      sb.append(hours).append("h "); 
    if (minutes > 0L)
      sb.append(minutes).append("m"); 
    return (sb.length() == 0) ? "Kilka sekund" : sb.toString().trim();
  }
}


/* Location:              C:\Users\bosiwo\Desktop\MINECRAFT\coreeveszafaitp.jar!\loluszek\pl\paczkiAnaeventowki\SZAFACORE\data\CostumeData.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */