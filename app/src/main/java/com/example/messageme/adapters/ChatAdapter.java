package com.example.messageme.adapters;

import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.messageme.databinding.ItemContainerReceivedMessageBinding;
import com.example.messageme.databinding.ItemContainerSentMessageBinding;
import com.example.messageme.models.ChatMessage;
import com.example.messageme.utilities.Constants;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.View;
import android.media.MediaPlayer;
import java.io.File;
import java.io.FileOutputStream;
import android.content.Context;
import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final List<ChatMessage> chatMessages;
    private final Bitmap receiverProfileImage;
    private final String senderId;

    public static final int VIEW_TYPE_SENT = 1;
    public static final int VIEW_TYPE_RECEIVED = 2;

    public ChatAdapter(List<ChatMessage> chatMessages, Bitmap receiverProfileImage, String senderId) {
        this.chatMessages = chatMessages;
        this.receiverProfileImage = receiverProfileImage;
        this.senderId = senderId;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_SENT) {
            return new SentMessageViewHolder(
                    ItemContainerSentMessageBinding.inflate(
                            LayoutInflater.from(parent.getContext()),
                            parent,
                            false));
        } else {
            return new ReceivedMessageViewHolder(
                    ItemContainerReceivedMessageBinding.inflate(
                            LayoutInflater.from(parent.getContext()),
                            parent,
                            false));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == VIEW_TYPE_SENT) {
            ((SentMessageViewHolder) holder).setData(chatMessages.get(position));
        } else {
            ((ReceivedMessageViewHolder) holder).setData(chatMessages.get(position), receiverProfileImage);
        }

    }

    @Override
    public int getItemCount() {
        return chatMessages.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (chatMessages.get(position).senderId != null && chatMessages.get(position).senderId.equals(senderId)) {
            return VIEW_TYPE_SENT;
        } else {
            return VIEW_TYPE_RECEIVED;
        }

    }

    static class SentMessageViewHolder extends RecyclerView.ViewHolder {
        private final ItemContainerSentMessageBinding binding;

        SentMessageViewHolder(ItemContainerSentMessageBinding itemContainerSentMessageBinding) {
            super(itemContainerSentMessageBinding.getRoot());
            binding = itemContainerSentMessageBinding;
        }

        void setData(ChatMessage chatMessage) {
            if (chatMessage.type != null && chatMessage.type.equals(Constants.KEY_MESSAGE_TYPE_IMAGE)) {
                binding.textMessage.setVisibility(View.GONE);
                binding.imageMessage.setVisibility(View.VISIBLE);
                binding.imageMessage.setImageBitmap(getBitmapFromEncodedString(chatMessage.message));
            } else if (chatMessage.type != null && chatMessage.type.equals(Constants.KEY_MESSAGE_TYPE_AUDIO)) {
                binding.textMessage.setVisibility(View.VISIBLE);
                binding.imageMessage.setVisibility(View.GONE);
                binding.textMessage.setText("▶ Play Audio");
                binding.textMessage.setOnClickListener(v -> playAudio(chatMessage.message, v.getContext()));
            } else {
                binding.textMessage.setVisibility(View.VISIBLE);
                binding.imageMessage.setVisibility(View.GONE);
                binding.textMessage.setText(chatMessage.message);
            }
            binding.textDateTime.setText(chatMessage.dateTime);
        }

    }

    static class ReceivedMessageViewHolder extends RecyclerView.ViewHolder {
        private final ItemContainerReceivedMessageBinding binding;

        ReceivedMessageViewHolder(ItemContainerReceivedMessageBinding itemContainerReceivedMessageBinding) {
            super(itemContainerReceivedMessageBinding.getRoot());
            binding = itemContainerReceivedMessageBinding;
        }

        void setData(ChatMessage chatMessage, Bitmap receiverProfileImage) {
            if (chatMessage.type != null && chatMessage.type.equals(Constants.KEY_MESSAGE_TYPE_IMAGE)) {
                binding.textMessage.setVisibility(View.GONE);
                binding.imageMessage.setVisibility(View.VISIBLE);
                binding.imageMessage.setImageBitmap(getBitmapFromEncodedString(chatMessage.message));
            } else if (chatMessage.type != null && chatMessage.type.equals(Constants.KEY_MESSAGE_TYPE_AUDIO)) {
                binding.textMessage.setVisibility(View.VISIBLE);
                binding.imageMessage.setVisibility(View.GONE);
                binding.textMessage.setText("▶ Play Audio");
                binding.textMessage.setOnClickListener(v -> playAudio(chatMessage.message, v.getContext()));
            } else {
                binding.textMessage.setVisibility(View.VISIBLE);
                binding.imageMessage.setVisibility(View.GONE);
                binding.textMessage.setText(chatMessage.message);
            }
            binding.textDateTime.setText(chatMessage.dateTime);
            if (receiverProfileImage != null) {
                binding.imageProfile.setImageBitmap(receiverProfileImage);
            }
        }
    }

    private static void playAudio(String encodedAudio, Context context) {
        try {
            byte[] decoded = Base64.decode(encodedAudio, Base64.DEFAULT);
            File tempMp3 = File.createTempFile("kurchina", "mp3", context.getCacheDir());
            tempMp3.deleteOnExit();
            FileOutputStream fos = new FileOutputStream(tempMp3);
            fos.write(decoded);
            fos.close();

            MediaPlayer mediaPlayer = new MediaPlayer();
            mediaPlayer.setDataSource(tempMp3.getAbsolutePath());
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static Bitmap getBitmapFromEncodedString(String encodedImage) {
        if (encodedImage != null) {
            byte[] bytes = Base64.decode(encodedImage, Base64.DEFAULT);
            return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        } else {
            return null;
        }
    }
}
