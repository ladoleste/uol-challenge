package br.ufs.uolchallenge.domain

import java.util.*

/**
 * Created by bira on 11/3/17.
 */

data class News(
        val title: String,
        val updateDateTime: Date,
        val sharableURL: String,
        val visualizationContentURL: String,
        val relatedImageURL: String = NO_IMAGE_AVAILABLE
) {
    companion object {
        val NO_IMAGE_AVAILABLE = "http://no.image.available.jpg"
    }
}

