package com.skin.minicraft.pe.skinmeloforminecraft;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

public class MainViewModel {
    private MutableLiveData<BaseResource<List<ItemSkin>>> baseResourceMutableLiveData = new MutableLiveData<>();
    private RepoMain repoMain;

    public MainViewModel() {
        repoMain = new RepoMain();
        baseResourceMutableLiveData = repoMain.getModBus();
    }

    public  void setItem(String id,String export){
        repoMain.requestSkin(export,id);
    }
    public LiveData<BaseResource<List<ItemSkin>>> getItem(){
        return  baseResourceMutableLiveData;
    }

}
