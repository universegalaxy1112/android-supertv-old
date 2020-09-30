/*
package com.livetv.normal.view;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v17.leanback.app.BackgroundManager;
import android.support.v17.leanback.app.RowsSupportFragment;
import android.support.v17.leanback.widget.ArrayObjectAdapter;
import android.support.v17.leanback.widget.HeaderItem;
import android.support.v17.leanback.widget.ListRow;
import android.support.v17.leanback.widget.ListRowPresenter;
import android.support.v17.leanback.widget.OnItemViewClickedListener;
import android.support.v17.leanback.widget.PageRow;
import android.support.v17.leanback.widget.Presenter;
import android.support.v17.leanback.widget.Presenter.ViewHolder;
import android.support.v17.leanback.widget.Row;
import android.support.v17.leanback.widget.RowPresenter;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;

import com.livetv.normal.R;
import com.livetv.normal.managers.VideoStreamManager;
import com.livetv.normal.model.LiveProgram;
import com.livetv.normal.model.LiveTVCategory;
import com.livetv.normal.view.adapters.LiveProgramHeaderPresenter;
import com.livetv.normal.viewmodel.Lifecycle;
import com.livetv.normal.viewmodel.LiveTVViewModel;
import com.livetv.normal.viewmodel.LiveTVViewModelContract;

import java.util.List;

public class LiveTVPlaybackFragment extends BaseTVFragment implements LiveTVViewModelContract.View {
    private LiveTVViewModel liveTVViewModel;
    private ArrayObjectAdapter mRowsAdapter;

    private final class ItemViewClickedListener implements OnItemViewClickedListener {
        private ItemViewClickedListener() {
        }

        public void onItemClicked(ViewHolder itemViewHolder, Object item, RowPresenter.ViewHolder rowViewHolder, Row row) {
            Log.d("DNLS", "onItemClicked");
            if (item instanceof LiveProgram) {
                ((LiveTVActivity) LiveTVPlaybackFragment.this.getActivity()).onLiveProgramSelected((LiveProgram) item, 0);
            }
        }
    }

    public static class LiveTVProgramsFragment extends RowsSupportFragment {

        */
/* renamed from: id *//*

        private int f95id;
        private ArrayObjectAdapter mRowsAdapter;

        public LiveTVProgramsFragment() {
            this.mRowsAdapter = null;
            this.f95id = 0;
        }

        @SuppressLint({"ValidFragment"})
        public LiveTVProgramsFragment(int anId) {
            this.mRowsAdapter = null;
            this.f95id = 0;
            this.mRowsAdapter = new ArrayObjectAdapter((Presenter) new ListRowPresenter());
            this.f95id = anId;
            setAdapter(this.mRowsAdapter);
            setOnItemViewClickedListener(new OnItemViewClickedListener() {
                public void onItemClicked(ViewHolder itemViewHolder, Object item, RowPresenter.ViewHolder rowViewHolder, Row row) {
                    if (item instanceof LiveProgram) {
                        ((LiveTVActivity) LiveTVProgramsFragment.this.getActivity()).onLiveProgramSelected((LiveProgram) item, 0);
                    }
                }
            });
        }

        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            createRows();
            getMainFragmentAdapter().getFragmentHost().notifyDataReady(getMainFragmentAdapter());
        }

        private void createRows() {
            List<LiveProgram> liveProgramList;
            Log.d("DNLS", "id: " + this.f95id);
            if (this.f95id == -1) {
                liveProgramList = VideoStreamManager.getInstance().getAllLivePrograms();
            } else {
                LiveTVCategory aCat = VideoStreamManager.getInstance().getLiveTVCategory(this.f95id);
                Log.d("DNLS", "category: " + aCat.getCatName());
                liveProgramList = aCat.getLivePrograms();
            }
            try {
                for (LiveProgram liveProgram : liveProgramList) {
                    if (!liveProgram.getTitle().equals("empty category")) {
                        this.mRowsAdapter.add(createRow(liveProgram));
                    }
                }
            } catch (NullPointerException e) {
            }
        }

        private Row createRow(LiveProgram liveProgram) {
            HeaderItem header = new HeaderItem((long) liveProgram.getContentId(), liveProgram.getTitle());
            ArrayObjectAdapter adapter = new ArrayObjectAdapter((Presenter) new LiveProgramHeaderPresenter(getActivity().getApplicationContext()));
            adapter.add(liveProgram);
            ListRow r = new ListRow(header, adapter);
            r.setId((long) liveProgram.getContentId());
            return r;
        }
    }

    private static class PageRowFragmentFactory extends FragmentFactory {
        private PageRowFragmentFactory() {
        }

        public Fragment createFragment(Object rowObj) {
            return new LiveTVProgramsFragment((int) ((Row) rowObj).getHeaderItem().getId());
        }
    }

    */
/* access modifiers changed from: protected *//*

    public LiveTVViewModelContract.View getLifecycleView() {
        return null;
    }

    */
/* access modifiers changed from: protected *//*

    public void onConfigurationChanged() {
    }

    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.liveTVViewModel = new LiveTVViewModel(getActivity().getApplicationContext());
        BackgroundManager mBackgroundManager = BackgroundManager.getInstance(getActivity());
        mBackgroundManager.attach(getActivity().getWindow());
        mBackgroundManager.setColor(getResources().getColor(R.color.colorBackgroundLight));
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(new DisplayMetrics());
        setHeadersState(1);
        setHeadersTransitionOnBackEnabled(true);
        mBackgroundManager.setColor(0);
        setBrandColor(getResources().getColor(R.color.colorPrimaryDarkAlpha));
        this.mRowsAdapter = new ArrayObjectAdapter((Presenter) new ListRowPresenter());
        setAdapter(this.mRowsAdapter);
        new Handler().postDelayed(new Runnable() {
            public void run() {
                LiveTVPlaybackFragment.this.loadData();
                LiveTVPlaybackFragment.this.startEntranceTransition();
            }
        }, 2000);
        setOnItemViewClickedListener(new ItemViewClickedListener());
        getMainFragmentRegistry().registerFragment(PageRow.class, new PageRowFragmentFactory());
    }

    */
/* access modifiers changed from: private *//*

    public void loadData() {
        List<LiveTVCategory> categoryList = VideoStreamManager.getInstance().getLiveTVCategoriesList();
        LiveTVCategory all = new LiveTVCategory();
        all.setCatName("Todos");
        all.setPosition(-1);
        all.setId(-1);
        this.mRowsAdapter.add(new PageRow(new HeaderItem((long) all.getPosition(), all.getCatName())));
        for (LiveTVCategory category : categoryList) {
            Log.d("DNLS", "category position:" + category.getPosition() + ", category name: " + category.getCatName());
            this.mRowsAdapter.add(new PageRow(new HeaderItem((long) category.getPosition(), category.getCatName())));
        }
    }

    public void onProgramAccepted(LiveProgram liveProgram) {
    }

    public void showActionBar() {
    }

    public void hideActionBar() {
    }

    */
/* access modifiers changed from: protected *//*

    public Lifecycle.ViewModel getViewModel() {
        return this.liveTVViewModel;
    }
}
*/
