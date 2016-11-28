package com.kekiyo.chsch.asterix;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import static com.kekiyo.chsch.asterix.R.layout.activity_route;

public class RouteActivity extends AppCompatActivity {
    //ctrl z end
    private static final int POIT_FUEL = 0;
    private static final int POIT_AMENITY = 1;
    private static final int POIT_PUB = 2;
    private int POIT_CURRENT = POIT_FUEL;
    Spinner poispinner = null;
    ArrayAdapter<CharSequence> poiTypeAdapter;
    Map<String, List<POI>> poimap;
    ArrayList<String> routeList;
    boolean typeSwitch;
    String erstesZiel = "fuck";
    boolean istFahrtAktiv = false;

    //TODO remove all unnecessary variables

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        typeSwitch = true;
        super.onCreate(savedInstanceState);
        setContentView(activity_route);

        routeList = new ArrayList<String>();
        final ListAdapter routenzieleadapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, routeList);
        final ListView list_view_route = (ListView)findViewById(R.id.listview_route);
        list_view_route.setAdapter(routenzieleadapter);



        // --------------------------------------------------------------------------------------------------------
        // Route AUSWAHL
        // --------------------------------------------------------------------------------------------------------

        poispinner = (Spinner)findViewById(R.id.spinnerPOI);
        final Spinner poiType = (Spinner)findViewById(R.id.spinnerPOITYPE);
        poiTypeAdapter = ArrayAdapter.createFromResource(this, R.array.POITypes, android.R.layout.simple_spinner_item);
        poiTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        poiType.setAdapter(poiTypeAdapter);

        JSONReader jsr = new JSONReader(getApplicationContext());
        poimap = null;
        try {
            poimap = jsr.ParsePOI();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        final ArrayList<String> poiList = new ArrayList<String>();
        final ArrayAdapter<String> poiAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, poiList);
        final Spinner poiSpinner = (Spinner)findViewById(R.id.spinnerPOI);



        poiType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                typeSwitch = true;
                poiList.clear();
                switch (i) {
                    case POIT_FUEL:
                        poiList.add("Verfügbare Tankstellen: ");
                        for(int iter = 0; iter < poimap.get("fuel").size(); iter++){
                            poiList.add(poimap.get("fuel").get(iter).name + " (" + poimap.get("fuel").get(iter).address + ")");
                        }
                        break;
                    case POIT_AMENITY:
                        poiList.add("Verfügbare Sehenswürdigkeiten: ");
                        for(int iter = 0; iter < poimap.get("amenity").size(); iter++){
                            poiList.add(poimap.get("amenity").get(iter).name + " (" + poimap.get("amenity").get(iter).address + ")");
                        }
                        break;
                    case POIT_PUB:
                        poiList.add("Verfügbare Kneipen: ");
                        for(int iter = 0; iter < poimap.get("pub").size(); iter++){
                            poiList.add(poimap.get("pub").get(iter).name + " (Capacity: " + Integer.toString(poimap.get("pub").get(iter).capacity) + ")");
                        }
                        break;
                    default:

                        break;
                }
                poiSpinner.setAdapter(poiAdapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        Intent iin = getIntent();
        Bundle b = iin.getExtras();
        if(b != null){
            erstesZiel = (String)b.get("erstesZiel");
            routeList.add(erstesZiel);
            list_view_route.setAdapter(routenzieleadapter);
        }

        poispinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(typeSwitch == true){
                    typeSwitch = false;
                } else {
                    if(i > 0) {
                        i--;
                        String test = poiType.getSelectedItem().toString();
                        String displayText = "";
                        POI mpoi;
                        String[] POITypes = getResources().getStringArray(R.array.POITypes); //0:Fuel/Tankstelle 1:Amenity/Sehenswuerdigkeit 2:Pub/Kneip

                        if (test.equals(POITypes[0])) {
                            mpoi = poimap.get("fuel").get(i);
                            displayText = mpoi.name
                                    + " \n" + mpoi.address;

                        } else if (test.equals(POITypes[1])) {
                            mpoi = poimap.get("amenity").get(i);
                            displayText = mpoi.name
                                    + " \n" + mpoi.address;

                        } else if (test.equals(POITypes[2])) {
                            mpoi = poimap.get("pub").get(i);
                            displayText = mpoi.name
                                    + " \n" + mpoi.address
                                    + " \nCapacity: " + Integer.toString(mpoi.capacity);

                        } else {
                        }

                        routeList.add(displayText);
                        list_view_route.setAdapter(routenzieleadapter);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        final EditText zielText = (EditText)findViewById(R.id.zielEditText);
        Button ziel_hinzufuegen_button = (Button)findViewById(R.id.ziel_hinzufuegen_button);
        ziel_hinzufuegen_button.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View v){
                routeList.add(zielText.getText().toString());
                list_view_route.setAdapter(routenzieleadapter);
                zielText.getText().clear();
            }
        });

        Button routeWeg = (Button)findViewById(R.id.routeWegButton);
        routeWeg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                routeList.clear();
                list_view_route.setAdapter(routenzieleadapter);
            }
        });

        final Button startbut = (Button)findViewById(R.id.StartButton);
        startbut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(istFahrtAktiv == false) {
                    Toast toast = Toast.makeText(getApplicationContext(), "Die Route wird angefahren.", Toast.LENGTH_SHORT);
                    toast.show();
                    startbut.setText("Pause");
                    istFahrtAktiv = true;
                } else {
                    Toast toast = Toast.makeText(getApplicationContext(), "Die Route wird unterbrochen.", Toast.LENGTH_SHORT);
                    toast.show();
                    startbut.setText("Start");
                    istFahrtAktiv = false;
                }
                /*
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(RouteActivity.this);

                alertDialog.setTitle("Fahrt aktiv");

                alertDialog.setMessage("Bitte beachten Sie die Hauptanzeige ihres Fahrzeugs. \nUm die nächstmögliche Haltestelle anzufahren, drücken Sie auf 'Pause'. \nUm die Route zu bearbeiten, drücke Sie auf 'zurück'.");

                alertDialog.setPositiveButton("Pause", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int which) {
                        Toast.makeText(getApplicationContext(), "Das Fahrzeug wird an der nächstmöglichen Haltestelle geparkt", Toast.LENGTH_SHORT).show();
                    }
                });

                alertDialog.setNegativeButton("Zurück", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                // Showing Alert Message
                alertDialog.show();
                */
            }
        });

    }
}
