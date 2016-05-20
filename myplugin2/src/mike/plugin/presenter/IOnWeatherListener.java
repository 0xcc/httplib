package mike.plugin.presenter;

import mike.plugin.model.entity.Weather;

/**
 * Created by Administrator on 16-5-6.
 */
public interface IOnWeatherListener {
    void onSuccess(Weather weather);
    void onError();
}
