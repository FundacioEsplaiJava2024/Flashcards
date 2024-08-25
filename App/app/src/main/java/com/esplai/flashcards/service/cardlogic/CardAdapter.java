package com.esplai.flashcards.service.cardlogic;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.esplai.flashcards.R;
import com.esplai.flashcards.network.ApiCliente;
import com.esplai.flashcards.network.ApiService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.ViewHolder> {

    private final List<CardModel> cardList;
    private static final List<Integer> BACKGROUND_COLORS = List.of(
            Color.rgb(146, 188, 234),
            Color.rgb(226, 194, 255),
            Color.rgb(173, 40, 49),
            Color.rgb(232, 136, 115),
            Color.rgb(201, 177, 189)
    );
    private static int backgroundColorIndex = 0;
    private final ApiService apiService;
    private final String token;  // Nuevo campo para almacenar el token

    public CardAdapter(List<CardModel> cardList, String token) { // Modificar constructor
        this.cardList = cardList;
        this.apiService = ApiCliente.getClient().create(ApiService.class);
        this.token = token;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.flash_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setData(cardList.get(position));
    }

    @Override
    public int getItemCount() {
        return cardList.size();
    }

    private int getNewBackgroundColor() {
        int color = BACKGROUND_COLORS.get(backgroundColorIndex);
        backgroundColorIndex = (backgroundColorIndex + 1) % BACKGROUND_COLORS.size();
        return color;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView text;
        ConstraintLayout backgroundLayout;
        ImageView ivHeart, btDelete;
        boolean isShowingBackside = false;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            text = itemView.findViewById(R.id.tvCardText);
            backgroundLayout = itemView.findViewById(R.id.cdCard);
            ivHeart = itemView.findViewById(R.id.ivHeart);
            btDelete = itemView.findViewById(R.id.btdelete);

            backgroundLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CardModel card = cardList.get(getAdapterPosition());
                    if (card.getBack() != null) {
                        flipCard(card);
                    }
                }
            });

            ivHeart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CardModel card = cardList.get(getAdapterPosition());
                    card.setLiked(!card.getLiked());
                    notifyItemChanged(getAdapterPosition());
                }
            });

            btDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    CardModel card = cardList.get(position);
                    deleteCard(card.getId(), position);
                }
            });
        }

        public void setData(CardModel cardModel) {
            text.setText(cardModel.getFront());
            backgroundLayout.setBackgroundColor(getNewBackgroundColor());

            if (cardModel.getLiked()) {
                ivHeart.setColorFilter(Color.RED);
            } else {
                ivHeart.setColorFilter(Color.GRAY);
            }

            isShowingBackside = false;
        }

        private void flipCard(CardModel card) {
            ObjectAnimator flipOutAnimator = ObjectAnimator.ofFloat(backgroundLayout, "rotationY", 0f, 90f);
            flipOutAnimator.setDuration(250);
            flipOutAnimator.setInterpolator(new AccelerateDecelerateInterpolator());

            ObjectAnimator flipInAnimator = ObjectAnimator.ofFloat(backgroundLayout, "rotationY", -90f, 0f);
            flipInAnimator.setDuration(250);
            flipInAnimator.setInterpolator(new AccelerateDecelerateInterpolator());

            flipOutAnimator.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {}

                @Override
                public void onAnimationEnd(Animator animation) {
                    if (isShowingBackside) {
                        text.setText(card.getFront());
                    } else {
                        text.setText(card.getBack());
                    }
                    isShowingBackside = !isShowingBackside;
                    flipInAnimator.start();
                }

                @Override
                public void onAnimationCancel(Animator animation) {}

                @Override
                public void onAnimationRepeat(Animator animation) {}
            });
            flipOutAnimator.start();
        }

        private void deleteCard(int cardId, int position) {
            apiService.deleteCard(cardId, "Bearer " + token)  // Utilizar el token

                    .enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            if (response.isSuccessful()) {
                                cardList.remove(position);
                                notifyItemRemoved(position);
                                Toast.makeText(itemView.getContext(), "Carta eliminada con éxito", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(itemView.getContext(), "Fallo en eliminar la carta", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {
                            Toast.makeText(itemView.getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }
}
