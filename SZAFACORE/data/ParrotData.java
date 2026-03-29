package dev.arab.SZAFACORE.data;

import java.util.ArrayList;
import java.util.List;

public class ParrotData {
  private final String parrotId;
  
  private final String uniqueId;
  
  private final long expiryTimestamp;
  
  private final String collector;
  
  private int rerolls;
  
  private List<String> effects;
  
  public ParrotData(String parrotId, String uniqueId, long expiryTimestamp, String collector, int rerolls, List<String> effects) {
    this.parrotId = parrotId;
    this.uniqueId = uniqueId;
    this.expiryTimestamp = expiryTimestamp;
    this.collector = collector;
    this.rerolls = rerolls;
    this.effects = (effects != null) ? effects : new ArrayList<>();
  }
  
  public String getParrotId() {
    return this.parrotId;
  }
  
  public String getUniqueId() {
    return this.uniqueId;
  }
  
  public long getExpiryTimestamp() {
    return this.expiryTimestamp;
  }
  
  public String getCollector() {
    return this.collector;
  }
  
  public int getRerolls() {
    return this.rerolls;
  }
  
  public void incrementRerolls() {
    this.rerolls++;
  }
  
  public List<String> getEffects() {
    return this.effects;
  }
  
  public void setEffects(List<String> effects) {
    this.effects = effects;
  }
  
  public boolean isExpired() {
    return (this.expiryTimestamp != -1L && System.currentTimeMillis() > this.expiryTimestamp);
  }
  
  public String getDurationString() {
    if (this.expiryTimestamp == -1L)
      return "Na zawsze"; 
    long diff = this.expiryTimestamp - System.currentTimeMillis();
    if (diff <= 0L)
      return "Wygasł"; 
    long days = diff / 86400000L;
    long hours = diff / 3600000L % 24L;
    long minutes = diff / 60000L % 60L;
    return (days > 0L) ? ("" + days + "d " + days + "h") : ((hours > 0L) ? ("" + hours + "h " + hours + "m") : ("" + minutes + "m"));
  }
}


/* Location:              C:\Users\bosiwo\Desktop\MINECRAFT\coreeveszafaitp.jar!\loluszek\pl\paczkiAnaeventowki\SZAFACORE\data\ParrotData.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */