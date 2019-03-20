package hw0036479998.android.fer.hr;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.math)
    Button math;

    @BindView(R.id.stats)
    Button stats;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

    }

    /**
     * This method is performed when user clicks on math button. It
     * redirects user to Calculus activity.
     *
     * @param view Current view.
     */
    @OnClick(R.id.math)
    public void mathClick(View view) {
        Intent intent = new Intent(MainActivity.this, CalculusActivity.class);
        startActivity(intent);
    }

    /**
     * This method is performed when user clicks the stats button. It redirects
     * the user to Form activity.
     *
     * @param view Current view.
     */
    @OnClick(R.id.stats)
    public void statsClick(View view) {
        Intent intent = new Intent(MainActivity.this, FormActivity.class);
        startActivity(intent);
    }
}
