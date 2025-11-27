package com.whasupp.app.data;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.whasupp.app.model.Message;
import com.whasupp.app.model.User;

public class FirebaseRepository {
    private static FirebaseRepository instance;
    private final FirebaseAuth auth;
    private final DatabaseReference database;

    private FirebaseRepository() {
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance().getReference();
    }

    public static synchronized FirebaseRepository getInstance() {
        if (instance == null) instance = new FirebaseRepository();
        return instance;
    }

    public FirebaseAuth getAuth() { return auth; }
    public DatabaseReference getUsersRef() { return database.child("users"); }

    public void saveUser(User user) {
        if (user != null) {
            database.child("users").child(user.getUid()).setValue(user);
        }
    }

    /**
     * Guarda mensaje en una sala específica (Privacidad)
     */
    public void saveMessage(String chatId, Message message) {
        if (message != null && chatId != null) {
            database.child("chats").child(chatId).child("messages").push().setValue(message);
        }
    }

    /**
     * Obtiene referencia de una sala específica para leer historial
     */
    public DatabaseReference getMessagesRef(String chatId) {
        return database.child("chats").child(chatId).child("messages");
    }
}