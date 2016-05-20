package mike.plugin.ui.view;

import mike.plugin.model.entity.Weather;
import mike.plugin.model.entity.WeatherInfo;

/**
 * Created by Administrator on 16-5-6.
 */
public interface IWeatherView {
    void showLoading();
    void hideLoading();
    void showError();
    void setWeatherInfo(Weather weather);
}
