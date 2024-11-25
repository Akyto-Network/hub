package akyto.hub.board.file;

import java.io.File;
import java.io.IOException;
import java.util.List;

import akyto.hub.Hub;
import org.bukkit.configuration.file.YamlConfiguration;

import com.google.common.collect.Lists;

import lombok.Getter;

public class ScoreboardFile {
	
	@Getter
	private Hub main;
	private YamlConfiguration config;
	private File file;
	private List<String> lines = Lists.newArrayList();
	private String name;
	
	public ScoreboardFile(final Hub main) {
		this.main = main;
		this.generate();
	}

	private void generate() {
		file = new File(this.main.getDataFolder(), "scoreboard.yml");
		if (!file.exists()) {            
			this.main.saveResource("scoreboard.yml", false);
		}
		config = YamlConfiguration.loadConfiguration(file);
		if (config.getKeys(true).size() > 2) {
			this.name = config.getString("title");
			for (String str : config.getConfigurationSection("lines").getKeys(false)) {
				this.lines.add(str.replace("%globalOnline%"));
			}
		}
		System.out.println("[ShimaHub] Scoreboards > Loaded");
	}

	public void save() {
		try {
			config.save(file);
		} 
		catch (IOException e) { e.printStackTrace(); }
	}
}
