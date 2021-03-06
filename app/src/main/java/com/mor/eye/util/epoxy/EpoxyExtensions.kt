package com.mor.eye.util.epoxy

import com.airbnb.epoxy.EpoxyRecyclerView
import com.airbnb.epoxy.EpoxyController
import com.airbnb.epoxy.CarouselModelBuilder
import com.airbnb.epoxy.CarouselModel_
import com.airbnb.epoxy.EpoxyModel

/** Easily add models to an EpoxyRecyclerView, the same way you would in a buildModels method of EpoxyController.
 *
 * @see <a href="https://github.com/airbnb/epoxy/wiki/EpoxyRecyclerView#kotlin-extensions">EpoxyRecyclerView#kotlin-extensions</a>
 */
fun EpoxyRecyclerView.withModels(buildModelsCallback: EpoxyController.() -> Unit) {
    setControllerAndBuildModels(object : EpoxyController() {
        override fun buildModels() {
            buildModelsCallback()
        }
    })
}

/** For use in the buildModels method of EpoxyController. A shortcut for creating a Carousel model, initializing it, and adding it to the controller.
 *
 *  @see <a href="https://github.com/airbnb/epoxy/wiki/Carousel#kotlin-extensions">Carousel#kotlin-extensions</a>
 */
inline fun EpoxyController.carousel(modelInitializer: CarouselModelBuilder.() -> Unit) {
    CarouselModel_().apply {
        modelInitializer()
    }.addTo(this)
}

/** Add models to a CarouselModel_ by transforming a list of items into EpoxyModels.
 *
 * @param items The items to transform to models
 * @param modelBuilder A function that take an item and returns a new EpoxyModel for that item.
 */
inline fun <T> CarouselModelBuilder.withModelsFrom(
        items: List<T>,
        modelBuilder: (T) -> EpoxyModel<*>
) {
    models(items.map { modelBuilder(it) })
}
