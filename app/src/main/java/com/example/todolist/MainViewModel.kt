package com.example.todolist

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import org.jetbrains.annotations.NotNull
import java.io.Closeable
import kotlin.concurrent.thread

class MainViewModel(application: Application):AndroidViewModel(application) {
    private val noteDatabase: NoteDatabase = NoteDatabase.getInstance(application)
    private val compositeDisposable: CompositeDisposable = CompositeDisposable()
    private val notes: MutableLiveData<List<Note>> = MutableLiveData()
    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
    fun remove(note: Note): Unit{
        val composite = removeNotesRx(note)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe ({
                Log.d("MainViewModel", "Removed")
                refreshList()
            }, {Log.d("MainViewModel", "Error on remove")})
        compositeDisposable.add(composite)
    }
    fun getNotes(): LiveData<List<Note>> {
        return notes
    }
    fun refreshList() {
        val disposable = getNotesRx()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({notesFromDb -> notes.value = notesFromDb}, {Log.d("MainViewModel", "Error refresh list")})
        compositeDisposable.add(disposable)
    }
    private fun getNotesRx(): Single<List<Note>> {
        return Single.fromCallable({ noteDatabase.notesDao().getNotes() })
    }
    private fun removeNotesRx(note: Note): Completable {
        return Completable.fromAction { noteDatabase.notesDao().remove(note.id) }
    }
}