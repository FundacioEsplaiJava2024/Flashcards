package com.esplai.flashcards.ui;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.esplai.flashcards.R;
import com.esplai.flashcards.service.cardlogic.CardAdapter;
import com.esplai.flashcards.service.cardlogic.CardModel;
import com.esplai.flashcards.service.entities.Collection;

import java.util.ArrayList;
import java.util.List;

public class CollectionDetailActivity extends AppCompatActivity {

    private TextView collectionTitle;
    private TextView collectionDescription;
    private RecyclerView cardsRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection_detail);
        addFooter(savedInstanceState);
        collectionTitle = findViewById(R.id.tvCollectionTitle);
        collectionDescription = findViewById(R.id.tvCollectionDescription);
        cardsRecyclerView = findViewById(R.id.rvCardList);

        //Cambiar collectiondetials por la petición
        Collection collection = getCollectionDetails();

        //Configura la vista con los datos de la colección
        collectionTitle.setText(collection.getTitle());
        collectionDescription.setText("Descripción de la colección");


        CardAdapter cardAdapter = new CardAdapter(collection.getCardList());
        cardsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        cardsRecyclerView.setAdapter(cardAdapter);
    }

    private Collection getCollectionDetails() {
        // Este método debería obtener los datos de la colección desde la API o base de datos
        // Aquí se utilizan datos simulados para propósitos de demostración

        List<CardModel> cards = new ArrayList<>();
        cards.add(new CardModel("This is card 1 description", "This is card 1 backside", false));
        cards.add(new CardModel("This is card 2 description", "This is card 2 backside", false));
        cards.add(new CardModel("This is card 3 description", "This is card 3 backside", false));

        return new Collection("Sample Collection", cards);
    }
    private void addFooter(Bundle savedInstance){
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.footer, new Footer(), "FOOTER")
                .disallowAddToBackStack()
                .commit();
    }
}
