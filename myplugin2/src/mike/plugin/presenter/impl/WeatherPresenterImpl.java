package mike.plugin.presenter.impl;

import mike.plugin.model.IWeatherModel;
import mike.plugin.model.entity.Weather;
import mike.plugin.model.impl.WeatherModelImpl;
import mike.plugin.presenter.IOnWeatherListener;
import mike.plugin.presenter.IWeatherPresenter;
import mike.plugin.ui.view.IWeatherView;

/**
 * Created by Administrator on 16-5-6.
 */
public class WeatherPresenterImpl implements IWeatherPresenter,IOnWeatherListener {
    private IWeatherView weatherView;
    private IWeatherModel weatherModel;

    public WeatherPresenterImpl(IWeatherView weatherView) {
        this.weatherView = weatherView;
        weatherModel = new WeatherModelImpl();
    }


    @Override
    public void onSuccess(Weather weather) {
        weatherView.hideLoading();
        weatherView.setWeatherInfo(weather);
    }

    @Override
    public void onError() {
        weatherView.hideLoading();
        weatherView.showError();
    }

    @Override
    public void getWeather(String cityNO) {
        weatherView.showLoading();
        weatherModel.loadWeather(cityNO,this);
    }

}
