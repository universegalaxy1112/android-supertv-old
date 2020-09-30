package com.livetv.normal.viewmodel;

import com.livetv.normal.databinding.ActivityLivetvnewBinding;
import com.livetv.normal.model.LiveProgram;

public interface LiveTVViewModelContract {

    public interface View extends Lifecycle.View {

        void onProgramAccepted(LiveProgram liveProgram);

    }

    public interface ViewModel extends Lifecycle.ViewModel {

        void showProgramList(ActivityLivetvnewBinding activityLiveBinding);

    }
}
