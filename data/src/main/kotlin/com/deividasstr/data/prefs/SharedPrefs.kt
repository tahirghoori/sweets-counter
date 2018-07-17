package com.deividasstr.data.prefs

import android.content.Context
import android.content.SharedPreferences
import com.deividasstr.data.utils.DebugOpenClass
import com.deividasstr.data.utils.StrictModePermitter
import com.deividasstr.data.utils.StrictModePermitter.permitDiskReads
import com.deividasstr.domain.repositories.PrefsRepo
import io.reactivex.Completable
import javax.inject.Singleton

@Singleton
@DebugOpenClass
class SharedPrefs(private val ctx: Context) : PrefsRepo {

    companion object {
        const val PREFS_NAME = "PREFS"
        const val PREF_FACT_DATE = "PREF_FACT_DATE"
        const val PREF_SWEET_DATE = "PREF_SWEET_DATE"
    }

    private val prefs: SharedPreferences by lazy {
        StrictModePermitter.permitDiskReads { ctx.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE) }
    }

    var factsUpdatedDate: Long
        get() = permitDiskReads { prefs.getLong(PREF_FACT_DATE, 0) }
        set(value) {
            prefs.edit().putLong(PREF_FACT_DATE, value).apply()
        }

    var sweetsUpdatedDate: Long
        get() = permitDiskReads { prefs.getLong(PREF_SWEET_DATE, 0) }
        set(value) = prefs.edit().putLong(PREF_SWEET_DATE, value).apply()

    override fun saveSweetsDownloadTime(): Completable {
        return Completable.fromAction { sweetsUpdatedDate = System.currentTimeMillis() }
    }
}