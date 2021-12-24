package com.example.gitsurfer.utils

import android.app.Activity
import android.content.Context
import android.util.Log
import android.widget.ImageView
import androidx.annotation.RawRes
import com.bumptech.glide.Glide
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.gson.Gson
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.lang.reflect.Type

fun Disposable.addToBag(bag: CompositeDisposable) { bag.add(this) }

fun <T> Single<T>.networkBind(): Single<T> =
    subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())

fun <T> Observable<T>.bind(): Observable<T> =
    subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())

inline fun <reified T> Any.tryCast() : T? {
    if (this is T) return this
    return null
}

fun Any.toJson(): String = Gson().toJson(this)

inline fun <reified T> String.fromJson(type: Type?): T =
    Gson().fromJson(this, type ?: T::class.java)

fun ImageView.load(url: String) {
    Glide.with(context)
        .load(url)
        .into(this)
}

fun Context.openAsset(@RawRes res: Int) = resources.openRawResource(res).bufferedReader()

fun Activity.getValue() {
    Firebase.remoteConfig.fetchAndActivate()
        .addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                val updated = task.result
                Log.d("REMOTE_CONFIG", "Config params updated: $updated")
            } else {
                Log.e("REMOTE_CONFIG", "Fetch failed")
            }
        }
}