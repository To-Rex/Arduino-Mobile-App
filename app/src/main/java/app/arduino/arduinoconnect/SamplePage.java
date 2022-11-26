package app.arduino.arduinoconnect;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

        TextView txtActive = findViewById(R.id.txtActive);
        TextView txtHumidity = findViewById(R.id.txtHumidity);
        TextView txtSoilmosture = findViewById(R.id.txtSoilmosture);
        TextView txtTemprature = findViewById(R.id.txtTemprature);

        mDatabase.child("data").child("on").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                if (task.getResult().getValue() != null) {
                    if (task.getResult().getValue().toString().equals("1")) {
                        isOn = true;
                        view.setBackgroundResource(R.drawable.on_button);
                        txtActive.setText("Active");

                    } else {
                        isOn = false;
                        view.setBackgroundResource(R.drawable.on_off);
                        txtActive.setText("Inactive");
                    }
                }
            }
        });

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
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
    }
}