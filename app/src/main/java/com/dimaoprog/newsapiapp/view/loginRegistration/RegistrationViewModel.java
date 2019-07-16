package com.dimaoprog.newsapiapp.view.loginRegistration;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableBoolean;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.dimaoprog.newsapiapp.data.PrefsRepository;
import com.dimaoprog.newsapiapp.models.UserDetails;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;

import javax.inject.Inject;

import static com.dimaoprog.newsapiapp.utils.Constants.BIRTHDAY_REG_EXP;
import static com.dimaoprog.newsapiapp.utils.Constants.COLLECTION_NAME;
import static com.dimaoprog.newsapiapp.utils.Constants.EMAIL_REG_EXP;

public class RegistrationViewModel extends ViewModel {

    private String email = "";
    private String password = "";
    private String passwordCheck = "";
    private String firstName = "";
    private String secondName = "";
    private String telNumber = "";
    private String birthDay = "";
    private String country;
    private String city;
    private String selectedSources;

    private PrefsRepository prefsRepository;
    private ObservableBoolean processing = new ObservableBoolean();
    private MutableLiveData<String> toastMessage = new MutableLiveData<>();
    private MutableLiveData<Boolean> registrationCompleted = new MutableLiveData<>();

    private MutableLiveData<Boolean> emailOk = new MutableLiveData<>();
    private MutableLiveData<Boolean> passOk = new MutableLiveData<>();
    private MutableLiveData<Boolean> passMatchOk = new MutableLiveData<>();
    private MutableLiveData<Boolean> firstNameOk = new MutableLiveData<>();
    private MutableLiveData<Boolean> secondNameOk = new MutableLiveData<>();
    private MutableLiveData<Boolean> telNumberOk = new MutableLiveData<>();
    private MutableLiveData<Boolean> birthDayOk = new MutableLiveData<>();

    private FirebaseAuth auth;
    private FirebaseFirestore db;

    @Inject
    public RegistrationViewModel(PrefsRepository prefsRepository) {
        this.prefsRepository = prefsRepository;
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
    }

    public void createAccount() {
        if (inputFalse()) {
            return;
        }
        setProcessing(true);
        selectedSources = prefsRepository.getSelectedSourceList();
        auth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener(authResult -> {
                    authResult.getUser().updateProfile(getUserNameChangeReq())
                            .addOnCompleteListener(task1 -> sendEmailVerification(authResult.getUser()));

                    db.collection(COLLECTION_NAME).document(authResult.getUser().getUid())
                            .set(getCurrentUserDetails().toMap())
                            .addOnSuccessListener(onSuccess -> setRegistrationCompleted(true))
                            .addOnFailureListener(onFail -> Log.d("firebase", "addDataToDB:failure" + onFail.getMessage()));
                    setProcessing(false);
                })
                .addOnFailureListener(e -> {
                    Log.d("firebase", "createUserWithEmail:failure" + e.getMessage());
                    setToastMessage(e.getLocalizedMessage());
                    setProcessing(false);
                });
    }

    private UserProfileChangeRequest getUserNameChangeReq() {
        return new UserProfileChangeRequest.Builder().setDisplayName(firstName + " " + secondName).build();
    }

    private UserDetails getCurrentUserDetails() {
        return new UserDetails(firstName, secondName, telNumber, birthDay, country, city, selectedSources);
    }

    private void sendEmailVerification(FirebaseUser newUser) {
        newUser.sendEmailVerification()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        setToastMessage("Verification email sent to " + newUser.getEmail());
                    } else {
                        Log.d("firebase", "sendEmailVerification", task.getException());
                        setToastMessage("Failed to send verification email");
                    }
                });
    }

    private boolean inputFalse() {
        return !(setEmailOk() & setPassOk() & setPassMatchOk() & setFirstNameOk()
                & setSecondNameOk() & setTelNumberOk() & setBirthDayOk());
    }

    public MutableLiveData<Boolean> getEmailOk() {
        return emailOk;
    }

    private boolean setEmailOk() {
        this.emailOk.setValue(!email.isEmpty() && email.matches(EMAIL_REG_EXP));
        return email.matches(EMAIL_REG_EXP);
    }

    public MutableLiveData<Boolean> getPassOk() {
        return passOk;
    }

    private boolean setPassOk() {
        this.passOk.setValue(!password.isEmpty() && password.length() > 5);
        return !password.isEmpty() && password.length() > 5;
    }

    public MutableLiveData<Boolean> getPassMatchOk() {
        return passMatchOk;
    }

    private boolean setPassMatchOk() {
        this.passMatchOk.setValue(password.equals(passwordCheck));
        return password.equals(passwordCheck);
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

    public MutableLiveData<Boolean> getBirthDayOk() {
        return birthDayOk;
    }

    private boolean setBirthDayOk() {
        this.birthDayOk.setValue(birthDay.matches(BIRTHDAY_REG_EXP));
        return birthDay.matches(BIRTHDAY_REG_EXP);
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

    public String getPasswordCheck() {
        return passwordCheck;
    }

    public void setPasswordCheck(String passwordCheck) {
        this.passwordCheck = passwordCheck;
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

    public String getBirthDay() {
        return birthDay;
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

    public void setBirthDay(String birthDay) {
        this.birthDay = birthDay;
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

    private void setToastMessage(String toastMessage) {
        this.toastMessage.setValue(toastMessage);
    }

    public MutableLiveData<Boolean> getRegistrationCompleted() {
        return registrationCompleted;
    }

    public void setRegistrationCompleted(boolean registrationCompleted) {
        this.registrationCompleted.setValue(registrationCompleted);
    }
}
