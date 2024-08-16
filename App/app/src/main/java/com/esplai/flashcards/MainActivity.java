package com.esplai.flashcards;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.esplai.flashcards.service.cardlogic.CardAdapter;
import com.esplai.flashcards.service.cardlogic.CardModel;
import com.esplai.flashcards.ui.Footer;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;

import com.esplai.flashcards.databinding.ActivityMainBinding;
import com.yuyakaido.android.cardstackview.CardStackLayoutManager;
import com.yuyakaido.android.cardstackview.CardStackListener;
import com.yuyakaido.android.cardstackview.CardStackView;
import com.yuyakaido.android.cardstackview.Direction;
import com.yuyakaido.android.cardstackview.StackFrom;
import com.yuyakaido.android.cardstackview.SwipeableMethod;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private CardStackLayoutManager manager;
    private CardAdapter adapter;
    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;
    private ImageView ivSound;
    private View footerFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        addFooter(savedInstanceState);
        CardStackView cardStackView = findViewById(R.id.card_stack_view);
        manager = new CardStackLayoutManager(this, new CardStackListener() {
            @Override
            public void onCardDragging(Direction direction, float ratio) {
                Log.d(TAG, "onCardDragging: d-"+ direction.name() + " ratio-"+ratio);
            }

            @Override
            public void onCardSwiped(Direction direction) {
                Log.d(TAG,"onCardSwiped: p-"+manager.getTopPosition()+" d-"+direction);
                //if(direction == Direction)
            }

            @Override
            public void onCardRewound() {

            }

            @Override
            public void onCardCanceled() {
                Log.d(TAG, "onCardRewound "+manager.getTopPosition());
            }

            @Override
            public void onCardAppeared(View view, int position) {
                //TextView tv = view.findViewById(R.id)
                Log.d(TAG, "onCardAppeared "+position);

            }

            @Override
            public void onCardDisappeared(View view, int position) {

            }
        });
        loadCardSettings(cardStackView);

        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }
    //Method that loads cards setting. For more information check yuyakaido CardStackView
    public void loadCardSettings(CardStackView cardStackView){
        manager.setStackFrom(StackFrom.None);
        manager.setVisibleCount(10);
        manager.setTranslationInterval(8.0f);
        manager.setScaleInterval(0.95f);
        manager.setSwipeThreshold(0.3f);
        manager.setMaxDegree(20.0f);
        manager.setDirections(Direction.FREEDOM);
        manager.setCanScrollHorizontal(true);
        manager.setSwipeableMethod(SwipeableMethod.Manual);
        manager.setOverlayInterpolator(new LinearInterpolator());
        adapter = new CardAdapter(addList());
        cardStackView.setLayoutManager(manager);
        cardStackView.setAdapter(adapter);
        cardStackView.setItemAnimator(new DefaultItemAnimator());
    }

    private List<CardModel> addList() {
        List<CardModel> cards = new ArrayList<>();
        cards.add(new CardModel("Los aguacates no son una verdura, son una fruta. Al igual que sucede con los tomates, sobre los que hay tanta discusión. En el caso del delicioso aguacate, se considera una baya de una sola semilla."));
        cards.add(new CardModel("En los hospitales de Japón no hay habitaciones con el número 4 ni con el número 9. Esto se debe a que se consideran números de la muerte. El cuatro se lee Yon o Shi. Esta última palabra también significa muerte. Por su lado, el nueve se puede leer Kyu o Ku. De nuevo, la segunda opción significa muerte. Terroríficamente matemático. "));
        cards.add(new CardModel("La Torre Eiffel es casi 15 cm más alta durante el verano. Y no, no es magia. Se debe a la expansión térmica. Al calentarse el hierro, las partículas generan energía cinética, ocupando más espacio."));
        cards.add(new CardModel("Los cocodrilos no pueden sacar la lengua. La tienen pegada al paladar y a su membrana en toda su extensión. Quizá por eso siempre parecen tan serios. "));
        cards.add(new CardModel("La miel nunca se echa a perder: Hay frascos de miel de más de 3000 años de antigüedad que se han encontrado en las tumbas de los faraones egipcios y todavía son comestibles. Esto se debe a su baja humedad y alta acidez, lo que impide el crecimiento de bacterias y otros microorganismos."));
        cards.add(new CardModel("Hay más árboles en la Tierra que estrellas en la Vía Láctea: Se estima que hay unos 3 billones de árboles en el mundo, mientras que la Vía Láctea contiene entre 100.000 y 400.000 millones de estrellas."));
        cards.add(new CardModel("Un día en Venus es más largo que un año en Venus: Venus gira muy lentamente sobre su eje, lo que significa que un día en Venus (243 días terrestres) es más largo que un año en Venus (225 días terrestres)"));
        cards.add(new CardModel("El cerebro humano puede generar suficiente electricidad para encender una bombilla: Se estima que el cerebro humano produce alrededor de 20 vatios de electricidad en reposo, suficiente para iluminar una pequeña bombilla."));
        cards.add(new CardModel("Los plátanos son ligeramente radiactivos: Esto se debe a su contenido en potasio, específicamente potasio-40, un isótopo radiactivo natural. Sin embargo, la radiación es tan mínima que no es dañina para los humanos."));
        cards.add(new CardModel("El sonido viaja más rápido en el agua que en el aire: En el agua, el sonido se propaga a aproximadamente 1.484 metros por segundo, más de cuatro veces la velocidad a la que viaja en el aire."));
        cards.add(new CardModel("Los caballos no pueden vomitar: La anatomía de su sistema digestivo, particularmente la fuerte unión entre el esófago y el estómago, impide que los caballos puedan vomitar. Esto hace que algunas condiciones de salud sean extremadamente peligrosas para ellos."));
        cards.add(new CardModel("-Would you lose? \n-Nah, I'd win"));
        return cards;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    private void addFooter(Bundle savedInstance){
        /*if(savedInstance == null)
            return;
        Fragment footer = new Footer();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.footer, footer).commit();*/
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.footer, new Footer(), "FOOTER")
                .disallowAddToBackStack()
                .commit();
    }
}