package com.sample.persistence.microstream.repository.storage;

import com.sample.persistence.microstream.domain.DataRoot;
import lombok.Getter;
import one.microstream.storage.embedded.types.EmbeddedStorage;
import one.microstream.storage.embedded.types.EmbeddedStorageManager;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.nio.file.Paths;

@Component
@Getter
public class StorageManager {

	private EmbeddedStorageManager storageManager;

	@PostConstruct
	void initialize() {
		storageManager = EmbeddedStorage.start(Paths.get("data"));
	}

	public DataRoot getRoot() {
		if (storageManager.root() == null) {
			return null;
		}
		return (DataRoot) storageManager.root();
	}

}
