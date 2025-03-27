package com.example.myapplication;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    // UI Components
    private Spinner spinnerFrom;
    private Spinner spinnerTo;
    private EditText inputValue;
    private Button convertButton;
    private TextView resultView;

    // Unit categories
    private final String[] lengthUnits = {"Inch", "Foot", "Yard", "Mile", "Centimeter", "Kilometer"};
    private final String[] weightUnits = {"Pound", "Ounce", "Ton", "Kilogram", "Gram"};
    private final String[] tempUnits = {"Celsius", "Fahrenheit", "Kelvin"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize UI Components
        spinnerFrom = findViewById(R.id.spinnerFrom);
        spinnerTo = findViewById(R.id.spinnerTo);
        inputValue = findViewById(R.id.inputValue);
        convertButton = findViewById(R.id.convertButton);
        resultView = findViewById(R.id.resultView);

        // Merge all unit categories into one array
        String[] allUnits = mergeArrays(lengthUnits, weightUnits, tempUnits);

        // Set up the Spinners
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, allUnits);
        spinnerFrom.setAdapter(adapter);
        spinnerTo.setAdapter(adapter);

        // Handle the conversion when the button is clicked
        convertButton.setOnClickListener(v -> convertUnits());
    }

    private void convertUnits() {
        String fromUnit = spinnerFrom.getSelectedItem().toString();
        String toUnit = spinnerTo.getSelectedItem().toString();
        String inputText = inputValue.getText().toString();

        if (inputText.isEmpty()) {
            resultView.setText("Please enter a value");
            return;
        }

        double inputValue = Double.parseDouble(inputText);
        double result = convertValue(inputValue, fromUnit, toUnit);
        resultView.setText("Result: " + result + " " + toUnit);
    }

    private double convertValue(double value, String fromUnit, String toUnit) {
        // Length conversions (Convert everything to cm first)
        Map<String, Double> lengthToCm = new HashMap<>();
        lengthToCm.put("Inch", 2.54);
        lengthToCm.put("Foot", 30.48);
        lengthToCm.put("Yard", 91.44);
        lengthToCm.put("Mile", 160934.0);
        lengthToCm.put("Centimeter", 1.0);
        lengthToCm.put("Kilometer", 100000.0);

        // Weight conversions (Convert everything to kg first)
        Map<String, Double> weightToKg = new HashMap<>();
        weightToKg.put("Pound", 0.453592);
        weightToKg.put("Ounce", 0.0283495);
        weightToKg.put("Ton", 907.185);
        weightToKg.put("Kilogram", 1.0);
        weightToKg.put("Gram", 0.001);

        // Length conversion
        if (lengthToCm.containsKey(fromUnit) && lengthToCm.containsKey(toUnit)) {
            double cmValue = value * lengthToCm.get(fromUnit);
            return cmValue / lengthToCm.get(toUnit);
        }

        // Weight conversion
        if (weightToKg.containsKey(fromUnit) && weightToKg.containsKey(toUnit)) {
            double kgValue = value * weightToKg.get(fromUnit);
            return kgValue / weightToKg.get(toUnit);
        }

        // Temperature conversions
        if (fromUnit.equals("Celsius") && toUnit.equals("Fahrenheit")) {
            return (value * 1.8) + 32;
        }
        if (fromUnit.equals("Fahrenheit") && toUnit.equals("Celsius")) {
            return (value - 32) / 1.8;
        }
        if (fromUnit.equals("Celsius") && toUnit.equals("Kelvin")) {
            return value + 273.15;
        }
        if (fromUnit.equals("Kelvin") && toUnit.equals("Celsius")) {
            return value - 273.15;
        }
        if (fromUnit.equals("Fahrenheit") && toUnit.equals("Kelvin")) {
            return ((value - 32) / 1.8) + 273.15;
        }
        if (fromUnit.equals("Kelvin") && toUnit.equals("Fahrenheit")) {
            return ((value - 273.15) * 1.8) + 32;
        }

        return 0.0; // Invalid conversion
    }

    private String[] mergeArrays(String[]... arrays) {
        int totalLength = 0;
        for (String[] array : arrays) {
            totalLength += array.length;
        }

        String[] result = new String[totalLength];
        int index = 0;
        for (String[] array : arrays) {
            System.arraycopy(array, 0, result, index, array.length);
            index += array.length;
        }
        return result;
    }
}
