package com.dimaoprog.newsapiapp.view.loginRegistration;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableBoolean;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.dimaoprog.newsapiapp.data.PrefsRepository;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import static com.dimaoprog.newsapiapp.utils.Constants.COLLECTION_NAME;
import static com.dimaoprog.newsapiapp.utils.Constants.EMAIL_REG_EXP;

public class LoginViewModel extends AndroidViewModel {

    private String email = "";
    private String password = "";
    private ObservableBoolean processing = new ObservableBoolean();

    private MutableLiveData<String> toastMessage = new MutableLiveData<>();
    private MutableLiveData<Boolean> emailOk = new MutableLiveData<>();
    private MutableLiveData<Boolean> passwordOk = new MutableLiveData<>();
    private MutableLiveData<Boolean> signInCompleted = new MutableLiveData<>();

    private PrefsRepository prefsRepository;
    private FirebaseAuth auth;
    private FirebaseFirestore db;

    public LoginViewModel(@NonNull Application application) {
        super(application);
        auth = FirebaseAuth.getInstance();
        prefsRepository = PrefsRepository.getInstance(application);
        db = FirebaseFirestore.getInstance();
    }

    public void signIn() {
        if (inputFalse()) {
            return;
        }
        setProcessing(true);
        auth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener(authResult -> {
                    if (authResult.getUser().isEmailVerified()) {
                        checkSelectedResourcesCity();
                        setSignInCompleted(true);
                    } else {
                        setToastMessage("Please verify your e-mail");
                    }
                    setProcessing(false);
                })
                .addOnFailureListener(e -> {
                    setToastMessage(e.getLocalizedMessage());
                    Log.d("firebase", "signInWithEmail:failure " + e.getMessage());
                    setProcessing(false);
                });
    }

    private void checkSelectedResourcesCity() {
        DocumentReference docRef = db.collection(COLLECTION_NAME).document(auth.getCurrentUser().getUid());
        docRef.get()
                .addOnSuccessListener(documentSnapshot -> {
                    String selectedSourcesDB = documentSnapshot.get("selectedSources").toString();
                    String userCity = documentSnapshot.get("city").toString();
                    if (!selectedSourcesDB.equals(prefsRepository.getSelectedSourceList())) {
                        prefsRepository.setSelectedSourceList(selectedSourcesDB);
                    }
                    prefsRepository.setUserCity(userCity);
                })
                .addOnFailureListener(e -> Log.d("firebase", "checkPrefs:failure " + e.getMessage()));
    }

    private boolean inputFalse() {
        return !(setEmailOk() & setPasswordOk());
    }

    public MutableLiveData<Boolean> getEmailOk() {
        return emailOk;
    }

    private boolean setEmailOk() {
        this.emailOk.setValue(email != null && !email.isEmpty() && email.matches(EMAIL_REG_EXP));
        return email.matches(EMAIL_REG_EXP);
    }

    public MutableLiveData<Boolean> getPasswordOk() {
        return passwordOk;
    }

    private boolean setPasswordOk() {
        this.passwordOk.setValue(password != null && !password.isEmpty() && password.length() > 5);
        return !password.isEmpty() && password.length() > 5;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public ObservableBoolean getProcessing() {
        return processing;
    }

    public void setProcessing(boolean processing) {
        this.processing.set(processing);
    }

    public MutableLiveData<String> getToastMessage() {
        return toastMessage;
    }

    public void setToastMessage(String toastMessage) {
        this.toastMessage.setValue(toastMessage);
    }

    public MutableLiveData<Boolean> getSignInCompleted() {
        return signInCompleted;
    }

    public void setSignInCompleted(boolean signInCompleted) {
        this.signInCompleted.setValue(signInCompleted);
    }
}
