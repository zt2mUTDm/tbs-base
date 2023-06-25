package online.money_daisuki.gaming.tbs.models.game;

import online.money_daisuki.api.utils.Reinstaller;
import online.money_daisuki.api.utils.Uninstaller;

public final class OnceInstallerModel {
	private Uninstaller uninstall;
	
	public OnceInstallerModel() {
		uninstall = new NullReinstaller();
	}
	public void install(final Reinstaller install) {
		final Uninstaller old = uninstall;
		uninstall = install;
		
		old.uninstall();
		install.install();
	}
	public void uninstall() {
		final Uninstaller old = uninstall;
		uninstall = new NullReinstaller();
		
		old.uninstall();
	}
}
