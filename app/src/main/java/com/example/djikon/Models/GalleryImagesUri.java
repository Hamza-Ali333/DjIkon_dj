package com.example.djikon.Models;

import android.net.Uri;

public class GalleryImagesUri {
    Uri uri;

    public GalleryImagesUri(Uri uri) {
        this.uri = uri;
    }

    public Uri getUri() {
        return uri;
    }

    public void setUri(Uri uri) {
        this.uri = uri;
    }
}
