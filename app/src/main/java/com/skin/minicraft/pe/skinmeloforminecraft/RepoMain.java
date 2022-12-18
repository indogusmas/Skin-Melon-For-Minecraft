package com.skin.minicraft.pe.skinmeloforminecraft;

import androidx.lifecycle.MutableLiveData;

import java.util.List;
import java.util.Random;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class RepoMain {
    private  final String TAG = getClass().getSimpleName();
    private Api api;
    private MutableLiveData<BaseResource<List<ItemSkin>>> responMutableLiveData = new MutableLiveData<>();

    public RepoMain() {
        api = RetrofitClient.getIntance().create(Api.class);
    }

    public void requestSkin(String export,String id ){
        responMutableLiveData.postValue(BaseResource.loading());
        api.getMod(export,id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    List<ItemSkin> dataItems = response.body().getData();
                    int countAds = dataItems.size() / 20;
                    for (int  i= 0;  i< countAds; i++) {
                        int position = new Random().nextInt(dataItems.size());
                        dataItems.add(position,new ItemSkin("","",0,""));
                    }
                    responMutableLiveData.postValue(
                            BaseResource.success(response.body().getMessage(),dataItems,response.body().getStatus())
                    );
                },throwable -> {
                    responMutableLiveData.postValue(
                            BaseResource.error(throwable.getMessage())
                    );
                });
    }

    public  MutableLiveData<BaseResource<List<ItemSkin>>> getModBus(){
        return  responMutableLiveData;
    }
}
