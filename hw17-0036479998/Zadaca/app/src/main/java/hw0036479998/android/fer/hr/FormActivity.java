package hw0036479998.android.fer.hr;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.Image;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import hw0036479998.android.fer.hr.models.FormEntry;
import hw0036479998.android.fer.hr.networking.RetrofitService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FormActivity extends AppCompatActivity {

    @BindView(R.id.relative_path)
    EditText relativePath;

    @BindView(R.id.firstName)
    TextView firstName;

    @BindView(R.id.lastName)
    TextView lastName;

    @BindView(R.id.phoneNumber)
    TextView phoneNumber;

    @BindView(R.id.email)
    TextView email;

    @BindView(R.id.spose)
    TextView spose;

    @BindView(R.id.age)
    TextView age;

    @BindView(R.id.load)
    Button load;

    @BindView(R.id.avatar)
    ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);
        ButterKnife.bind(this);
    }

    /**
     * This method executes when user clicks load button. It fetches data
     * from the given relative path and fills the form on screen.
     *
     * @param view Current view.
     */
    @OnClick(R.id.load)
    public void loadClick(View view) {

        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("http://m.uploadedit.com")
                .build();

        RetrofitService service = retrofit.create(RetrofitService.class);
        service.getForm(relativePath.getText().toString()).enqueue(new Callback<FormEntry>() {

            @Override
            public void onResponse(Call<FormEntry> call, Response<FormEntry> response) {
                Gson gson = new Gson();
                String json = gson.toJson(response.body());
                FormEntry newEntry = gson.fromJson(json, FormEntry.class);

                if (newEntry == null) {
                    onFailure(call, null);
                    return;
                }

                setTextBoxes(newEntry);
            }

            @Override
            public void onFailure(Call<FormEntry> call, Throwable t) {
                Toast.makeText(FormActivity.this,
                        "Unesena je neispravna adresa.",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Sett all boxes according to the given form entry.
     *
     * @param entry Reference to the FormEntry object.
     */
    private void setTextBoxes(FormEntry entry) {

        String fn = entry.getFirstName();
        firstName.setText(fn == null || fn.isEmpty() ? "N/A" : fn);

        String ln = entry.getLastName();
        lastName.setText(ln == null || fn.isEmpty() ? "N/A" : ln);

        String phone = entry.getPhoneNumber();
        phoneNumber.setText(phone == null || phone.isEmpty() ? "N/A" : phone);

        String mail = entry.getEmail();
        email.setText(mail == null || mail.isEmpty() ? "N/A" : mail);

        String sp = entry.getSpouse();
        spose.setText(sp == null || sp.isEmpty() ? "N/A" : sp);

        Integer a = entry.getAge();
        age.setText(a == null ? "N/A" : a.toString());

        if (entry.getAvatarLocation() == null) {
            image.setVisibility(View.INVISIBLE);

        } else {
            image.setVisibility(View.VISIBLE);
            Glide.with(this)
                    .load(entry.getAvatarLocation())
                    .into(image);
        }

    }

    /**
     * This method is called when user clicks on email text view. It sends
     * an email.
     *
     * @param view Current view.
     */
    @OnClick(R.id.email)
    public void emailClick(View view) {

        if (email.getText().toString().equals("N/A")) {
            return;
        }

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("message/rfc822");
        intent.putExtra(Intent.EXTRA_EMAIL  , new String[]{email.getText().toString()});

        try {
            startActivity(Intent.createChooser(intent, "Pošalji e-mail..."));

        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(FormActivity.this,
                    "There are no email clients installed.",
                    Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * This method is called when user clicks on the phone number. It calls the phone number.
     *
     * @param view Current view.
     */
    @OnClick(R.id.phoneNumber)
    public void phoneClick(View view) {

        if (phoneNumber.getText().toString().equals("N/A")) {
            return;
        }

        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + phoneNumber.getText().toString()));

        if (ActivityCompat.checkSelfPermission(FormActivity.this,
                Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        startActivity(callIntent);
    }
}
