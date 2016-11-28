package com.kekiyo.chsch.asterix;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.RunnableFuture;

//TODO persistenz,
//TODO sprache,

public class MainActivity extends AppCompatActivity {

    private static final int TUER_FAHRER = 0;
    private static final int TUER_BEIFAHRER = 1;
    private static final int TUER_HINTENLINKS = 2;
    private static final int TUER_HINTENRECHTS = 3;
    private static final int TUER_KOFFERRAUM = 4;
    CharSequence erstesZiel = "Parkplatz Heidelberger Straße 15";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // --------------------------------------------------------------------------------------------------------
        // Tank
        // --------------------------------------------------------------------------------------------------------

        final ProgressBar TankBarAnzeige = (ProgressBar)findViewById(R.id.TankProgressBar);
        TextView TankTextAnzeige = (TextView)findViewById(R.id.TankTextInfo);

        TankBarAnzeige.setMax(100);
        TankBarAnzeige.post(new Runnable() {
            public void run(){
                TankBarAnzeige.setProgress(70);
            }
        });
        TankTextAnzeige.setText("70%");

        // --------------------------------------------------------------------------------------------------------
        // Verriegelung
        // --------------------------------------------------------------------------------------------------------

        final Vector<ToggleButton> verriegelungTB_v = new Vector<ToggleButton>();
        verriegelungTB_v.add((ToggleButton)findViewById(R.id.VerriegelungFahrerToggleButton));
        verriegelungTB_v.add((ToggleButton)findViewById(R.id.VerriegelungBeifahrerFahrerToggleButton));
        verriegelungTB_v.add((ToggleButton)findViewById(R.id.VerriegelungHintertuerLinksFahrerToggleButton));
        verriegelungTB_v.add((ToggleButton)findViewById(R.id.VerriegelungHintertuerRechtsFahrerToggleButton));
        verriegelungTB_v.add((ToggleButton)findViewById(R.id.VerriegelungKofferraumFahrerToggleButton));

        final ToggleButton VerriegelungKofferraumLukeB = (ToggleButton)findViewById(R.id.VerriegelungKofferraumLukeFahrerToggleButton);
        Button VerriegelungSchliessenB = (Button)findViewById(R.id.VerriegelungSchliessenToggleButton);

        for(int i = 0; i < verriegelungTB_v.size(); ++i){
            final int finalI = i;
            verriegelungTB_v.elementAt(i).setOnClickListener(new Button.OnClickListener() {
                public void onClick(View v){
                    if(finalI == TUER_KOFFERRAUM) {
                        if (VerriegelungKofferraumLukeB.isChecked() == true) {
                            VerriegelungKofferraumLukeB.setChecked(false);
                            Toast toast = Toast.makeText(getApplicationContext(), "Kofferraumluke wird geschlossen!", Toast.LENGTH_SHORT);
                            toast.show();
                        }
                    }
                }
            });
        }


        VerriegelungSchliessenB.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v){
                for(ToggleButton tbv : verriegelungTB_v){
                    tbv.setChecked(false);
                }
                if(VerriegelungKofferraumLukeB.isChecked() == true) {
                    VerriegelungKofferraumLukeB.setChecked(false);
                    Toast toast = Toast.makeText(getApplicationContext(), "Kofferraumluke wird geschlossen!", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });


        VerriegelungKofferraumLukeB.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v){
                if(verriegelungTB_v.elementAt(TUER_KOFFERRAUM).isChecked() == false){
                    Toast toast = Toast.makeText(getApplicationContext(),  "Kofferraum wurde entriegelt!", Toast.LENGTH_SHORT);
                    toast.show();
                    verriegelungTB_v.elementAt(TUER_KOFFERRAUM).setChecked(true);
                }
            }
        });

        // --------------------------------------------------------------------------------------------------------
        // Klimaanlage
        // --------------------------------------------------------------------------------------------------------

        final SeekBar klimaanlageSB = (SeekBar)findViewById(R.id.KlimaanlageSeekBar);
        klimaanlageSB.setEnabled(false);
        final TextView klimaanlageTV = (TextView)findViewById(R.id.KlimaanlageTextInfo);
        klimaanlageTV.setEnabled(false);
        klimaanlageTV.setText("18°C");
        final int klimaanlage_step = 1;
        final int klimaanlage_max = 28;
        final int klimaanlage_min = 18;
        klimaanlageSB.setMax( (klimaanlage_max-klimaanlage_min) / klimaanlage_step);
        klimaanlageSB.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                klimaanlageTV.setText(""+ Integer.toString( klimaanlage_min + (i * klimaanlage_step) )  + "°C");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        final Switch klimaanlageSW = (Switch)findViewById(R.id.KlimaanlageSwitch);
        klimaanlageSW.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked){
                if(isChecked){
                    klimaanlageSB.setEnabled(true);
                    klimaanlageTV.setEnabled(true);
                } else {
                    klimaanlageSB.setEnabled(false);
                    klimaanlageTV.setEnabled(false);
                }
            }
        });

        // --------------------------------------------------------------------------------------------------------
        // Los
        // --------------------------------------------------------------------------------------------------------

        Button LosStartB = (Button)findViewById(R.id.LosStartButton);
        LosStartB.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View v){

                boolean allLocked = true;

                for(ToggleButton tbv : verriegelungTB_v){
                    if(tbv.isChecked() == true){
                        allLocked = false;
                    }
                }

                if(allLocked == false){
                    Toast toast = Toast.makeText(getApplicationContext(), "Alle Türen müssen verriegelt sein!", Toast.LENGTH_SHORT);
                    toast.show();
                } else {
                    Intent i = new Intent(getApplicationContext(), RouteActivity.class);
                    i.putExtra("erstesZiel", erstesZiel);
                    startActivity(i);
                }
            }
        });



        Button LosStandortB = (Button)findViewById(R.id.LosStandortButton);
        LosStandortB.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View v){
                /*
                Toast toast = Toast.makeText(getApplicationContext(),  "Ihr Standort wurde aktualisiert!", Toast.LENGTH_SHORT);
                toast.show();
                */


                final CharSequence colors[] = new CharSequence[] {"Aktueller Standort: \nParkplatz Heidelberger Straße 15","Parkplatz D15", "Parkplatz D12", "Straßenrand Birkenweg 14"};

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Anfahrorte in Ihrer Nähe");
                builder.setItems(colors, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO das als erstes Ziel in die Route (Um anzuzeigen, dass das Auto noch unterwegs ist)
                        if(which == 0){
                            erstesZiel = "Parkplatz Heidelberger Straße 15";
                        } else {
                            erstesZiel = colors[which];
                        }
                    }
                });
                builder.show();

                builder.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });
            }
        });




    }

}
