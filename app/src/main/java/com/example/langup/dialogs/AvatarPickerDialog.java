package com.example.langup.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.langup.R;
import com.example.langup.utils.AvatarManager;
import java.util.List;

public class AvatarPickerDialog extends Dialog {
    private final AvatarManager avatarManager;
    private final OnAvatarSelectedListener listener;

    public interface OnAvatarSelectedListener {
        void onAvatarSelected(String avatarPath);
    }

    public AvatarPickerDialog(@NonNull Context context, OnAvatarSelectedListener listener) {
        super(context, R.style.DialogTheme);
        this.avatarManager = new AvatarManager(context);
        this.listener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_avatar_picker);

        // Set up RecyclerView
        RecyclerView recyclerView = findViewById(R.id.avatarRecyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
        recyclerView.setAdapter(new AvatarAdapter(avatarManager.getAvatarPaths()));

        // Set up close button
        findViewById(R.id.closeButton).setOnClickListener(v -> dismiss());
    }

    private class AvatarAdapter extends RecyclerView.Adapter<AvatarAdapter.AvatarViewHolder> {
        private final List<String> avatarPaths;

        AvatarAdapter(List<String> avatarPaths) {
            this.avatarPaths = avatarPaths;
        }

        @NonNull
        @Override
        public AvatarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_avatar, parent, false);
            return new AvatarViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull AvatarViewHolder holder, int position) {
            String avatarPath = avatarPaths.get(position);
            Drawable avatar = avatarManager.getAvatarDrawable(avatarPath);
            holder.imageView.setImageDrawable(avatar);
            holder.itemView.setOnClickListener(v -> {
                listener.onAvatarSelected(avatarPath);
                dismiss();
            });
        }

        @Override
        public int getItemCount() {
            return avatarPaths.size();
        }

        class AvatarViewHolder extends RecyclerView.ViewHolder {
            final ImageView imageView;

            AvatarViewHolder(@NonNull View itemView) {
                super(itemView);
                imageView = itemView.findViewById(R.id.avatarImageView);
            }
        }
    }
} 