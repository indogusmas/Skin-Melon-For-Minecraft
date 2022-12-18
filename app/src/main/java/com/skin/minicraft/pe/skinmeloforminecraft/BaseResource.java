package com.skin.minicraft.pe.skinmeloforminecraft;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

public class BaseResource<T>{

	public   BaseResponseStatus baseResponseStatus;

	@SerializedName("data")
	private T data;

	@SerializedName("message")
	private String message;

	@SerializedName("status")
	private int status;



	public BaseResource(BaseResponseStatus baseResponseStatus, T data, String message, int status) {
		this.baseResponseStatus = baseResponseStatus;
		this.data = data;
		this.message = message;
		this.status = status;
	}

	public BaseResponseStatus getBaseResponseStatus() {
		return baseResponseStatus;
	}

	public void setBaseResponseStatus(BaseResponseStatus baseResponseStatus) {
		this.baseResponseStatus = baseResponseStatus;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public  static  <T> BaseResource<T> success(@NonNull String message, @Nullable T data, int status){
		return  new BaseResource<T>(BaseResponseStatus.STATUS_1_SUCCESS,  data, message,status);
	}

	public  static <T> BaseResource<T> loading(){
		return  new BaseResource<>(BaseResponseStatus.STATUS_3_LOADING,null,null,0);
	}

	public  static <T> BaseResource<T> error(String message){
		return  new BaseResource<>(BaseResponseStatus.STATUS_2_ERROR,null,message,0);
	}


	public  enum  BaseResponseStatus{
		STATUS_1_SUCCESS,
		STATUS_2_ERROR,
		STATUS_3_LOADING
	}

}