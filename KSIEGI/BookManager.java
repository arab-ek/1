package dev.arab.KSIEGI;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import dev.arab.EVENTOWKICORE.utils.ChatUtils;
import dev.arab.KSIEGI.ksiazki.Book;
import dev.arab.KSIEGI.ksiazki.CritBook;
import dev.arab.KSIEGI.ksiazki.DuraBook;
import dev.arab.KSIEGI.ksiazki.FireResistBook;
import dev.arab.KSIEGI.ksiazki.HasteBook;
import dev.arab.KSIEGI.ksiazki.PoisonBook;
import dev.arab.KSIEGI.ksiazki.RegenBook;
import dev.arab.KSIEGI.ksiazki.SlownessBook;
import dev.arab.KSIEGI.ksiazki.SpeedBook;
import dev.arab.KSIEGI.ksiazki.UnikBook;
import dev.arab.KSIEGI.ksiazki.VampBook;
import dev.arab.Main;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;

public class BookManager {
  private final Main plugin;
  
  private FileConfiguration config;
  
  private final NamespacedKey bookKey;
  
  private final NamespacedKey HAS_CUSTOM_BOOK_KEY;
  
  private final NamespacedKey REGEN_FIX_KEY;
  
  private final List<Book> books = new ArrayList<>();
  
  private final List<Book> tickableBooks = new ArrayList<>();
  
  private final Map<String, String> bookNamesStripped = new HashMap<>();
  
  public BookManager(Main plugin) {
    this.plugin = plugin;
    this.bookKey = new NamespacedKey((Plugin)plugin, "ksiega_id");
    this.HAS_CUSTOM_BOOK_KEY = new NamespacedKey((Plugin)plugin, "has_custom_book");
    this.REGEN_FIX_KEY = new NamespacedKey((Plugin)plugin, "regen_fix_v1");
    loadConfig();
    registerBooks();
  }
  
  private void registerBooks() {
    addBook((Book)new UnikBook(this.plugin));
    addBook((Book)new CritBook(this.plugin));
    addBook((Book)new SpeedBook(this.plugin));
    addBook((Book)new VampBook(this.plugin));
    addBook((Book)new DuraBook(this.plugin));
    addBook((Book)new RegenBook(this.plugin));
    addBook((Book)new HasteBook(this.plugin));
    addBook((Book)new SlownessBook(this.plugin));
    addBook((Book)new FireResistBook(this.plugin));
    addBook((Book)new PoisonBook(this.plugin));
    for (Book book : this.books) {
      String stripped = ChatUtils.stripColor(book.getDisplayName());
      if (stripped != null && !stripped.isEmpty())
        this.bookNamesStripped.put(stripped.toLowerCase(), book.getId()); 
    } 
  }
  
  private void addBook(Book book) {
    this.books.add(book);
    if (book instanceof SpeedBook || book instanceof HasteBook || book instanceof FireResistBook)
      this.tickableBooks.add(book); 
  }
  
  public List<Book> getBooks() {
    return this.books;
  }
  
  public List<Book> getTickableBooks() {
    return this.tickableBooks;
  }
  
  public Book getBookById(String id) {
    return this.books.stream().filter(b -> b.getId().equals(id)).findFirst().orElse(null);
  }
  
  public void loadConfig() {
    File file = new File(this.plugin.getDataFolder(), "config-ksiegi.yml");
    if (!file.exists())
      this.plugin.saveResource("config-ksiegi.yml", false); 
    this.config = (FileConfiguration)YamlConfiguration.loadConfiguration(file);
  }
  
  public FileConfiguration getConfig() {
    return this.config;
  }
  
  public ItemStack createBook(String bookId) {
    ConfigurationSection section = this.config.getConfigurationSection("books." + bookId);
    if (section == null)
      return null; 
    ItemStack item = new ItemStack(Material.ENCHANTED_BOOK);
    ItemMeta meta = item.getItemMeta();
    if (meta == null)
      return item; 
    String name = section.getString("name", "&eZaklęta książka");
    meta.setDisplayName(name.replace("&", "§"));
    List<String> lore = section.getStringList("lore");
    if (lore != null)
      meta.setLore((List)lore.stream().map(line -> line.replace("&", "§")).collect(Collectors.toList())); 
    meta.getPersistentDataContainer().set(this.bookKey, PersistentDataType.STRING, bookId);
    int customModelData = section.getInt("custom_model_data", 0);
    if (customModelData > 0)
      meta.setCustomModelData(Integer.valueOf(customModelData)); 
    item.setItemMeta(meta);
    return item;
  }
  
  public NamespacedKey getBookKey() {
    return this.bookKey;
  }
  
  public String getBookId(ItemStack item) {
    return (item == null || !item.hasItemMeta()) ? null : getBookId(item.getItemMeta());
  }
  
  public String getBookId(ItemMeta meta) {
    if (meta == null)
      return null; 
    String id = (String)meta.getPersistentDataContainer().get(this.bookKey, PersistentDataType.STRING);
    if (id != null)
      return id; 
    if (meta.hasDisplayName()) {
      String name = meta.getDisplayName();
      for (String key : this.config.getConfigurationSection("books").getKeys(false)) {
        String configName = this.config.getString("books." + key + ".name");
        if (configName != null && name.equals(configName.replace("&", "§")))
          return key; 
      } 
    } 
    return null;
  }
  
  public boolean hasCustomBook(ItemStack item) {
    return (item == null || !item.hasItemMeta()) ? false : hasCustomBook(item, item.getItemMeta());
  }
  
  public boolean hasCustomBook(ItemStack item, ItemMeta meta) {
    if (meta == null)
      return false; 
    PersistentDataContainer pdc = meta.getPersistentDataContainer();
    if (pdc.has(this.HAS_CUSTOM_BOOK_KEY, PersistentDataType.BYTE))
      return true; 
    if (meta.hasLore()) {
      List<String> lore = meta.getLore();
      boolean found = false;
      for (String line : lore) {
        String strippedLine = ChatUtils.stripColor(line).toLowerCase();
        for (Map.Entry<String, String> entry : this.bookNamesStripped.entrySet()) {
          if (strippedLine.contains(entry.getKey())) {
            pdc.set(new NamespacedKey((Plugin)this.plugin, "ksiega_" + (String)entry.getValue()), PersistentDataType.BYTE, Byte.valueOf((byte)1));
            found = true;
          } 
        } 
      } 
      if (found) {
        pdc.set(this.HAS_CUSTOM_BOOK_KEY, PersistentDataType.BYTE, Byte.valueOf((byte)1));
        return true;
      } 
    } 
    return false;
  }
  
  public void migrateItem(ItemStack item) {
    if (item == null || item.getType() == Material.AIR || !item.hasItemMeta())
      return; 
    ItemMeta meta = item.getItemMeta();
    if (meta == null)
      return; 
    if (meta.getPersistentDataContainer().has(this.HAS_CUSTOM_BOOK_KEY, PersistentDataType.BYTE))
      return; 
    hasCustomBook(item, meta);
  }
}


/* Location:              C:\Users\bosiwo\Desktop\MINECRAFT\coreeveszafaitp.jar!\loluszek\pl\paczkiAnaeventowki\KSIEGI\BookManager.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */