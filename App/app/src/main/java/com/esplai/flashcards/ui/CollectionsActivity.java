package com.esplai.flashcards.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.GridLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.esplai.flashcards.R;
import com.esplai.flashcards.service.entities.Collection;

import java.util.ArrayList;
import java.util.List;

public class CollectionsActivity extends AppCompatActivity {

    private GridLayout collectionsContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.collection_screen);

        collectionsContainer = findViewById(R.id.collectionsContainer);

        // Simulación de datos para las colecciones
        List<Collection> collections = getCollectionsFromServer();

        for (Collection collection : collections) {
            addCollectionView(collection);
        }
    }

    private void addCollectionView(Collection collection) {
        View collectionView = LayoutInflater.from(this).inflate(R.layout.collection_item, collectionsContainer, false);

        ImageView collectionIcon = collectionView.findViewById(R.id.collection_image);
        TextView collectionName = collectionView.findViewById(R.id.collection_title);

        // Configura la vista con los datos de la colección
        collectionIcon.setImageResource(R.drawable.album_colored); // o una imagen dinámica si tienes una URL
        collectionName.setText(collection.getTitle());

        // Ajusta los parámetros de layout para que se distribuyan correctamente en el GridLayout
        GridLayout.LayoutParams params = new GridLayout.LayoutParams();
        params.width = 0;
        params.height = GridLayout.LayoutParams.WRAP_CONTENT;
        params.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f);
        params.rowSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f);
        params.setMargins(8, 8, 8, 8);
        collectionView.setLayoutParams(params);

        // Añade la vista al contenedor
        collectionsContainer.addView(collectionView);
    }

    private List<Collection> getCollectionsFromServer() {
        //Cambiar luego pot la petición
        List<Collection> collections = new ArrayList<>();
        collections.add(new Collection("Colección 1"));
        collections.add(new Collection("Colección 2"));
        collections.add(new Collection("Colección 3"));collections.add(new Collection("Colección 1"));
        collections.add(new Collection("Colección 2"));
        collections.add(new Collection("Colección 3"));collections.add(new Collection("Colección 1"));
        collections.add(new Collection("Colección 2"));
        collections.add(new Collection("Colección 3"));collections.add(new Collection("Colección 1"));
        collections.add(new Collection("Colección 2"));
        collections.add(new Collection("Colección 3"));collections.add(new Collection("Colección 1"));
        collections.add(new Collection("Colección 2"));
        collections.add(new Collection("Colección 3"));collections.add(new Collection("Colección 1"));
        collections.add(new Collection("Colección 2"));
        collections.add(new Collection("Colección 3"));collections.add(new Collection("Colección 1"));
        collections.add(new Collection("Colección 2"));
        collections.add(new Collection("Colección 3"));collections.add(new Collection("Colección 1"));
        collections.add(new Collection("Colección 2"));
        collections.add(new Collection("Colección 3"));collections.add(new Collection("Colección 1"));
        collections.add(new Collection("Colección 2"));
        collections.add(new Collection("Colección 3"));collections.add(new Collection("Colección 1"));
        collections.add(new Collection("Colección 2"));
        collections.add(new Collection("Colección 3"));collections.add(new Collection("Colección 1"));
        collections.add(new Collection("Colección 2"));
        collections.add(new Collection("Colección 3"));collections.add(new Collection("Colección 1"));
        collections.add(new Collection("Colección 2"));
        collections.add(new Collection("Colección 3"));
        return collections;
    }
}
