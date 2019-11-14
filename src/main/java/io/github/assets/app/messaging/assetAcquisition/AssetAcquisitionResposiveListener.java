package io.github.assets.app.messaging.assetAcquisition;

import io.github.assets.app.messaging.ResponsiveListener;
import io.github.assets.app.messaging.fileNotification.FileNotification;

/**
 * This is intended to listen on FileUploads for which relate to asset acquisitions
 * and to deserialize that data and send it back to stream.
 */
public class AssetAcquisitionResposiveListener implements ResponsiveListener<FileNotification, AssetAcquisitionMTO> {

    @Override
    public AssetAcquisitionMTO attendMessage(final FileNotification fileNotification) {
        return null;
    }
}
