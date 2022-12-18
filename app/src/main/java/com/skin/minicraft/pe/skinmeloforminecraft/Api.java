package com.skin.minicraft.pe.skinmeloforminecraft;



import java.util.List;

import io.reactivex.Observable;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface Api {

    @GET("uc")
    Observable<Response<BaseResource<List<ItemSkin>>>> getMod(@Query("export") String export,
                                                             @Query("id") String id);
}
