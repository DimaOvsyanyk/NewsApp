package com.dimaoprog.newsapiapp.view.sources;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableBoolean;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.dimaoprog.newsapiapp.data.PrefsRepository;
import com.dimaoprog.newsapiapp.data.RoomRepository;
import com.dimaoprog.newsapiapp.models.Source;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

import static com.dimaoprog.newsapiapp.utils.Constants.COLLECTION_NAME;

public class SourcesViewModel extends AndroidViewModel {

    private RoomRepository roomRepository;
    private PrefsRepository prefsRepository;
    private ObservableBoolean processing = new ObservableBoolean();
    private CompositeDisposable disposable = new CompositeDisposable();

    private LiveData<List<Source>> sources;

    private FirebaseUser currentUser;
    private FirebaseFirestore db;

    public SourcesViewModel(@NonNull Application application) {
        super(application);
        prefsRepository = PrefsRepository.getInstance(application);
        roomRepository = RoomRepository.getInstance(application);
        sources = roomRepository.getSourceList();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        db = FirebaseFirestore.getInstance();

    }

    public void makeChangesInSources(Source source) {
        disposable.add(roomRepository.update(source)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(t -> changePrefsAndFirebase(),
                        onError -> Log.d("room response", onError.getMessage())
                ));

    }

    private void changePrefsAndFirebase() {
        disposable.add(roomRepository.getSelectedSourcesString()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(sourceListString -> {
                            prefsRepository.setSelectedSourceList(sourceListString);
                            db.collection(COLLECTION_NAME).document(currentUser.getUid()).update("selectedSources", sourceListString);
                        },
                        onError -> Log.d("room response", onError.getMessage())
                ));
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        disposable.clear();
    }

    public LiveData<List<Source>> getSources() {
        return sources;
    }

    public void setSources(LiveData<List<Source>> sources) {
        this.sources = sources;
    }

    public ObservableBoolean getProcessing() {
        return processing;
    }

    public void setProcessing(boolean processing) {
        this.processing.set(processing);
    }
}
