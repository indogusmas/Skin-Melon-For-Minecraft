package com.skin.minicraft.pe.skinmeloforminecraft;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;

public class ItemSkin implements Parcelable {

	@SerializedName("skin_view")
	private String skinView;

	@SerializedName("name")
	private String name;

	@SerializedName("id")
	private int id;

	@SerializedName("skin_download")
	private String skinDownload;

	protected ItemSkin(Parcel in) {
		skinView = in.readString();
		name = in.readString();
		id = in.readInt();
		skinDownload = in.readString();
	}

	public ItemSkin(String skinView, String name, int id, String skinDownload) {
		this.skinView = skinView;
		this.name = name;
		this.id = id;
		this.skinDownload = skinDownload;
	}

	public static final Creator<ItemSkin> CREATOR = new Creator<ItemSkin>() {
		@Override
		public ItemSkin createFromParcel(Parcel in) {
			return new ItemSkin(in);
		}

		@Override
		public ItemSkin[] newArray(int size) {
			return new ItemSkin[size];
		}
	};

	public void setSkinView(String skinView){
		this.skinView = skinView;
	}

	public String getSkinView(){
		return skinView;
	}

	public void setName(String name){
		this.name = name;
	}

	public String getName(){
		return name;
	}

	public void setId(int id){
		this.id = id;
	}

	public int getId(){
		return id;
	}

	public void setSkinDownload(String skinDownload){
		this.skinDownload = skinDownload;
	}

	public String getSkinDownload(){
		return skinDownload;
	}

	@Override
 	public String toString() {
		return  new GsonBuilder().create().toJson(this,ItemSkin.class);
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel parcel, int i) {
		parcel.writeString(skinView);
		parcel.writeString(name);
		parcel.writeInt(id);
		parcel.writeString(skinDownload);
	}
}