// Copyright (C) (2019) (Mathieu Bergeron) (mathieu.bergeron@cmontmorency.qc.ca)
//
// This file is part of aquiletour
//
// aquiletour is free software: you can redistribute it and/or modify
// it under the terms of the GNU Affero General Public License as published by
// the Free Software Foundation, either version 3 of the License, or
// (at your option) any later version.
//
// aquiletour is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU Affero General Public License for more details.
//
// You should have received a copy of the GNU Affero General Public License
// along with aquiletour.  If not, see <https://www.gnu.org/licenses/>


package ca.aquiletour.filewatcher;

import java.io.IOException;

import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;

public class Watcher extends Thread {

	private WatchService watcher;
	private Path pathToWatch;
	
	private OnDeleteListener onDeleteListener;
	private OnModifyListener onModifyListener;

	public Watcher(Path pathToWatch) {

		this.pathToWatch = pathToWatch;

		try {

			// see : https://docs.oracle.com/javase/tutorial/essential/io/notification.html
			watcher = FileSystems.getDefault().newWatchService();

			pathToWatch.register(watcher, StandardWatchEventKinds.ENTRY_CREATE,
					StandardWatchEventKinds.ENTRY_DELETE, StandardWatchEventKinds.ENTRY_MODIFY);

		} catch (IOException e) {

			System.out.println("[WARNING] unable to watch dir " + pathToWatch);
			e.printStackTrace();

		}
	}
	
	public void setOnDeleteListener(OnDeleteListener onDeleteListener) {
		this.onDeleteListener = onDeleteListener;
	}

	public void setOnModifyListener(OnModifyListener onModifyListener) {
		this.onModifyListener = onModifyListener;
	}


	@Override
	public void run() {
		if (watcher != null) {
			System.out.println("FileWatcher running on: " + pathToWatch);
			watcherLoop();
		}
	}

	private void watcherLoop() {
		while (true) {
			WatchKey key;
			
			try {
				key = watcher.take();
			} catch (InterruptedException e1) {
				break;
			}

			reactToEachEvent(key);

			boolean valid = key.reset();
			if (!valid) {
				break;
			}
		}
	}

	
	@SuppressWarnings("unchecked")
	private void reactToEachEvent(WatchKey key) {
		for (WatchEvent<?> event : key.pollEvents()) {

			WatchEvent.Kind<?> kind = event.kind();

			if (kind == StandardWatchEventKinds.OVERFLOW) {
				continue;
			}

			WatchEvent<Path> ev = (WatchEvent<Path>) event;
			Path filename = ev.context();

			if (kind == StandardWatchEventKinds.ENTRY_CREATE) {
			} else if (kind == StandardWatchEventKinds.ENTRY_MODIFY) {

				onEntryModified(filename);

			} else if (kind == StandardWatchEventKinds.ENTRY_DELETE) {

				onEntryDeleted(filename);
			} 
		}
	}

	private void onEntryDeleted(Path filename) {
		if(onDeleteListener != null) {
			onDeleteListener.onDelete(filename.toString());
		}
	}

	protected void onEntryModified(Path filename) {
		if(onModifyListener != null) {
			onModifyListener.onModify(filename.toString());
		}
	}
	
}