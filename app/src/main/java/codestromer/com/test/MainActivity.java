package codestromer.com.test;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;


public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private CardView meCard,pathshalaCard,donateCard, addVolCard,eventCard, infoCard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        meCard = (CardView) findViewById(R.id.me_card);
        pathshalaCard = (CardView) findViewById(R.id.pathshala_card);
        donateCard = (CardView) findViewById(R.id.addPathshala_card);
        addVolCard = (CardView) findViewById(R.id.addVol_card);
        eventCard = (CardView) findViewById(R.id.events_card);
        infoCard = (CardView) findViewById(R.id.info_card);

        meCard.setOnClickListener(this);
        pathshalaCard.setOnClickListener(this);
        donateCard.setOnClickListener(this);
        addVolCard.setOnClickListener(this);
        eventCard.setOnClickListener(this);
        infoCard.setOnClickListener(this);
    }

    @Override
    public void onClick(View v){
        Intent i;
        switch (v.getId()){
            case R.id.me_card :
                i=new Intent(this,myinfo.class);startActivity(i);break;
            case R.id.pathshala_card:
                i=new Intent(this,pathshala.class);startActivity(i);break;
            case R.id.addPathshala_card:
                i=new Intent(this,addPathshala.class);startActivity(i);break;
            case R.id.addVol_card:
                i=new Intent(this,addVol.class);startActivity(i);break;
            case R.id.events_card:
                i=new Intent(this,events.class);startActivity(i);break;
            case R.id.info_card:
                i=new Intent(this,Info.class);startActivity(i);break;
      }
    }
}
