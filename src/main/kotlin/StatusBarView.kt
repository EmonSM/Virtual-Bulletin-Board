import javafx.scene.control.Label
import javafx.scene.layout.HBox
import javafx.scene.layout.Priority
import javafx.scene.layout.Region

class StatusBarView(model: Model) : IView, HBox() {
    private val numOfImagesLoaded = Label(" ${model.imageViews.size} images loaded")
    private val selectedImage = Label("No image selected")

    init {
        selectedImage.style = "-fx-font-weight: bold"
        val blankRegion1 = Region()
        setHgrow(blankRegion1, Priority.ALWAYS)
        val blankRegion2 = Region()
        setHgrow(blankRegion2, Priority.ALWAYS)
        this.children.addAll(numOfImagesLoaded, blankRegion1, selectedImage, blankRegion2)
    }

    override fun update(model: Model) {
        numOfImagesLoaded.text = " ${model.imageViews.size} images loaded"
        selectedImage.text = if (model.selectedFileName == "") { "No image selected" }
        else { "${model.selectedFileName} selected" }
    }
}