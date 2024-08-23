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

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.esplai.flashcards.R;

import java.util.List;

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

    public CardAdapter(List<CardModel> cardList) {
        this.cardList = cardList;
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
        ImageView ivHeart;
        boolean isShowingBackside = false; // Estado inicial mostrando la cara frontal

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            text = itemView.findViewById(R.id.tvCardText);
            backgroundLayout = itemView.findViewById(R.id.cdCard);
            ivHeart = itemView.findViewById(R.id.ivHeart);

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
                    if (card.getLiked()) {
                        ivHeart.setColorFilter(Color.RED);
                    } else {
                        ivHeart.setColorFilter(Color.GRAY);
                    }
                }
            });

        }


         public void setData(CardModel cardModel) {
            text.setText(cardModel.getText());
            backgroundLayout.setBackgroundColor(getNewBackgroundColor());

            if (cardModel.getLiked()) {
                ivHeart.setColorFilter(Color.RED);
            } else {
                ivHeart.setColorFilter(Color.GRAY);
            }

            // Resetear el estado para mostrar la cara frontal cuando se vincule la tarjeta
            isShowingBackside = false;
        }

        private void flipCard(CardModel card) {
            //Primera parte del flip
            ObjectAnimator flipOutAnimator = ObjectAnimator.ofFloat(backgroundLayout, "rotationY", 0f, 90f);
            flipOutAnimator.setDuration(250);
            flipOutAnimator.setInterpolator(new AccelerateDecelerateInterpolator());

            //Segunda parte del flip
            ObjectAnimator flipInAnimator = ObjectAnimator.ofFloat(backgroundLayout, "rotationY", -90f, 0f);
            flipInAnimator.setDuration(250);
            flipInAnimator.setInterpolator(new AccelerateDecelerateInterpolator());

            flipOutAnimator.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    //Cambiar el texto después de la primera parte de la animación
                    if (isShowingBackside) {
                        // Volver a mostrar la parte frontal
                        text.setText(card.getFront());
                    } else {
                        // Mostrar la parte trasera
                        text.setText(card.getBack());
                    }
                    //Alterna el estado
                    isShowingBackside = !isShowingBackside;

                    //Inicia la segunda parte de la animación
                    flipInAnimator.start();
                }

                @Override
                public void onAnimationCancel(Animator animation) {
                }

                @Override
                public void onAnimationRepeat(Animator animation) {
                }
            });
            flipOutAnimator.start();
        }

    }

}
