package com.esplai.flashcards.service.cardlogic;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.util.Log;
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
import com.esplai.flashcards.model.DeleteMessage;
import com.esplai.flashcards.network.ApiCliente;
import com.esplai.flashcards.network.ApiService;
import com.esplai.flashcards.ui.CollectionDetailActivity;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.ViewHolder> {
    private List<CardModel> cardList = null;
    private String actualUsername = getUsername();
    private Context context;
    private static final List<Integer> BACKGROUND_COLORS = List.of(
            Color.rgb(146, 188, 234),
            Color.rgb(226, 194, 255),
            Color.rgb(173, 40, 49),
            Color.rgb(232, 136, 115),
            Color.rgb(201, 177, 189)
    );
    private static int backgroundColorIndex = 0;

    public CardAdapter(List<CardModel> cardList, Context context) {
        this.cardList = cardList;
        this.context = context;
    }

    private String getUsername() {
        Context context = AppContext.getContext();
        SharedPreferences sharedPreferences = context.getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE);
        String username = sharedPreferences.getString("username", null);
        return username;
    }
    private String getToken() {
        Context context = AppContext.getContext();
        SharedPreferences sharedPreferences = context.getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE);
        String token = sharedPreferences.getString("token", null);
        return token;
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
        ImageView ivHeart, ivDelete, ivShare;
        boolean isShowingBackside = false; // Estado inicial mostrando la cara frontal

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            text = itemView.findViewById(R.id.tvCardText);
            backgroundLayout = itemView.findViewById(R.id.cdCard);
            ivHeart = itemView.findViewById(R.id.ivHeart);
            ivDelete = itemView.findViewById(R.id.ivDelete);
            ivShare = itemView.findViewById(R.id.ivShare);

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
                    boolean newLikeStatus = !card.getLiked();
                    card.setLiked(newLikeStatus);

                    // Actualizar la interfaz de usuario
                    if (newLikeStatus) {
                        ivHeart.setColorFilter(Color.RED);
                    } else {
                        ivHeart.setColorFilter(Color.BLACK);
                    }

                }
            });
            ivShare.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    CardModel card = cardList.get(getAdapterPosition());
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.putExtra(Intent.EXTRA_TITLE, card.getFront());
                    context.startActivity(Intent.createChooser(intent,null));
                }
            });
            ivDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    CardModel card = cardList.get(getAdapterPosition());
                    deleteCard(card.getId());
                }
            });
        }

        public void setData(CardModel cardModel) {
            text.setText(cardModel.getFront());
            backgroundLayout.setBackgroundColor(getNewBackgroundColor());
            if (cardModel.getUsername().equals(actualUsername)) {
                ivDelete.setVisibility(View.VISIBLE);
            } else {
                ivDelete.setVisibility(View.INVISIBLE);
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
    public void deleteCard(int id) {
        ApiService apiService = ApiCliente.getClient().create(ApiService.class);
        String token = getToken();

        if (token != null) {
            Call<DeleteMessage> call = apiService.deleteCard("Bearer " + token, id);

            call.enqueue(new Callback<DeleteMessage>() {
                @Override
                public void onResponse(Call<DeleteMessage> call, Response<DeleteMessage> response) {
                    if (response.isSuccessful()) {
                        String cardsResponse = response.body().getMessage();
                        if (cardsResponse.equals("Card with ID "+id+" was deleted successfully")) {
                            int positionToRemove = -1;
                            for (int i = 0; i < cardList.size(); i++) {
                                if (cardList.get(i).getId() == id) {
                                    positionToRemove = i;
                                    break;
                                }
                            }
                            if (positionToRemove != -1) {
                                // Eliminar la carta de la lista y notificar al adaptador
                                cardList.remove(positionToRemove);
                                notifyItemRemoved(positionToRemove);
                                notifyItemRangeChanged(positionToRemove, cardList.size());
                                Toast.makeText(context, "Carta eliminada correctamente", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(context, "No se encontró la carta para eliminar", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(context, "No se encontraron cartas en esta colección, prueba a añadirlas", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        try {
                            Log.e("CardsError", "Error body: " + response.errorBody().string());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        Toast.makeText(context, "Error al recuperar las cartas", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<DeleteMessage> call, Throwable t) {
                    Toast.makeText(context, "Error al contactar con el servidor", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(context, "No se encontró el token", Toast.LENGTH_SHORT).show();
        }
    }


}
