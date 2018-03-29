package com.taskail.mixion.ui.animation

import android.os.Parcel
import android.os.Parcelable

/**
 *Created by ed on 3/29/18.
 */

data class RevealAnimationSettings(val centerX: Int,
                                   val centerY: Int,
                                   val width: Int,
                                   val height: Int) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readInt(),
            parcel.readInt(),
            parcel.readInt(),
            parcel.readInt()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(centerX)
        parcel.writeInt(centerY)
        parcel.writeInt(width)
        parcel.writeInt(height)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<RevealAnimationSettings> {
        override fun createFromParcel(parcel: Parcel): RevealAnimationSettings {
            return RevealAnimationSettings(parcel)
        }

        override fun newArray(size: Int): Array<RevealAnimationSettings?> {
            return arrayOfNulls(size)
        }
    }


}