package app.arduino.arduinoconnect;

import static app.arduino.arduinoconnect.R.id.vTempM;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class SamplePage extends AppCompatActivity {

    private DatabaseReference mDatabase;
    private Boolean isOn = false;
    private double counter = 0;
    private double counter1 = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).hide();
        setContentView(R.layout.activity_sample_page);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        View view = findViewById(R.id.view);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"})
        View vTempP = findViewById(R.id.vTempP);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"})
        View vTempM = findViewById(R.id.vTempM);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"})
        View vHumP = findViewById(R.id.vHumP);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"})
        View vHumM = findViewById(R.id.vHumM);

        TextView txtActive = findViewById(R.id.txtActive);
        TextView txtHumidity = findViewById(R.id.txtHumidity);
        TextView txtSoilmosture = findViewById(R.id.txtSoilmosture);
        TextView txtTemprature = findViewById(R.id.txtTemprature);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"})
        TextView txtTemp = findViewById(R.id.txtTemp);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"})
        TextView txtHum = findViewById(R.id.txtHum);

        @SuppressLint({"MissingInflatedId", "LocalSuppress"})
        Button btnrele1 = findViewById(R.id.btnrele1);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"})
        Button btnrele2 = findViewById(R.id.btnrele2);

        mDatabase.addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child("data").child("on").getValue() != null) {
                    if (Objects.requireNonNull(snapshot.child("data").child("on").getValue()).toString().equals("1")) {
                        isOn = true;
                        view.setBackgroundResource(R.drawable.on_button);
                        txtActive.setText("Active");

                    } else {
                        isOn = false;
                        view.setBackgroundResource(R.drawable.on_off);
                        txtActive.setText("Inactive");
                    }
                }
                if (snapshot.child("data").child("humidity").getValue() != null) {
                    txtHumidity.setText(snapshot.child("data").child("humidity").getValue().toString());
                }
                if (snapshot.child("data").child("soilmoisture").getValue() != null) {
                    txtSoilmosture.setText(snapshot.child("data").child("soilmoisture").getValue().toString());
                }
                if (snapshot.child("data").child("temperature").getValue() != null) {
                    txtTemprature.setText(snapshot.child("data").child("temperature").getValue().toString());
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        mDatabase.addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child("phone").child("soilmoisture").getValue() != null) {
                    txtHum.setText(Objects.requireNonNull(snapshot.child("phone").child("soilmoisture").getValue()).toString());
                    counter1 = Double.parseDouble(Objects.requireNonNull(snapshot.child("phone").child("soilmoisture").getValue()).toString());
                }
                if (snapshot.child("phone").child("temperature").getValue() != null) {
                    txtTemp.setText(String.valueOf(snapshot.child("phone").child("temperature").getValue()));
                    counter = Double.parseDouble(Objects.requireNonNull(snapshot.child("phone").child("temperature").getValue()).toString());
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        view.setOnClickListener(v -> {
            if (isOn) {
                mDatabase.child("data").child("on").setValue(0);
                isOn = false;
                view.setBackgroundResource(R.drawable.on_off);
            } else {
                mDatabase.child("data").child("on").setValue(1);
                isOn = true;
                view.setBackgroundResource(R.drawable.on_button);
            }
        });

        vTempP.setOnClickListener(v ->{
            if (counter < 100) {
                counter++;
                txtTemp.setText(String.valueOf(counter));
                mDatabase.child("phone").child("temperature").setValue(counter);
            } else {
                Toast.makeText(SamplePage.this, "Max Temp Reached", Toast.LENGTH_SHORT).show();
            }
        });

        vTempM.setOnClickListener(v ->{
            if (counter > 0) {
                counter--;
                txtTemp.setText(String.valueOf(counter));
                mDatabase.child("phone").child("temperature").setValue(counter);
            } else {
                Toast.makeText(SamplePage.this, "Min Temp Reached", Toast.LENGTH_SHORT).show();
            }
        });

        vHumP.setOnClickListener(v ->{
            if (counter < 10000) {
                counter1++;
                txtHum.setText(String.valueOf(counter1));
                mDatabase.child("phone").child("soilmoisture").setValue(counter1);
            } else {
                Toast.makeText(SamplePage.this, "Max Humidity Reached", Toast.LENGTH_SHORT).show();
            }
        });

        vHumM.setOnClickListener(v ->{
            if (counter > 0) {
                counter1--;
                txtHum.setText(String.valueOf(counter1));
                mDatabase.child("phone").child("soilmoisture").setValue(counter1);
            } else {
                Toast.makeText(SamplePage.this, "Min Humidity Reached", Toast.LENGTH_SHORT).show();
            }
        });


    }
}