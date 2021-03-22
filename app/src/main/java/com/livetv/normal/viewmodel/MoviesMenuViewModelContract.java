package com.livetv.normal.viewmodel;

import androidx.recyclerview.widget.RecyclerView;
import com.livetv.normal.model.Movie;
import com.livetv.normal.model.Serie;

public interface MoviesMenuViewModelContract {

    public interface View extends Lifecycle.View {
        void onMovieAccepted(Movie movie);

        void onSearchSelected(boolean z);

        void onSerieAccepted(int i, Serie serie);

        void onShowAsGridSelected(Integer num);
    }

    public interface ViewModel extends Lifecycle.ViewModel {
        void showEpisodeLists(RecyclerView recyclerView, int i, int i2, int i3);

        void showMovieLists(RecyclerView recyclerView, int i);
    }
}
