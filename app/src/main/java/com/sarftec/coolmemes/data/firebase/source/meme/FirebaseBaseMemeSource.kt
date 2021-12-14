package com.sarftec.coolmemes.data.firebase.source.meme

import android.content.Context
import android.util.Log
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import com.sarftec.coolmemes.data.DATA_PAGE_SIZE
import com.sarftec.coolmemes.data.firebase.extra.FirebaseKey
import com.sarftec.coolmemes.data.firebase.extra.FirebaseResult
import com.sarftec.coolmemes.data.firebase.mapper.FirebaseMemeMapper
import com.sarftec.coolmemes.data.firebase.model.FirebaseMeme
import com.sarftec.coolmemes.utils.Resource
import kotlinx.coroutines.tasks.await

abstract class FirebaseBaseMemeSource(
    mapper: FirebaseMemeMapper,
    context: Context
) : FirebaseBuildSource(mapper, context) {

    abstract suspend fun getOption() : Option

    suspend fun hasMemes() : Resource<Boolean> {
        return try {
            val snapshots = parseOption(getOption())
                .limit(1)
                .get()
                .await()
            Resource.success(!snapshots.isEmpty)
        } catch (e : Exception) {
            Resource.error(e.message)
        }
    }

    suspend fun loadFirstPage(key: FirebaseKey.ID): FirebaseResult {
        return if (!FirebaseKey.isDefaultKey(key)) selectedFirstPage(key)
        else defaultFirstPage(key)
    }

    suspend fun getMemes(key: FirebaseKey): FirebaseResult {
        return when (key.direction) {
            FirebaseKey.Direction.NEXT -> getNextMemes(key)
            FirebaseKey.Direction.PREVIOUS -> {
                gePreviousMemes(key)
            }
        }
    }

    private suspend fun selectedFirstPage(key: FirebaseKey.ID): FirebaseResult {
        val documentSnapshot = getDocumentSnapshot(key.id)
        val snapshot = parseOption(getOption())
            .orderBy(FirebaseMeme.FIELD_ID, Query.Direction.DESCENDING)
            .startAfter(documentSnapshot)
            .limit(DATA_PAGE_SIZE - 1)
            .get()
            .await()
        val memes = snapshot.documents.map { it as DocumentSnapshot }
            .toMutableList()
            .also { it.add(0, documentSnapshot) }
            .mapNotNull { docSnapshot ->
                docSnapshot.toObject(FirebaseMeme::class.java).also {
                    it?.id = documentSnapshot.id
                }
            }
        return FirebaseResult(
            memes.map { mapper.toMeme(it) },
            snapshot.documents.lastOrNull()?.let {
                FirebaseKey.Snapshot(FirebaseKey.Direction.NEXT, it)
            },
            null//FirebaseKey.ID(FirebaseKey.Direction.PREVIOUS, key.id)
        )
    }

    private suspend fun defaultFirstPage(key: FirebaseKey.ID): FirebaseResult {
        val snapshot = parseOption(getOption())
            .orderBy(FirebaseMeme.FIELD_ID, Query.Direction.DESCENDING)
            .limit(DATA_PAGE_SIZE)
            .get()
            .await()
        val memes = snapshot.map { documentSnapshot ->
            documentSnapshot.toObject(FirebaseMeme::class.java).also {
                it.id = documentSnapshot.id
            }
        }
        return FirebaseResult(
            memes.map { mapper.toMeme(it) },
            snapshot.documents.lastOrNull()?.let {
                FirebaseKey.Snapshot(FirebaseKey.Direction.NEXT, it)
            },
            null
        )
    }

    private suspend fun getNextMemes(
        snapshot: DocumentSnapshot,
        key: FirebaseKey
    ): FirebaseResult {
        val querySnapshots = querySnapshot(snapshot, Query.Direction.DESCENDING)
        val memes = querySnapshots.map { documentSnapshot ->
            documentSnapshot.toObject(FirebaseMeme::class.java).also {
                it.id = documentSnapshot.id
            }
        }
        return FirebaseResult(
            data = memes.map { mapper.toMeme(it) },
            nextKey = querySnapshots.lastOrNull()?.let {
                FirebaseKey.Snapshot(FirebaseKey.Direction.NEXT, it)
            },
            previousKey = key
        )
    }

    private suspend fun getNextMemes(key: FirebaseKey): FirebaseResult {
        return when (key) {
            is FirebaseKey.Snapshot -> {
                getNextMemes(key.ref, key)
            }
            is FirebaseKey.ID -> {
                val snapshot = getDocumentSnapshot(key.id)
                getNextMemes(snapshot, key)
            }
        }
    }

    private suspend fun gePreviousMemes(
        snapshot: DocumentSnapshot,
        key: FirebaseKey
    ): FirebaseResult {
        val querySnapshots = querySnapshot(snapshot, Query.Direction.ASCENDING)
        val memes = querySnapshots.map { documentSnapshot ->
            documentSnapshot.toObject(FirebaseMeme::class.java).also {
                it.id = documentSnapshot.id
            }
        }
        return FirebaseResult(
            data = memes.reversed().map { mapper.toMeme(it) },
            nextKey = FirebaseKey.ID(
                FirebaseKey.Direction.NEXT,
                querySnapshots.first().id.toLong()
            ),
            previousKey = querySnapshots.lastOrNull()?.let {
                FirebaseKey.Snapshot(FirebaseKey.Direction.PREVIOUS, it)
            }
        )
    }

    private suspend fun gePreviousMemes(key: FirebaseKey): FirebaseResult {
        return when (key) {
            is FirebaseKey.Snapshot -> {
                gePreviousMemes(key.ref, key).also {
                    Log.v("TAG", "Previous Snapshot Items size => ${it.data.size}")
                }
            }
            is FirebaseKey.ID -> {
                val snapshot = getDocumentSnapshot(key.id)
                gePreviousMemes(snapshot, key).also {
                    Log.v("TAG", "Previous ID Items size => ${it.data.size}")
                }
            }
        }
    }

    private suspend fun querySnapshot(
        snapshot: DocumentSnapshot,
        direction: Query.Direction
    ): QuerySnapshot {

        return parseOption(getOption())
            .orderBy(FirebaseMeme.FIELD_ID, direction)
            .startAfter(snapshot)
            .limit(DATA_PAGE_SIZE)
            .get()
            .await()
    }

    private suspend fun getDocumentSnapshot(id: Long): DocumentSnapshot {
        return collectionRef
            .document(id.toString())
            .get()
            .await()
    }

    private fun parseOption(option: Option) : Query {
        return when(option) {
           is Option.QueryOption -> option.query
            else -> collectionRef
        }
    }

    sealed class Option {
        class QueryOption(val query: Query) : Option()
        object Default : Option()
    }
}