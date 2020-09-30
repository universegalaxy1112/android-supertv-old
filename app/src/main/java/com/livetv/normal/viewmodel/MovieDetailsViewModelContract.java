package com.livetv.normal.viewmodel;

import com.livetv.normal.databinding.MovieDetailsFragmentBinding;
import com.livetv.normal.listeners.OptionsMenuOpenListener;
import com.livetv.normal.model.Movie;

public interface MovieDetailsViewModelContract {

    public interface View extends Lifecycle.View {
        void onPlaySelected(Movie movie, boolean z);
    }

    public interface ViewModel extends Lifecycle.ViewModel {
        void showMovieDetails(Movie movie, MovieDetailsFragmentBinding movieDetailsFragmentBinding, OptionsMenuOpenListener optionsMenuOpenListener, int i);
    }
}
