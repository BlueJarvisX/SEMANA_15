package com.whasupp.app.ui.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.whasupp.app.data.FirebaseRepository;
import com.whasupp.app.model.User;

public class LoginVM extends ViewModel {
    private final FirebaseRepository repository;
    private final MutableLiveData<FirebaseUser> userLiveData = new MutableLiveData<>();

    public LoginVM() {
        repository = FirebaseRepository.getInstance();
    }

    public void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        repository.getAuth().signInWithCredential(credential)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser firebaseUser = repository.getAuth().getCurrentUser();
                        if(firebaseUser != null) {
                            // Extraemos la URL de la foto de Google
                            String photoUrl = "";
                            if (firebaseUser.getPhotoUrl() != null) {
                                photoUrl = firebaseUser.getPhotoUrl().toString();
                            }

                            // Guardamos el usuario con su foto
                            User user = new User(
                                    firebaseUser.getUid(),
                                    firebaseUser.getDisplayName(),
                                    firebaseUser.getEmail(),
                                    photoUrl
                            );
                            repository.saveUser(user);
                            userLiveData.postValue(firebaseUser);
                        }
                    } else {
                        userLiveData.postValue(null);
                    }
                });
    }

    public LiveData<FirebaseUser> getUserLiveData() { return userLiveData; }
}