package com.example.gatherersmap.domain.model

import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterItem
import kotlinx.serialization.Serializable

@Serializable
data class ItemSpot(
    val lat: Double,
    val lng: Double,
    val id: Int = 0,
    val name: String = "",
    val description: String = "",
    val image: String? = null
) : ClusterItem {

    private val zIndex = 0f
    override fun getPosition(): LatLng = LatLng(lat, lng)

    override fun getTitle(): String = name

    override fun getSnippet(): String = description
    override fun getZIndex(): Float = zIndex
}
