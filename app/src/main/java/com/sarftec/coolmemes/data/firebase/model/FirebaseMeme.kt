package com.sarftec.coolmemes.data.firebase.model

import com.google.firebase.firestore.Exclude

class FirebaseMeme(
    @Exclude
    var id: String? = null,
    var imageLocation: String? = null,
    var voteCount: Long? = null,
    var inReview: Boolean? = null,
    var userId: String? = null
) {
    companion object {
        const val FIELD_ID = "id"
        const val FIELD_LIKES = "voteCount"
        const val FIELD_IN_REVIEW = "inReview"
        const val USER_ID = "userId"
    }
}