package com.whasupp.app.ui.view;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.whasupp.app.R;
import com.whasupp.app.ui.adapters.UsersAdapter;
import com.whasupp.app.ui.viewmodel.UserListVM;

public class UserListActivity extends AppCompatActivity {
    private UserListVM viewModel;
    private UsersAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);
        setTitle("Contactos WhasUpp");

        RecyclerView recycler = findViewById(R.id.recyclerUsers);
        recycler.setLayoutManager(new LinearLayoutManager(this));

        adapter = new UsersAdapter(user -> {
            Intent intent = new Intent(this, ChatActivity.class);
            // PASAMOS TODOS LOS DATOS (Vital para chat privado)
            intent.putExtra("USER_NAME", user.getName());
            intent.putExtra("USER_PHOTO", user.getPhotoUrl());
            intent.putExtra("USER_UID", user.getUid());
            startActivity(intent);
        });

        recycler.setAdapter(adapter);

        viewModel = new ViewModelProvider(this).get(UserListVM.class);
        viewModel.getUsers().observe(this, users -> adapter.setUsers(users));
    }
}