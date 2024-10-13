package com.example.todolist

import android.annotation.SuppressLint
import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.ForeignKey
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.functions.Action
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class AddNoteViewModel(application: Application): AndroidViewModel(application) {
    private val notesDao = NoteDatabase.getInstance(application).notesDao()
    private val shouldCloseScreen: MutableLiveData<Boolean> = MutableLiveData()
    private val compositeDisposable: CompositeDisposable = CompositeDisposable()
    fun getShouldCloseScreen(): LiveData<Boolean> {
        return shouldCloseScreen
    }
    fun saveNote(note: Note) {
        val disposable = saveNoteRx(note)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe ({
                Log.d("AddNoteViewModel", "subscribe")
                shouldCloseScreen.value = true
            }, {Log.d("AddNoteViewModel", "Error on add note")})
        compositeDisposable.add(disposable)
    }
    private fun saveNoteRx(note: Note): Completable {
        return Completable.fromAction { notesDao.add(note) }
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}