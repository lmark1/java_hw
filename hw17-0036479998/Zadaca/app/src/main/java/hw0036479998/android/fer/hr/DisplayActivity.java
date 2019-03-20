package hw0036479998.android.fer.hr;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import hw0036479998.android.fer.hr.models.ResultResponse;

public class DisplayActivity extends AppCompatActivity {

    @BindView(R.id.resultText)
    TextView resultText;

    @BindView(R.id.okButton)
    Button okButton;

    @BindView(R.id.reportButton)
    Button reportButton;

    /**
     * Payload bundle key.
     */
    public static final String PAYLOAD_KEY = "payload";

    /**
     * Current jmbag.
     */
    private static final String JMBAG = "0036479998";

    /**
     * Result of the operation.
     */
    private ResultResponse result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);
        ButterKnife.bind(this);

        Bundle extras = getIntent().getExtras();
        if (extras != null && extras.getSerializable(PAYLOAD_KEY) != null) {
            result = (ResultResponse) extras.getSerializable(PAYLOAD_KEY);
            resultText.setText(result.getString());
    }
    }

    /**
     * This method executes on OK button click. It returns user to calculate screen.
     *
     * @param view Current view.
     */
    @OnClick(R.id.okButton)
    public void okClick(View view) {
        finish();
    }

    /**
     * This method executes on Report button click. It sends an e-mail with appropriate
     * report body text.
     *
     * @param view Current view.
     */
    @OnClick(R.id.reportButton)
    public void reportClick(View view) {

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("message/rfc822");
        intent.putExtra(Intent.EXTRA_EMAIL  , new String[]{"ana@baotic.org"});
        intent.putExtra(Intent.EXTRA_SUBJECT, JMBAG + ": dz report");
        intent.putExtra(Intent.EXTRA_TEXT   , result.getString());

        try {
            startActivity(Intent.createChooser(intent, "Pošalji e-mail..."));

        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(DisplayActivity.this,
                    "There are no email clients installed.",
                    Toast.LENGTH_SHORT).show();
        }
    }
}
