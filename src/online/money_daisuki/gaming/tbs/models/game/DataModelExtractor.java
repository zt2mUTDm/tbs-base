package online.money_daisuki.gaming.tbs.models.game;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;

import online.money_daisuki.api.base.Converter;
import online.money_daisuki.api.base.DataSink;
import online.money_daisuki.api.base.NullDataSink;
import online.money_daisuki.api.io.FileDeleteSink;
import online.money_daisuki.api.io.FileIteratorRunnable;
import online.money_daisuki.api.io.ZipFileStructurExtractor;
import online.money_daisuki.api.io.json.JsonDecoder;
import online.money_daisuki.gaming.tbs.models.data.DataModel;
import online.money_daisuki.gaming.tbs.models.data.DataModelLoader;

public final class DataModelExtractor implements Converter<String, ExtractedDataModel> {
	private final Map<String, File> dataModelTargetFolders;
	private final Map<String, ExtractedDataModel> loadedDataModels;
	
	public DataModelExtractor() {
		dataModelTargetFolders = new HashMap<>();
		loadedDataModels = new WeakHashMap<>();
	}
	@Override
	public ExtractedDataModel convert(final String name) {
		ExtractedDataModel exModel = loadedDataModels.get(name);
		if(exModel != null) {
			return(exModel);
		}
		
		File dataFolder = dataModelTargetFolders.get(name);
		if(dataFolder == null) {
			dataFolder = extractPack(name);
			dataModelTargetFolders.put(name, dataFolder);
		}
		
		try(final Reader in = new FileReader(new File(dataFolder, "data.json"))) {
			final DataModel model = new DataModelLoader(new JsonDecoder(in).decode().asMap()).source();
			exModel = new ExtractedDataModel(model, dataFolder);
			loadedDataModels.put(name, exModel);
			return(exModel);
		} catch (final IOException e) {
			throw new RuntimeException(e);
		}
	}
	private File extractPack(final String file) { 
		File targetDir = null;
		try {
			targetDir = Files.createTempDirectory("tmp.").toFile();
			
			try(final InputStream in = new FileInputStream(new File("packs", file))) {
				new ZipFileStructurExtractor(in, targetDir).run();
				return(targetDir);
			}
		} catch(final Throwable t) {
			if(targetDir != null)  {
				deleteFileRecursive(targetDir);
			}
			throw new RuntimeException(t);
		}
	}
	
	public void dispose() {
		for(final File f:dataModelTargetFolders.values()) {
			deleteFileRecursive(f);
		}
		dataModelTargetFolders.clear();
		
		loadedDataModels.clear();
	}
	private void deleteFileRecursive(final File f) {
		final DataSink<File> deleteSink = new FileDeleteSink();
		final DataSink<Object> nullSink = new NullDataSink<>();
		
		new FileIteratorRunnable(f, deleteSink, nullSink, deleteSink, deleteSink).run();
		deleteSink.sink(f);
	}
	
}
