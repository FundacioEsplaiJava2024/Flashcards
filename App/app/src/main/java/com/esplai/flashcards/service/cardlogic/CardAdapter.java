package com.esplai.flashcards.service.cardlogic;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.esplai.flashcards.R;

import java.util.List;

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.ViewHolder> {
     private List<CardModel> cardList;
    private static final List<Integer> backgroundColors = List.of(Color.rgb(146, 188, 234), Color.rgb(226, 194, 255), Color.rgb(173, 40, 49),Color.rgb(232, 136, 115), Color.rgb(201, 177, 189));
    private static int backgroundColorIndex = 0;
    public CardAdapter(List<CardModel> cardList) {
        this.cardList = cardList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.flash_card, parent,false);

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
        int res = backgroundColors.get(backgroundColorIndex);
        backgroundColorIndex = (backgroundColorIndex+1) % backgroundColors.size();
        return res;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView text;
        ConstraintLayout backgroundLayout;
        ViewHolder(@NonNull View itemView) {
            super(itemView);
            text = itemView.findViewById(R.id.tvCardText);
            backgroundLayout = itemView.findViewById(R.id.cdCard);
        }

        public void setData(CardModel cardModel) {
            text.setText(cardModel.getText());
            backgroundLayout.setBackgroundColor(getNewBackgroundColor());
        }
    }
}
