package com.whasupp.app.ui.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.whasupp.app.data.FirebaseRepository;
import com.whasupp.app.model.User;
import java.util.ArrayList;
import java.util.List;

public class UserListVM extends ViewModel {
    private final MutableLiveData<List<User>> users = new MutableLiveData<>();
    private final FirebaseRepository repository;

    public UserListVM() {
        repository = FirebaseRepository.getInstance();
        loadUsers();
    }

    private void loadUsers() {
        repository.getUsersRef().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<User> list = new ArrayList<>();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    User user = ds.getValue(User.class);
                    if (user != null) list.add(user);
                }
                users.postValue(list);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }

    public LiveData<List<User>> getUsers() { return users; }
}