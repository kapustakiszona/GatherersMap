package com.example.gatherersmap.navigation

import android.net.Uri
import com.example.gatherersmap.domain.model.ItemSpot
import com.google.android.gms.maps.model.LatLng
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.encodeToJsonElement

sealed class ScreenState(val route: String) {

    data object GoogleMap : ScreenState(route = ROUTE_MAIN_MAP)

    data object BottomSheet : ScreenState(route = ROUTE_BOTT_SHEET_HOME) {
        object Add : ScreenState(route = ROUTE_ADD_ITEM) {
            private const val ROUTE_FOR_ARGS = "add_item"
            fun getRouteWithArgs(latLng: LatLng): String {
                val newMarker = ItemSpot(lat = latLng.latitude, lng = latLng.longitude)
                val newMarkerLatLngJson = Json.encodeToJsonElement(newMarker).toString()
                return "$ROUTE_FOR_ARGS/${newMarkerLatLngJson.encode()}"
            }
        }

        data object Edit : ScreenState(route = ROUTE_EDIT_ITEM) {
            private const val ROUTE_FOR_ARGS = "edit_item"
            fun getRouteWithArgs(itemSpot: ItemSpot): String {
                val itemSpotJson = Json.encodeToJsonElement(itemSpot).toString()
                return "$ROUTE_FOR_ARGS/${itemSpotJson.encode()}"
            }
        }

        data object Details : ScreenState(route = ROUTE_DETAILS_ITEM) {
            private const val ROUTE_FOR_ARGS = "details_item"

            fun getRouteWithArgs(itemSpot: ItemSpot): String {
                val itemSpotJson = Json.encodeToJsonElement(itemSpot).toString()
                return "$ROUTE_FOR_ARGS/${itemSpotJson.encode()}"
            }
        }

        data object Image : ScreenState(route = ROUTE_DETAILS_IMAGE) {
            private const val ROUTE_FOR_ARGS = "details_item_image"

            fun getRouteWithArgs(itemSpot: ItemSpot): String {
                val itemSpotJson = Json.encodeToJsonElement(itemSpot).toString()
                return "$ROUTE_FOR_ARGS/${itemSpotJson.encode()}"
            }
        }
    }

    companion object {
        const val KEY_ITEM_SPOT = "item_spot"
        const val KEY_ITEM_IMAGE = "item_image"
        const val KEY_LAT_LNG_MARKER = "new_latlng"

        const val ROUTE_MAIN_MAP = "google_map"
        const val ROUTE_BOTT_SHEET_HOME = "bott_sheet_home"
        const val ROUTE_ADD_ITEM = "add_item/{$KEY_LAT_LNG_MARKER}"
        const val ROUTE_EDIT_ITEM = "edit_item/{$KEY_ITEM_SPOT}"
        const val ROUTE_DETAILS_ITEM = "details_item/{$KEY_ITEM_SPOT}"
        const val ROUTE_DETAILS_IMAGE = "details_item_image/{$KEY_ITEM_IMAGE}"
    }

    fun String.encode(): String {
        return Uri.encode(this)
    }

}
