package com.livetv.normal.viewmodel;

import android.support.annotation.NonNull;

public interface Lifecycle {

    public interface View {
    }

    public interface ViewModel {
        void onViewAttached(@NonNull View view);

        void onViewDetached();

        void onViewResumed();
    }
}
