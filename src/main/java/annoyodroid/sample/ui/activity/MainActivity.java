package annoyodroid.sample.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.concurrent.Executors;

import annoyodroid.sample.R;
import butterknife.Bind;
import butterknife.BindString;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemSelected;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @author Jorge Antonio Diaz-Benito Soriano (github.com/Stoyicker).
 */
public class MainActivity extends AppCompatActivity {

    private static final String IF_TRAVIS = "true";
    private static final String CI_KEY = "CI";

    @SuppressWarnings("WeakerAccess") //Butterknife
    @Bind(R.id.view_response)
    TextView mResponseField;

    @SuppressWarnings("WeakerAccess") //Butterknife
    @Bind(R.id.spinner_methods)
    Spinner mMethodSpinner;

    @SuppressWarnings("WeakerAccess") //Butterknife
    @Bind(R.id.button_sync)
    Button mButtonSync;

    @SuppressWarnings("WeakerAccess") //Butterknife
    @Bind(R.id.button_async)
    Button mButtonASync;

    @SuppressWarnings("WeakerAccess") //Butterknife
    @Bind(R.id.progress_bar)
    View mProgressBar;

    @SuppressWarnings("WeakerAccess") //Butterknife
    @Bind(R.id.edit_text_api_key)
    TextView mApiKeyView;

    private SharedPreferences mSharedPreferences;

    @BindString(R.string.repo_url)
    String mRepoUrl;

    @Override
    public boolean onCreateOptionsMenu(@NonNull final Menu menu) {
        final MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar_activity_demo, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull final MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_github:
                openRepo();
                return Boolean.TRUE;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void openRepo() {
        final Intent ghIntent = new Intent(Intent.ACTION_VIEW);
        ghIntent.setData(Uri.parse(mRepoUrl));
        startActivity(ghIntent);
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mResponseField.setMovementMethod(new ScrollingMovementMethod());
        mSharedPreferences = getSharedPreferences(getPackageName(),
                Context.MODE_PRIVATE);
        mApiKeyView.post(new Runnable() {
            @Override
            public void run() {
                final String key = getString(R.string.pref_key_api_key);
                mApiKeyView.setText(onCi() ? System.getenv(key) : mSharedPreferences.getString(key, ""));
            }
        });
    }

    @OnClick(R.id.button_async)
    void requestAsync() {
        //You'll have to instantiate it in the switch statement
        final Callback<Object> genericCallback = new Callback<Object>() {

            @Override
            public void onResponse(final Response<Object> response, final Retrofit retrofit) {
                MainActivity.this.setText((response != null ? response.body().toString() : "Empty response received"));
                MainActivity.this.setResponseVisibility(true);
            }

            @Override
            public void onFailure(final Throwable t) {
                MainActivity.this.setText(t.getMessage() + ":" + t.getCause());
                MainActivity.this.setResponseVisibility(true);

            }
        };
        final int position;

        switch (position = mMethodSpinner.getSelectedItemPosition()) {
//            default:
//                throw new IllegalArgumentException("Position " + position + " not properly aligned with a method");
        }
    }

    private void setResponseVisibility(final boolean b) {
        mProgressBar.post(new BooleanParameterRunnable() {
            @Override
            public void run() {
                mProgressBar.setVisibility(mBoolean ? View.INVISIBLE : View.VISIBLE);
            }
        }.init(b));
        mResponseField.post(new BooleanParameterRunnable() {
            @Override
            public void run() {
                mResponseField.setVisibility(mBoolean ? View.VISIBLE : View.INVISIBLE);
            }
        }.init(b));
    }

    private static abstract class BooleanParameterRunnable implements Runnable {

        boolean mBoolean;

        BooleanParameterRunnable init(boolean b) {
            this.mBoolean = b;
            return this;
        }

        @Override
        public abstract void run();
    }

    @OnClick(R.id.button_sync)
    void requestSync() {
        new AsyncTask<Void, Void, Object>() {

            private int mPosition;

            private AsyncTask<Void, Void, Object> init(int position) {
                mPosition = position;
                return this;
            }

            @Override
            protected Object doInBackground(final Void... params) {
                switch (mPosition) {
                    default:
//                throw new IllegalArgumentException("Position " + position + " not properly aligned with a method");
                }
                return null;
            }

            @Override
            protected void onPostExecute(final @Nullable Object o) {
                MainActivity.this.setText((o != null ? o.toString() : "Empty response received"));
                MainActivity.this.setResponseVisibility(true);
            }
        }.init(mMethodSpinner.getSelectedItemPosition()).executeOnExecutor(Executors.newSingleThreadExecutor());
    }

    @OnClick(R.id.button_rxjava)
    void requestRx() {
        //You'll have to instantiate in the switch statement
        Observable<Object> observableResponse = null;
        final int position;

        switch (position = mMethodSpinner.getSelectedItemPosition()) {
            default:
//                throw new IllegalArgumentException("Position " + position + " not properly aligned with a method");
        }
        Observable.just(observableResponse)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Object>() {
                    @Override
                    public void onCompleted() {
                        MainActivity.this.setResponseVisibility(true);
                    }

                    @Override
                    public void onError(final Throwable e) {
                        MainActivity.this.setText(e.getMessage());
                        MainActivity.this.setResponseVisibility(true);
                    }

                    @Override
                    public void onNext(final Object o) {
                        MainActivity.this.setText((o != null ? o.toString() : "Empty response received"));

                    }
                });
    }

    @OnItemSelected(R.id.spinner_methods)
    void onItemSelected(final int position) {
        onAllCommonClick();
        //Remember to use post
        switch (position) {
            default:
                throw new IllegalArgumentException("Position " + position + " not properly aligned with a method");
        }
    }

    @OnClick({R.id.button_rxjava, R.id.button_async, R.id.button_sync})
    void onAllCommonClick() {
        mApiKeyView.post(new Runnable() {
            @Override
            public void run() {
                final InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(mApiKeyView.getWindowToken(), 0);
                mApiKeyView.clearFocus();
            }
        });
        if (!onCi()) {
            mSharedPreferences.edit().putString(getString(R.string.pref_key_api_key), mApiKeyView.getText().toString()).apply();
        }
    }

    @OnClick({R.id.button_rxjava, R.id.button_async, R.id.button_sync})
    void onRequestCommonClick() {
        setResponseVisibility(false);
    }

    private void setText(final String text) {
        MainActivity.this.mResponseField.post(new Runnable() {

            private String mText;

            private Runnable init(final String text) {
                mText = text;
                return this;
            }

            @Override
            public void run() {
                mResponseField.scrollTo(0, 0);
                mResponseField.setText(mText);
            }
        }.init(text));
    }

    private static boolean onCi() {
        final String env = System.getenv
                (CI_KEY);
        return env != null && env.contentEquals(IF_TRAVIS);
    }
}
