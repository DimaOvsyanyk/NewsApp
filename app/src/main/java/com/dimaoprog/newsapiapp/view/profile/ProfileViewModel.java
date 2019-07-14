package com.dimaoprog.newsapiapp.view.profile;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableBoolean;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.dimaoprog.newsapiapp.data.PrefsRepository;
import com.dimaoprog.newsapiapp.models.UserDetails;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import static com.dimaoprog.newsapiapp.utils.Constants.COLLECTION_NAME;

public class ProfileViewModel extends AndroidViewModel {

    private String oldPassword = "";
    private String newPassword = "";
    private String firstName;
    private String secondName;
    private String telNumber;
    private String country;
    private String city;

    private PrefsRepository prefsRepository;
    private ObservableBoolean processing = new ObservableBoolean();
    private MutableLiveData<String> toastMessage = new MutableLiveData<>();
    private MutableLiveData<Boolean> changesSaved = new MutableLiveData<>();

    private MutableLiveData<Boolean> firstDataLoaded = new MutableLiveData<>();
    private MutableLiveData<Boolean> oldPassOk = new MutableLiveData<>();
    private MutableLiveData<Boolean> newPassOk = new MutableLiveData<>();
    private MutableLiveData<Boolean> firstNameOk = new MutableLiveData<>();
    private MutableLiveData<Boolean> secondNameOk = new MutableLiveData<>();
    private MutableLiveData<Boolean> telNumberOk = new MutableLiveData<>();

    private FirebaseAuth auth;
    private FirebaseUser currentUser;
    private UserDetails currentUserDetails;
    private FirebaseFirestore db;

    public ProfileViewModel(@NonNull Application application) {
        super(application);
        prefsRepository = PrefsRepository.getInstance(application);
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        currentUser = auth.getCurrentUser();
        getDetails();
    }

    private void getDetails() {
        setProcessing(true);
        DocumentReference docRef = db.collection(COLLECTION_NAME).document(currentUser.getUid());
        docRef.get().addOnSuccessListener(documentSnapshot -> {
            currentUserDetails = documentSnapshot.toObject(UserDetails.class);
            populateUserDetails();
            setProcessing(false);
        })
        .addOnFailureListener(e -> {
            setProcessing(false);
            setToastMessage(e.getLocalizedMessage());
        });
    }

    private void populateUserDetails() {
        Log.d("firebase", currentUserDetails.toString());
        firstName = currentUserDetails.getFirstName();
        secondName = currentUserDetails.getSecondName();
        telNumber = currentUserDetails.getTelNumber();
        country = currentUserDetails.getCountry();
        city = currentUserDetails.getCity();
        setFirstDataLoaded(true);
    }

    public void saveChanges() {
        if (inputFalse()) {
            return;
        }
        if (newPassword.length() > 1) {
            if (!setNewPassOk()){
                return;
            }
            setProcessing(true);
            changePassword();
        } else {
            setProcessing(true);
            postNewDetails();
        }
    }

    private void changePassword() {
        Log.d("firebase", "changing pass");
        AuthCredential credential = EmailAuthProvider.getCredential(currentUser.getEmail(), oldPassword);
        currentUser.reauthenticate(credential)
                .addOnSuccessListener(onSuccess1 -> currentUser.updatePassword(newPassword)
                        .addOnSuccessListener(onSuccess2 -> {
                            currentUser.updateProfile(getUserNameChangeReq())
                                    .addOnFailureListener(e -> setToastMessage(e.getLocalizedMessage()));
                            postNewDetails();
                        })
                        .addOnFailureListener(e ->{
                            setProcessing(false);
                            setToastMessage(e.getLocalizedMessage());
                        }))
                .addOnFailureListener(e ->{
                    setProcessing(false);
                    setToastMessage(e.getLocalizedMessage());
                });
    }

    private void postNewDetails() {
        Log.d("firebase", "postNewDetails");
        setChangesToCurrentUserDetails();
        db.collection(COLLECTION_NAME).document(auth.getCurrentUser().getUid())
                .set(currentUserDetails.toMap())
                .addOnSuccessListener(onSuccess -> {
                    prefsRepository.setUserCity(city);
                    setChangesSaved(true);
                    setProcessing(false);
                })
                .addOnFailureListener(e -> {
                    setProcessing(false);
                    setToastMessage(e.getLocalizedMessage());
                });
    }

    private UserProfileChangeRequest getUserNameChangeReq() {
        return new UserProfileChangeRequest.Builder().setDisplayName(firstName + " " + secondName).build();
    }

    private void setChangesToCurrentUserDetails() {
        currentUserDetails.setFirstName(firstName);
        currentUserDetails.setSecondName(secondName);
        currentUserDetails.setTelNumber(telNumber);
        currentUserDetails.setCountry(country);
        currentUserDetails.setCity(city);
    }

    private boolean inputFalse() {
        return !(setOldPassOk() & setFirstNameOk() & setSecondNameOk() & setTelNumberOk());
    }

    public MutableLiveData<Boolean> getOldPassOk() {
        return oldPassOk;
    }

    private boolean setOldPassOk() {
        this.oldPassOk.setValue(!oldPassword.isEmpty() && oldPassword.length() > 5);
        return !oldPassword.isEmpty() && oldPassword.length() > 5;
    }

    public MutableLiveData<Boolean> getNewPassOk() {
        return newPassOk;
    }

    private boolean setNewPassOk() {
        this.newPassOk.setValue(newPassword.length() > 5);
        return !newPassword.isEmpty() && newPassword.length() > 5;
    }

    public MutableLiveData<Boolean> getFirstNameOk() {
        return firstNameOk;
    }

    private boolean setFirstNameOk() {
        this.firstNameOk.setValue(!firstName.isEmpty() && firstName.length() > 1);
        return !firstName.isEmpty() && firstName.length() > 3;
    }

    public MutableLiveData<Boolean> getSecondNameOk() {
        return secondNameOk;
    }

    private boolean setSecondNameOk() {
        this.secondNameOk.setValue(!secondName.isEmpty() && secondName.length() > 1);
        return !secondName.isEmpty() && secondName.length() > 3;
    }

    public MutableLiveData<Boolean> getTelNumberOk() {
        return telNumberOk;
    }

    private boolean setTelNumberOk() {
        this.telNumberOk.setValue(!telNumber.isEmpty() && telNumber.length() == 13);
        return !telNumber.isEmpty() && telNumber.length() == 13;
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

    public MutableLiveData<Boolean> getChangesSaved() {
        return changesSaved;
    }

    public void setChangesSaved(boolean changesSaved) {
        this.changesSaved.setValue(changesSaved);
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getSecondName() {
        return secondName;
    }

    public void setSecondName(String secondName) {
        this.secondName = secondName;
    }

    public String getTelNumber() {
        return telNumber;
    }

    public void setTelNumber(String telNumber) {
        this.telNumber = telNumber;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public MutableLiveData<Boolean> getFirstDataLoaded() {
        return firstDataLoaded;
    }

    public void setFirstDataLoaded(boolean firstDataLoaded) {
        this.firstDataLoaded.setValue(firstDataLoaded);
    }
}
