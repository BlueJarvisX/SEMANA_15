package com.whasupp.app.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide; // Librería de imágenes
import com.whasupp.app.R;
import com.whasupp.app.model.User;
import de.hdodenhof.circleimageview.CircleImageView;
import java.util.ArrayList;
import java.util.List;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.UserViewHolder> {
    private List<User> users = new ArrayList<>();
    private final OnUserClickListener listener;

    public interface OnUserClickListener { void onUserClick(User user); }

    public UsersAdapter(OnUserClickListener listener) { this.listener = listener; }

    public void setUsers(List<User> users) {
        this.users = users;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user, parent, false);
        return new UserViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        holder.bind(users.get(position), listener);
    }

    @Override
    public int getItemCount() { return users.size(); }

    static class UserViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvEmail;
        CircleImageView imgAvatar;

        UserViewHolder(View v) {
            super(v);
            tvName = v.findViewById(R.id.tvUserName);
            tvEmail = v.findViewById(R.id.tvUserStatus);
            imgAvatar = v.findViewById(R.id.imgUserAvatar); // ID nuevo
        }

        void bind(final User user, final OnUserClickListener listener) {
            tvName.setText(user.getName());
            tvEmail.setText(user.getEmail());

            // Cargar foto con Glide
            if (user.getPhotoUrl() != null && !user.getPhotoUrl().isEmpty()) {
                Glide.with(itemView.getContext())
                        .load(user.getPhotoUrl())
                        .placeholder(R.mipmap.ic_launcher_round) // Imagen mientras carga
                        .into(imgAvatar);
            } else {
                imgAvatar.setImageResource(R.mipmap.ic_launcher_round);
            }

            itemView.setOnClickListener(v -> listener.onUserClick(user));
        }
    }
}