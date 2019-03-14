package com.sv.common.databinding;

/**
 * @author sven-ou
 */
public abstract class DataBindingObject<T> {
    private T model;

    public DataBindingObject(T model) {
        this.model = model;
    }

    public void triggerSaveModel() {
        onSaveModel(model);
    };

    public void triggerRefreshUI() {
        onRefreshUI(model);
    };

    protected abstract void onSaveModel(T t);

    protected abstract void onRefreshUI(T t);

    public T getModel() {
        return model;
    }

    public void setModel(T model) {
        this.model = model;
    }
}
