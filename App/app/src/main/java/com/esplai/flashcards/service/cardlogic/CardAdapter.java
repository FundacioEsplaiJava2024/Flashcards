package com.esplai.flashcards.service.cardlogic;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
        View.OnClickListener listener;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            text = itemView.findViewById(R.id.tvCardText);
            backgroundLayout = itemView.findViewById(R.id.cdCard);
            ivHeart = itemView.findViewById(R.id.ivHeart);

            ivHeart.setOnClickListener(listener);
            listener = new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    System.out.println("ACHUUU");
                    CardModel card = cardList.get(backgroundColorIndex); //TODO: Arreglar
                    if(v.getId()==ivHeart.getId()){
                        card.setLiked(!card.getLiked());
                    }
                }
            };
        }

        public void setData(CardModel cardModel) {
            text.setText(cardModel.getText());
            backgroundLayout.setBackgroundColor(getNewBackgroundColor());

            if (cardModel.getLiked()) {
                ivHeart.setColorFilter(Color.RED); // Cambia el color del corazón a rojo si está "liked"
            } else {
                ivHeart.setColorFilter(Color.BLACK); // Cambia el color del corazón a gris si no está "liked"
            }
        }
    }
}
