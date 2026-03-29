package dev.arab.SZAFACORE.util;

public class TimeConverter {
  public static long parseTimeToMillis(String timeStr) {
    if (timeStr == null || timeStr.isEmpty())
      return -1L; 
    timeStr = timeStr.toLowerCase().trim();
    try {
      char unit = timeStr.charAt(timeStr.length() - 1);
      String numberPart = timeStr.substring(0, timeStr.length() - 1);
      int amount = Integer.parseInt(numberPart);
      switch (unit) {
        case 'd':
          return amount * 24L * 60L * 60L * 1000L;
        case 'h':
          return amount * 60L * 60L * 1000L;
        case 'm':
          return amount * 60L * 1000L;
        case 's':
          return amount * 1000L;
      } 
      return -1L;
    } catch (NumberFormatException|StringIndexOutOfBoundsException e) {
      return -1L;
    } 
  }
  
  public static String formatMillis(long millis) {
    if (millis <= 0L)
      return "0s"; 
    long seconds = millis / 1000L;
    long minutes = seconds / 60L;
    long hours = minutes / 60L;
    long days = hours / 24L;
    return (days >= 1L && hours % 24L == 0L) ? ("" + days + "d") : ((hours >= 1L && minutes % 60L == 0L) ? ("" + hours + "h") : ((minutes >= 1L && seconds % 60L == 0L) ? ("" + minutes + "m") : ("" + seconds + "s")));
  }
  
  public static int parseTimeToDays(String timeStr) {
    long millis = parseTimeToMillis(timeStr);
    return (millis == -1L) ? -1 : (int)(millis / 86400000L);
  }
}


/* Location:              C:\Users\bosiwo\Desktop\MINECRAFT\coreeveszafaitp.jar!\loluszek\pl\paczkiAnaeventowki\SZAFACOR\\util\TimeConverter.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */