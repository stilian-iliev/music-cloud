package com.musicloud.models.dtos.cloudinary;

import java.util.List;

public class CloudinaryResponse {
    private List<CloudinaryEntry> resources;

    public List<CloudinaryEntry> getResources() {
        return resources;
    }

    public void setResources(List<CloudinaryEntry> resources) {
        this.resources = resources;
    }
}
