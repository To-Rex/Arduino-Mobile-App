package app.arduino.arduinoconnect;

import static app.arduino.arduinoconnect.R.id.vTempM;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class SamplePage extends AppCompatActivity {

    private DatabaseReference mDatabase;
    private Boolean isOn = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).hide();
        setContentView(R.layout.activity_sample_page);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        //mDatabase.child("phone").child("humidity").setValue(10);
        //mDatabase.child("phone").child("mator").setValue(10);
        //mDatabase.child("data").child("on").setValue(1);
        //mDatabase.child("phone").child("soilmoisture").setValue(20);
        //mDatabase.child("phone").child("temperature").setValue(30);
        View view = findViewById(R.id.view);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"})
        View vTempP = findViewById(R.id.vTempP);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"})
        View vTempM = findViewById(R.id.vTempM);


        TextView txtActive = findViewById(R.id.txtActive);
        TextView txtHumidity = findViewById(R.id.txtHumidity);
        TextView txtSoilmosture = findViewById(R.id.txtSoilmosture);
        TextView txtTemprature = findViewById(R.id.txtTemprature);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"})
        TextView txtTemp = findViewById(R.id.txtTemp);

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
            txtTemp.setText(String.valueOf(Integer.parseInt(txtTemp.getText().toString()) + 1));
        });
        vTempM.setOnClickListener(v ->{
            txtTemp.setText(String.valueOf(Integer.parseInt(txtTemp.getText().toString()) - 1));
        });

    }
}