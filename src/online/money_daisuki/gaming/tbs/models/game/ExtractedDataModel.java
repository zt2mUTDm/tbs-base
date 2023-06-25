package online.money_daisuki.gaming.tbs.models.game;

import java.io.File;

import online.money_daisuki.api.base.Requires;
import online.money_daisuki.gaming.tbs.models.data.DataModel;

public final class ExtractedDataModel {
	private final DataModel model;
	private final File dataFolder;
	
	public ExtractedDataModel(final DataModel model, final File dataFolder) {
		this.model = Requires.notNull(model, "model == null");
		this.dataFolder = Requires.notNull(dataFolder, "dataFolder == null");
	}
	public DataModel getModel() {
		return (model);
	}
	public File getDataFolder() {
		return (dataFolder);
	}
}
