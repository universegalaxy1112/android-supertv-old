/*
package com.livetv.normal.view;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.livetv.normal.R;
import com.livetv.normal.databinding.LiveTvFragmentBinding;
import com.livetv.normal.listeners.LiveProgramSelectedListener;
import com.livetv.normal.model.LiveProgram;
import com.livetv.normal.utils.Screen;
import com.livetv.normal.viewmodel.Lifecycle;
import com.livetv.normal.viewmodel.LiveTVViewModel;
import com.livetv.normal.viewmodel.LiveTVViewModelContract;

public class LiveTVFragment extends BaseFragment implements LiveTVViewModelContract.View {
    private LiveProgramSelectedListener liveProgramSelectedListener;
    private LiveTvFragmentBinding liveTVFragmentBinding;
    */
/* access modifiers changed from: private *//*

    public LiveTVViewModel liveTVViewModel;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        Screen.SetScreenDimensions(getActivity());
        this.liveTVViewModel = new LiveTVViewModel(getContext());
    }

    public void onAttach(Context context) {
        super.onAttach(context);
        this.liveProgramSelectedListener = (LiveProgramSelectedListener) getActivity();
    }

    */
/* access modifiers changed from: protected *//*

    public void onConfigurationChanged() {
        this.liveTVViewModel.onConfigurationChanged();
    }

    public android.view.View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.liveTVFragmentBinding = (LiveTvFragmentBinding) DataBindingUtil.inflate(inflater, R.layout.live_tv_fragment, container, false);
        this.liveTVFragmentBinding.setLiveTVFragmentVM((LiveTVViewModel) getViewModel());
        this.liveTVViewModel.showProgramList(this.liveTVFragmentBinding.listContainer, this.liveTVFragmentBinding.liveTvRecyclerview, this.liveTVFragmentBinding.categoriesRecyclerview, this.liveTVFragmentBinding.categoriesRecyclerviewContainer);
        getActivity().findViewById(R.id.category_options).setOnClickListener(new OnClickListener() {
            public void onClick(android.view.View v) {
                LiveTVFragment.this.liveTVViewModel.toggleCategoryOption();
            }
        });
        return this.liveTVFragmentBinding.getRoot();
    }

    */
/* access modifiers changed from: protected *//*

    public Lifecycle.ViewModel getViewModel() {
        return this.liveTVViewModel;
    }

    */
/* access modifiers changed from: protected *//*

    public Lifecycle.View getLifecycleView() {
        return this;
    }

    */
/* access modifiers changed from: protected *//*

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode != 4) {
            return false;
        }
        if (this.liveTVViewModel.isCategoriesMenuOpen()) {
            finishActivity();
        } else if (this.liveTVViewModel.isProgramsListOpen()) {
            this.liveTVViewModel.toggleCategoryOption();
        } else {
            this.liveTVViewModel.toggleChannels();
        }
        return true;
    }

    public void onProgramAccepted(LiveProgram liveProgram) {
        Screen.SetScreenDimensions(getActivity());
        this.liveProgramSelectedListener.onLiveProgramSelected(liveProgram, 0);
    }

    public void showActionBar() {
        if (getActivity().findViewById(R.id.live_tv_action_bar) != null) {
            getActivity().findViewById(R.id.live_tv_action_bar).setVisibility(android.view.View.VISIBLE);
        }
    }

    public void hideActionBar() {
        if (getActivity().findViewById(R.id.live_tv_action_bar) != null) {
            getActivity().findViewById(R.id.live_tv_action_bar).setVisibility(android.view.View.GONE);
        }
    }

    public void showChannels() {
        this.liveTVViewModel.showChannels();
    }

    public void hideChannels() {
        this.liveTVViewModel.hideChannels();
    }

    public void toggleChannels() {
        this.liveTVViewModel.toggleChannels();
    }

    public void toggleCategoryOption() {
        this.liveTVViewModel.toggleCategoryOption();
    }
}
*/
