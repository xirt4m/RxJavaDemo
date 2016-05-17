package hu.bme.aut.amorg.education.rxjava;


import android.content.Context;
import android.graphics.Color;
import android.widget.SeekBar;

import java.util.concurrent.TimeUnit;

import hu.bme.aut.amorg.education.rxjava.network.Network;
import hu.bme.aut.amorg.education.rxjava.util.OnSeekBarChangeAdapter;
import rx.Observable;
import rx.Single;

public class ObservableStore {

    private static final int STRING_OBSERVER_INTERVAL = 2;

    public static Observable<String> getBetterStringObservable(Context context) {
        return Observable.defer(() -> Observable.interval(0, STRING_OBSERVER_INTERVAL, TimeUnit.SECONDS).map(aLong -> {
            int ms = aLong.intValue();
            String[] fruits = context.getResources().getStringArray(R.array.fruits);
            return fruits != null ? fruits[ms % fruits.length] : null;
        }));
    }

    public static Observable<Integer> createObservableFromSeekBar(SeekBar seekBar) {
        return Observable.create(subscriber -> {
            seekBar.setOnSeekBarChangeListener(new OnSeekBarChangeAdapter() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    int color = Color.HSVToColor(new float[]{((float) progress) / 100.0f * 360.0f, 0.6f, 0.6f});
                    subscriber.onNext(color);
                }
            });

        });
    }

    public static Single<String> getTimeFromServerObservable() {
        return Single.defer(() -> Single.just(Network.getTimeFromApi()));
    }

}
