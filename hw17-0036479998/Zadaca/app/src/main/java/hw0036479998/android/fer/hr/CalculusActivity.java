package hw0036479998.android.fer.hr;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import hw0036479998.android.fer.hr.models.ResultResponse;

public class CalculusActivity extends AppCompatActivity {

    @BindView(R.id.firstInput)
    EditText firstInput;

    @BindView(R.id.secondInput)
    EditText secondInput;

    @BindView(R.id.radioGroup)
    RadioGroup radioGroup;

    @BindView(R.id.calculate)
    Button calculateButton;

    /**
     * Subtraction radio button text.
     */
    @BindString(R.string.sub_string)
    public String sub;

    /**
     * Addition radio button text.
     */
    @BindString(R.string.add_string)
    public String add;

    /**
     * Multiplication radio button text.
     */
    @BindString(R.string.mul_string)
    public String mul;

    /**
     * Division radio button text.
     */
    @BindString(R.string.div_string)
    public String div;

    /**
     * Display activity request code.
     */
    public static int DISPLAY_REQUEST_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculus);
        ButterKnife.bind(this);
    }

    /**
     * Method executed when user clicks on the result button.
     *
     * @param view Current view.
     */
    @OnClick(R.id.calculate)
    public void resultClick(View view) {

        String firstString = firstInput.getText().toString();
        String secondString = secondInput.getText().toString();

        double first = 0;
        double second = 0;
        String error = null;

        try {
            first = Double.valueOf(firstString);

        } catch (NumberFormatException e) {
            if (!firstString.isEmpty()) {
                error = "Jedan ili više unosa je neispravno uneseno";
            }
        }

        try {
            second = Double.valueOf(secondString);

        } catch (NumberFormatException e) {
            if (!secondString.isEmpty()) {
                error = "Jedan ili više unosa je neispravno uneseno";
            }
        }

        // Get selected radio button
        int selectedID = radioGroup.getCheckedRadioButtonId();
        RadioButton selectedRB = (RadioButton) findViewById(selectedID);
        String rbText = selectedRB.getText().toString();

        // Construct response
        ResultResponse response = new ResultResponse(
                (first % 1) != 0 ? String.valueOf(first) : String.valueOf((int)first),
                (second % 1) != 0 ? String.valueOf(second) : String.valueOf((int)second),
                rbText);

        // Calculate result if there is no error
        if (error == null) {
            double result = 0;

            try {
                result = calculateResult(first, second, rbText);
                response.setResult( (result % 1) != 0 ?
                        String.format("%.2f", result) : String.valueOf((int)result));

            } catch (IllegalArgumentException e) {
                response.setError(e.getMessage());
            }
        }

        // Make new intent and redirect
        Intent intent = new Intent(CalculusActivity.this, DisplayActivity.class);
        Bundle extras = new Bundle();
        extras.putSerializable(DisplayActivity.PAYLOAD_KEY, response);
        intent.putExtras(extras);
        startActivityForResult(intent, DISPLAY_REQUEST_CODE);
    }


    /**
     * Calculate result based on the inputs and given operator string.
     *
     * @param a First input
     * @param b Second input
     * @param operator Operator string
     * @return Result of the operation
     */
    private double calculateResult(double a, double b, String operator) {
        double result = 0;

        if (add.equals(operator)) {
            result = a + b;

        } else if (sub.equals(operator)) {
            result = a - b;

        } else if (mul.equals(operator)) {
            result = a * b;

        } else if (div.equals(operator)) {

            if (b < 10e-10) {
                throw new IllegalArgumentException("Dijeljenje s nulom");
            }
            result = a / b;

        } else {
            throw new IllegalStateException("Unrecognized operator");
        }

        return result;
    }
}
