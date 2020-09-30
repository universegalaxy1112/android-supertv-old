package com.livetv.normal.view.adapters;

import android.support.v17.leanback.widget.ArrayObjectAdapter;
import android.support.v17.leanback.widget.Presenter;
import android.support.v17.leanback.widget.PresenterSelector;
import java.util.Comparator;
import java.util.TreeSet;

public class SortedArrayObjectAdapter extends ArrayObjectAdapter {
    private TreeSet<Object> mSortedItems;

    public SortedArrayObjectAdapter(Comparator comparator, PresenterSelector presenterSelector) {
        super(presenterSelector);
        this.mSortedItems = new TreeSet<>(comparator);
    }

    public SortedArrayObjectAdapter(Comparator comparator, Presenter presenter) {
        super(presenter);
        this.mSortedItems = new TreeSet<>(comparator);
    }

    public SortedArrayObjectAdapter(Comparator comparator) {
        this.mSortedItems = new TreeSet<>(comparator);
    }

    public void add(Object item) {
        this.mSortedItems.add(item);
        super.add(this.mSortedItems.headSet(item).size(), item);
    }

    public boolean remove(Object item) {
        this.mSortedItems.remove(item);
        return super.remove(item);
    }

    public void clear() {
        this.mSortedItems.clear();
        super.clear();
    }
}
