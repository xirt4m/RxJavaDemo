package hu.bme.aut.amorg.education.rxjava.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import hu.bme.aut.amorg.education.rxjava.util.ObservableStore;
import hu.bme.aut.amorg.education.rxjava.R;
import hu.bme.aut.amorg.education.rxjava.util.TextWithColor;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    //<editor-fold desc="UI">
    private TextView textView;
    private SeekBar seekBar;
    private Button button;
    //</editor-fold>

    //<editor-fold desc="Rx Subscriptions">
    private Subscription subscriptionStringAndSeekBar;
    private Subscription subscriptionNetworkTime;
    //</editor-fold>

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = (TextView) findViewById(R.id.demoTextView);
        button = (Button) findViewById(R.id.startButton);
        seekBar = (SeekBar) findViewById(R.id.seekBar);

        button.setOnClickListener(v -> {
            startStringAndSeekbarObservable();
            startNetworkTimeObservable();
        });
    }

    @Override
    protected void onDestroy() {
        if (subscriptionStringAndSeekBar != null) {
            subscriptionStringAndSeekBar.unsubscribe();
        }
        if (subscriptionNetworkTime != null) {
            subscriptionNetworkTime.unsubscribe();
        }
        super.onDestroy();
    }

    private void startStringAndSeekbarObservable() {
        Observable<String> stringObservable = ObservableStore.getBetterStringObservable(getApplicationContext());
        Observable<Integer> seekBarObservable = ObservableStore.createObservableFromSeekBar(seekBar);
        subscriptionStringAndSeekBar = Observable.combineLatest(stringObservable, seekBarObservable, TextWithColor::new)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::onStringWithColorReceived, this::onError, this::onFinished);
    }

    private void onStringWithColorReceived(TextWithColor textWithColor) {
        textView.setTextColor(textWithColor.getColor());
        textView.setText(textWithColor.getText());
    }

    private void onFinished() {

    }

    private void onError(Throwable throwable) {
        Log.w(TAG, throwable);
    }

    private void startNetworkTimeObservable() {
        subscriptionNetworkTime = ObservableStore.getTimeFromServerObservable()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::onTimeReceived, this::onError);
    }

    private void onTimeReceived(String time) {
        Toast.makeText(this, time, Toast.LENGTH_LONG).show();
    }

}
