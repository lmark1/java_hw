package hw0036479998.android.fer.hr.networking;

import hw0036479998.android.fer.hr.models.FormEntry;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * This interface defines a retrofice service call.
 *
 * Created by lmark on 29.6.2017..
 */

public interface RetrofitService {

    @GET("{path}")
    Call<FormEntry> getForm(@Path("path") String path);
}
