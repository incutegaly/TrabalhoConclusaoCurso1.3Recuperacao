package com.example.computador.crud;

/**
 * Created by Pradumn K Mahanta on 07-07-2016.
 **/

public class PlaceItem {
    public final String placeName, placeVicinity, placeType, placeDistance, placeLat, placeLng, placePhotoRef;
    float nota;

    public PlaceItem(String placeName, String placeVicinity, String placeType, String placeDistance, String placeLat, String placeLng, String placePhotoRef, float nota){
        this.placeName = placeName;
        this.placeVicinity = placeVicinity;
        this.placeType = placeType;
        this.placeDistance = placeDistance;
        this.placeLat = placeLat;
        this.placeLng = placeLng;
        this.placePhotoRef = placePhotoRef;
        this.nota = nota;
    }
}
