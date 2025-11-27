package com.whasupp.app.ui.view;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.whasupp.app.R;
import com.whasupp.app.ui.adapters.ChatAdapter;
import com.whasupp.app.ui.viewmodel.ChatVM;
import de.hdodenhof.circleimageview.CircleImageView;

public class ChatActivity extends AppCompatActivity {
    private ChatVM viewModel;
    private ChatAdapter adapter;
    private EditText etMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        // 1. Ocultar la ActionBar del sistema para usar nuestro Header con foto
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        // 2. OBTENER DATOS DEL AMIGO (Desde el Intent)
        String chatName = getIntent().getStringExtra("USER_NAME");
        String chatPhoto = getIntent().getStringExtra("USER_PHOTO");
        String chatPartnerUid = getIntent().getStringExtra("USER_UID"); // Vital para la sala privada

        // 3. CONFIGURAR EL HEADER PERSONALIZADO
        TextView tvTitle = findViewById(R.id.tvChatTitle);
        CircleImageView imgAvatar = findViewById(R.id.imgChatAvatar);

        if (chatName != null) {
            tvTitle.setText(chatName);
        }

        // Cargar foto del amigo con Glide
        if (chatPhoto != null && !chatPhoto.isEmpty()) {
            Glide.with(this).load(chatPhoto).into(imgAvatar);
        }

        // 4. OBTENER MIS DATOS (Usuario Actual)
        String myName = "Anonimo";
        String myUid = "";
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            myName = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
            myUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        }

        // 5. CONFIGURAR INTERFAZ (UI)
        etMessage = findViewById(R.id.etMessage);
        FloatingActionButton btnSend = findViewById(R.id.btnSend);
        RecyclerView recycler = findViewById(R.id.recyclerChat);

        adapter = new ChatAdapter(myName);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setStackFromEnd(true); // El chat empieza desde abajo
        recycler.setLayoutManager(layoutManager);
        recycler.setAdapter(adapter);

        // 6. INICIALIZAR VIEWMODEL Y SALA PRIVADA
        viewModel = new ViewModelProvider(this).get(ChatVM.class);

        // Aquí se crea la "Sala Secreta" entre tú y tu amigo
        if (chatPartnerUid != null && !myUid.isEmpty()) {
            viewModel.setupChat(myUid, chatPartnerUid);
        } else {
            Toast.makeText(this, "Error: No se pudo identificar al usuario", Toast.LENGTH_SHORT).show();
        }

        // Observar mensajes entrantes (del historial o nuevos)
        viewModel.getIncomingMessage().observe(this, message -> {
            if(message != null) {
                adapter.addMessage(message);
                recycler.smoothScrollToPosition(adapter.getItemCount() - 1);
            }
        });

        // 7. LÓGICA DE ENVÍO
        final String senderName = myName;
        btnSend.setOnClickListener(v -> {
            String txt = etMessage.getText().toString().trim();
            if(!txt.isEmpty()) {
                viewModel.sendMessage(senderName, txt);
                etMessage.setText("");
            }
        });
    }
}