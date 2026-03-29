package dev.arab;

import java.io.File;
import java.util.Iterator;
import java.util.Random;
import dev.arab.EVENTOWKICORE.commands.AnarchiaEventowkiCommand;
import dev.arab.EVENTOWKICORE.commands.ExcaliburCommand;
import dev.arab.EVENTOWKICORE.eventowki.ArcusMagnus;
import dev.arab.EVENTOWKICORE.eventowki.BalonikZHelem;
import dev.arab.EVENTOWKICORE.eventowki.BlokWidmo;
import dev.arab.EVENTOWKICORE.eventowki.BombardaMaxina;
import dev.arab.EVENTOWKICORE.eventowki.BoskiTopor;
import dev.arab.EVENTOWKICORE.eventowki.CiepleMleko;
import dev.arab.EVENTOWKICORE.eventowki.CudownaLatarnia;
import dev.arab.EVENTOWKICORE.eventowki.Dynamit;
import dev.arab.EVENTOWKICORE.eventowki.EventItemManager;
import dev.arab.EVENTOWKICORE.eventowki.Excalibur;
import dev.arab.EVENTOWKICORE.eventowki.HydroKlatka;
import dev.arab.EVENTOWKICORE.eventowki.JajkoCreepera;
import dev.arab.EVENTOWKICORE.eventowki.KoronaAnarchii;
import dev.arab.EVENTOWKICORE.eventowki.Kosa;
import dev.arab.EVENTOWKICORE.eventowki.KostkaRubika;
import dev.arab.EVENTOWKICORE.eventowki.KrewWampira;
import dev.arab.EVENTOWKICORE.eventowki.KupaAnarchii;
import dev.arab.EVENTOWKICORE.eventowki.LeweJajko;
import dev.arab.EVENTOWKICORE.eventowki.Lizak;
import dev.arab.EVENTOWKICORE.eventowki.LopataGrincha;
import dev.arab.EVENTOWKICORE.eventowki.LukKupidyna;
import dev.arab.EVENTOWKICORE.eventowki.MarchewkowaKusza;
import dev.arab.EVENTOWKICORE.eventowki.MarchewkowyMiecz;
import dev.arab.EVENTOWKICORE.eventowki.Parawan;
import dev.arab.EVENTOWKICORE.eventowki.PiekielnaTarcza;
import dev.arab.EVENTOWKICORE.eventowki.PiekielnyMiecz;
import dev.arab.EVENTOWKICORE.eventowki.Piernik;
import dev.arab.EVENTOWKICORE.eventowki.PrzeterminowanyTrunek;
import dev.arab.EVENTOWKICORE.eventowki.RogJednorozca;
import dev.arab.EVENTOWKICORE.eventowki.RozaKupidyna;
import dev.arab.EVENTOWKICORE.eventowki.RozaKupidyna2026;
import dev.arab.EVENTOWKICORE.eventowki.RozdzkaIluzjonisty;
import dev.arab.EVENTOWKICORE.eventowki.Rozga;
import dev.arab.EVENTOWKICORE.eventowki.RozgotowanaKukurydza;
import dev.arab.EVENTOWKICORE.eventowki.SakiewkaDropu;
import dev.arab.EVENTOWKICORE.eventowki.SiekieraGrincha;
import dev.arab.EVENTOWKICORE.eventowki.SmoczyMiecz;
import dev.arab.EVENTOWKICORE.eventowki.Sniezka;
import dev.arab.EVENTOWKICORE.eventowki.SplesnialaKanapka;
import dev.arab.EVENTOWKICORE.eventowki.TotemUlaskawienia;
import dev.arab.EVENTOWKICORE.eventowki.TrojzabPosejdona;
import dev.arab.EVENTOWKICORE.eventowki.TurboTrap;
import dev.arab.EVENTOWKICORE.eventowki.WampirzeJablko;
import dev.arab.EVENTOWKICORE.eventowki.WataCukrowa;
import dev.arab.EVENTOWKICORE.eventowki.WedkaNielota;
import dev.arab.EVENTOWKICORE.eventowki.WedkaSurferka;
import dev.arab.EVENTOWKICORE.eventowki.WzmocnionaElytra;
import dev.arab.EVENTOWKICORE.eventowki.ZajeczyMiecz;
import dev.arab.EVENTOWKICORE.eventowki.ZatrutyOlowek;
import dev.arab.EVENTOWKICORE.eventowki.ZlamaneSerce;
import dev.arab.EVENTOWKICORE.hooks.WorldGuardHook;
import dev.arab.EVENTOWKICORE.listeners.InventoryListener;
import dev.arab.EVENTOWKICORE.listeners.ItemListener;
import dev.arab.EVENTOWKICORE.tasks.ActionBarTask;
import dev.arab.EVENTOWKICORE.tasks.PassiveEffectTask;
import dev.arab.EVENTOWKICORE.utils.BlockTracker;
import dev.arab.EVENTOWKICORE.utils.CooldownManager;
import dev.arab.EVENTOWKICORE.utils.EquipmentCacheManager;
import dev.arab.ADDONS.GUI.ModuleInventory;
import dev.arab.ADDONS.GUI.ModuleInventoryListener;
import dev.arab.ADDONS.INCOGNITO.IncognitoCommand;
import dev.arab.ADDONS.INCOGNITO.IncognitoExpansion;
import dev.arab.ADDONS.INCOGNITO.IncognitoManager;
import dev.arab.KSIEGI.BookListener;
import dev.arab.KSIEGI.BookManager;
import dev.arab.KSIEGI.KsiegiCommand;
import dev.arab.ADDONS.PVPCORE.ConfigPvpCore;
import dev.arab.ADDONS.PVPCORE.DamageLimitListener;
import dev.arab.SZAFACORE.commands.AdminSzafaCommand;
import dev.arab.SZAFACORE.commands.SzafaCommand;
import dev.arab.SZAFACORE.listeners.AntiDupeListener;
import dev.arab.SZAFACORE.manager.ConfigManager;
import dev.arab.SZAFACORE.manager.CostumeManager;
import dev.arab.SZAFACORE.manager.ParrotManager;
import dev.arab.SZAFACORE.manager.PetManager;
import dev.arab.SZAFACORE.manager.PetVisibilityManager;
import dev.arab.SZAFACORE.manager.StatsManager;
import dev.arab.ADDONS.TRYB_TWORCY.commands.TrybTworcyCommand;
import dev.arab.ADDONS.TRYB_TWORCY.listeners.TrybTworcyListener;
import dev.arab.ADDONS.TRYB_TWORCY.managers.TrybTworcyManager;
import dev.arab.ADDONS.ZMIANKI.ConfigZaczarowania;
import dev.arab.ADDONS.ZMIANKI.ZaczarowanieListener;
import dev.arab.ADDONS.ZMIANKI.ZmiakaCommand;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public final class Main extends JavaPlugin {
    private WorldGuardHook worldGuardHook;
    private BlockTracker blockTracker;
    private CooldownManager cooldownManager;
    EventItemManager eventItemManager;
    private EquipmentCacheManager equipmentCacheManager;
    private ItemListener itemListener;
    private final Random random = new Random();
    private ConfigManager configManager;
    private PetManager petManager;
    private CostumeManager costumeManager;
    private ParrotManager parrotManager;
    private ConfigZaczarowania configZaczarowania;
    private BookManager bookManager;
    private IncognitoManager incognitoManager;
    private dev.arab.ADDONS.INCOGNITO.ConfigManager incognitoConfig;
    private TrybTworcyManager trybTworcyManager;
    private ModuleInventory moduleInventory;
    private ConfigPvpCore configPvpCore;
    private static Economy econ = null;
    private FileConfiguration messagesConfig;
    private File messagesFile;

    public void onEnable() {
        this.saveDefaultConfig();
        PetVisibilityManager.init(this);
        this.configManager = new ConfigManager(this);
        this.petManager = new PetManager(this);
        this.costumeManager = new CostumeManager(this);
        this.parrotManager = new ParrotManager(this);
        this.incognitoConfig = new dev.arab.ADDONS.INCOGNITO.ConfigManager(this);
        this.incognitoManager = new IncognitoManager(this, this.incognitoConfig);
        this.trybTworcyManager = new TrybTworcyManager(this);
        this.moduleInventory = new ModuleInventory(this);
        this.configPvpCore = new ConfigPvpCore(this);
        if (!this.setupEconomy()) {
            this.getLogger().warning("Nie znaleziono Vaulta!");
        }

        boolean wgEnabled = this.getServer().getPluginManager().getPlugin("WorldGuard") != null;
        this.worldGuardHook = new WorldGuardHook(wgEnabled);
        this.blockTracker = new BlockTracker(this);
        this.cooldownManager = new CooldownManager();
        this.eventItemManager = new EventItemManager(this);
        this.equipmentCacheManager = new EquipmentCacheManager(this);
        this.getServer().getPluginManager().registerEvents(this.equipmentCacheManager, this);
        this.configZaczarowania = new ConfigZaczarowania(this);
        this.bookManager = new BookManager(this);
        this.getServer().getPluginManager().registerEvents(new InventoryListener(this), this);
        this.getServer().getPluginManager().registerEvents(new dev.arab.SZAFACORE.listeners.InventoryListener(this), this);
        this.getServer().getPluginManager().registerEvents(this.petManager, this);
        this.getServer().getPluginManager().registerEvents(this.costumeManager, this);
        this.getServer().getPluginManager().registerEvents(this.parrotManager, this);
        this.itemListener = new ItemListener(this, this.worldGuardHook, this.blockTracker);
        this.getServer().getPluginManager().registerEvents(this.itemListener, this);
        this.getServer().getPluginManager().registerEvents(new AntiDupeListener(this), this);
        this.getServer().getPluginManager().registerEvents(new ZaczarowanieListener(this, this.configZaczarowania), this);
        this.getServer().getPluginManager().registerEvents(new BookListener(this, this.bookManager), this);
        if (this.getServer().getPluginManager().getPlugin("PlaceholderAPI") != null) {
            (new IncognitoExpansion(this, this.incognitoManager)).register();
        }

        this.getServer().getPluginManager().registerEvents(new TrybTworcyListener(this.trybTworcyManager), this);
        this.getServer().getPluginManager().registerEvents(new ModuleInventoryListener(this, this.moduleInventory), this);
        this.getServer().getPluginManager().registerEvents(new DamageLimitListener(this.configPvpCore), this);
        this.registerEventItems();
        AnarchiaEventowkiCommand anarchiaCmd = new AnarchiaEventowkiCommand(this);
        this.getCommand("anarchiaeventowki").setExecutor(anarchiaCmd);
        this.getCommand("anarchiaeventowki").setTabCompleter(anarchiaCmd);
        this.getCommand("excalibur").setExecutor(new ExcaliburCommand(this));
        this.getCommand("szafa").setExecutor(new SzafaCommand(this));
        AdminSzafaCommand adminCmd = new AdminSzafaCommand(this);
        this.getCommand("aszafa").setExecutor(adminCmd);
        this.getCommand("aszafa").setTabCompleter(adminCmd);
        ZmiakaCommand zmiakiCmd = new ZmiakaCommand(this, this.configZaczarowania);
        this.getCommand("azmianki").setExecutor(zmiakiCmd);
        this.getCommand("azmianki").setTabCompleter(zmiakiCmd);
        this.getCommand("ksiegi").setExecutor(new KsiegiCommand(this, this.bookManager));
        this.getCommand("incognito").setExecutor(new IncognitoCommand(this, this.incognitoManager));
        TrybTworcyCommand creatorCmd = new TrybTworcyCommand(this, this.trybTworcyManager);
        this.getCommand("trybtworcy").setExecutor(creatorCmd);
        this.getCommand("trybtworcy").setTabCompleter(creatorCmd);
        Iterator var6 = this.getServer().getOnlinePlayers().iterator();

        while(var6.hasNext()) {
            Player player = (Player)var6.next();
            StatsManager.updateStats(player);
        }

        (new ActionBarTask(this)).runTaskTimer(this, 1L, 5L);
        (new PassiveEffectTask(this)).runTaskTimer(this, 20L, 40L);
        (new BukkitRunnable() {
            private int tick = 0;

            public void run() {
                if (this.tick++ % 1200 == 0) {
                    Main.this.eventItemManager.updateAllInventories();
                }
            }
        }).runTaskTimer(this, 1L, 1L);
        this.loadMessages();
        this.getLogger().info("Plugin zostal pomyslnie wlaczony!");
    }

    public EventItemManager getEventItemManager() {
        return this.eventItemManager;
    }

    public ItemListener getItemListener() {
        return this.itemListener;
    }

    public Random getRandom() {
        return this.random;
    }

    public void loadMessages() {
        this.messagesFile = new File(this.getDataFolder(), "messages.yml");
        if (!this.messagesFile.exists()) {
            this.saveResource("messages.yml", false);
        }

        this.messagesConfig = YamlConfiguration.loadConfiguration(this.messagesFile);
    }

    public FileConfiguration getMessagesConfig() {
        if (this.messagesConfig == null) {
            this.loadMessages();
        }

        return this.messagesConfig;
    }

    public FileConfiguration getMessages() {
        return this.getMessagesConfig();
    }

    private void registerEventItems() {
        this.eventItemManager.registerItem(new CiepleMleko(this));
        this.eventItemManager.registerItem(new BombardaMaxina(this));
        this.eventItemManager.registerItem(new Piernik(this));
        this.eventItemManager.registerItem(new Sniezka(this));
        this.eventItemManager.registerItem(new TurboTrap(this));
        this.eventItemManager.registerItem(new BoskiTopor(this));
        this.eventItemManager.registerItem(new LeweJajko(this));
        this.eventItemManager.registerItem(new BalonikZHelem(this));
        this.eventItemManager.registerItem(new PrzeterminowanyTrunek(this));
        this.eventItemManager.registerItem(new Dynamit(this));
        this.eventItemManager.registerItem(new JajkoCreepera(this));
        this.eventItemManager.registerItem(new WataCukrowa(this));
        this.eventItemManager.registerItem(new RozdzkaIluzjonisty(this));
        this.eventItemManager.registerItem(new SakiewkaDropu(this));
        this.eventItemManager.registerItem(new KrewWampira(this));
        this.eventItemManager.registerItem(new Parawan(this));
        this.eventItemManager.registerItem(new RozaKupidyna(this));
        this.eventItemManager.registerItem(new TrojzabPosejdona(this));
        this.eventItemManager.registerItem(new WzmocnionaElytra(this));
        this.eventItemManager.registerItem(new WedkaSurferka(this));
        this.eventItemManager.registerItem(new WedkaNielota(this));
        this.eventItemManager.registerItem(new ZajeczyMiecz(this));
        this.eventItemManager.registerItem(new Excalibur(this));
        this.eventItemManager.registerItem(new CudownaLatarnia(this));
        this.eventItemManager.registerItem(new Rozga(this));
        this.eventItemManager.registerItem(new LopataGrincha(this));
        this.eventItemManager.registerItem(new Kosa(this));
        this.eventItemManager.registerItem(new MarchewkowyMiecz(this));
        this.eventItemManager.registerItem(new ZatrutyOlowek(this));
        this.eventItemManager.registerItem(new SplesnialaKanapka(this));
        this.eventItemManager.registerItem(new SiekieraGrincha(this));
        this.eventItemManager.registerItem(new KostkaRubika(this));
        this.eventItemManager.registerItem(new ZlamaneSerce(this));
        this.eventItemManager.registerItem(new PiekielnaTarcza(this));
        this.eventItemManager.registerItem(new MarchewkowaKusza(this));
        this.eventItemManager.registerItem(new TotemUlaskawienia(this));
        this.eventItemManager.registerItem(new RogJednorozca(this));
        this.eventItemManager.registerItem(new RozaKupidyna2026(this));
        this.eventItemManager.registerItem(new Lizak(this));
        this.eventItemManager.registerItem(new ArcusMagnus(this));
        this.eventItemManager.registerItem(new WampirzeJablko(this));
        this.eventItemManager.registerItem(new RozgotowanaKukurydza(this));
        this.eventItemManager.registerItem(new PiekielnyMiecz(this));
        this.eventItemManager.registerItem(new HydroKlatka(this));
        this.eventItemManager.registerItem(new BlokWidmo(this));
        this.eventItemManager.registerItem(new KoronaAnarchii(this));
        this.eventItemManager.registerItem(new KupaAnarchii(this));
        this.eventItemManager.registerItem(new SmoczyMiecz(this));
        this.eventItemManager.registerItem(new LukKupidyna(this));
    }

    public WorldGuardHook getWorldGuardHook() {
        return this.worldGuardHook;
    }

    public BlockTracker getBlockTracker() {
        return this.blockTracker;
    }

    public CooldownManager getCooldownManager() {
        return this.cooldownManager;
    }

    public ConfigManager getConfigManager() {
        return this.configManager;
    }

    public PetManager getPetManager() {
        return this.petManager;
    }

    public EquipmentCacheManager getEquipmentCacheManager() {
        return this.equipmentCacheManager;
    }

    public CostumeManager getCostumeManager() {
        return this.costumeManager;
    }

    public ParrotManager getParrotManager() {
        return this.parrotManager;
    }

    public BookManager getBookManager() {
        return this.bookManager;
    }

    public ModuleInventory getModuleInventory() {
        return this.moduleInventory;
    }

    public IncognitoManager getIncognitoManager() {
        return this.incognitoManager;
    }

    public TrybTworcyManager getTrybTworcyManager() {
        return this.trybTworcyManager;
    }

    public static Economy getEconomy() {
        return econ;
    }

    private boolean setupEconomy() {
        if (this.getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        } else {
            RegisteredServiceProvider<Economy> rsp = this.getServer().getServicesManager().getRegistration(Economy.class);
            if (rsp == null) {
                return false;
            } else {
                econ = (Economy)rsp.getProvider();
                return econ != null;
            }
        }
    }

    public void onDisable() {
        if (this.blockTracker != null) {
            this.blockTracker.save();
        }

        if (this.trybTworcyManager != null) {
            this.trybTworcyManager.savePlayers();
            this.trybTworcyManager.cleanup();
        }

        HydroKlatka.cleanupAll();
        BlokWidmo.cleanupAll();
        CudownaLatarnia.cleanupAll();
    }
}