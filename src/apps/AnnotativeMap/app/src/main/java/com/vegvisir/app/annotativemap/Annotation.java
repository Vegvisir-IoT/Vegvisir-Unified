package com.vegvisir.app.annotativemap;

public class Annotation {
    private String annotation = "";
    private PictureTagLayout layout = null;
    private Boolean shouldRemove = false;
    private Boolean alreadyAdded = false;

    public Annotation (String anno, PictureTagLayout l) {
        annotation = anno;
        layout = l;
    }

    public String getAnnotation() {
        return annotation;
    }

    public PictureTagLayout getLayout() {
        return layout;
    }

    public Boolean getShouldRemove() {
        return shouldRemove;
    }

    public Boolean getAlreadyAdded() {
        return alreadyAdded;
    }

    public void setAnnotation(String anno) {
        annotation = anno;
    }

    public void setShouldRemove(Boolean flag) {
        shouldRemove = flag;
    }

    public void setAlreadyAdded(Boolean aa) {
        alreadyAdded = aa;
    }


}
