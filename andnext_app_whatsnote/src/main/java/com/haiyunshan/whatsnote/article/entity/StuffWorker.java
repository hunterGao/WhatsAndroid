package com.haiyunshan.whatsnote.article.entity;

import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;
import club.andnext.utils.ContentUtils;

import java.io.File;

class StuffWorker {

    Context context;

    StuffWorker(Context context) {
        this.context = context.getApplicationContext();
    }

    void add(DocumentEntity entity) {
        if (entity instanceof PictureEntity) {
            this.addPicture((PictureEntity)entity);
        }
    }

    void remove(DocumentEntity entity) {
        File file = getManager().getFile(entity);
        if (file == null) {
            return;
        }

        remove(file);
    }

    void addPicture(PictureEntity entity) {
        File file = getManager().getFile(entity);
        if (file.exists()) {
            return;
        }

        String uriString = entity.getEntry().getUri();
        if (TextUtils.isEmpty(uriString)) {
            return;
        }

        Uri uri = Uri.parse(uriString);
        this.copy(uri, file);
    }

    void remove(File file) {
        if (file.exists()) {
            file.delete();
        }

        file = getTempFile(file);
        if (file.exists()) {
            file.delete();
        }
    }

    void copy(Uri uri, File file) {

        file.getParentFile().mkdirs();
        File tmpFile = getTempFile(file);
        tmpFile = ContentUtils.copy(context, uri, tmpFile);
        if (tmpFile != null) {
            tmpFile.renameTo(file);
        }
    }

    File getTempFile(File file) {
        return new File(file.getAbsolutePath() + ".tmp");
    }

    DocumentManager getManager() {
        return DocumentManager.getInstance(context);
    }

}
