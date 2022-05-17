import javafx.scene.Group
import javafx.scene.control.ScrollPane
import javafx.scene.image.ImageView
import javafx.scene.layout.Pane

class CanvasView(model: Model) : IView, ScrollPane() {
    private val IMAGEVIEW_DEFAULT_HEIGHT = 240.0
    private val PADDING = 10.0
    val group = Group()

    init {
        group.isAutoSizeChildren = false
        this.hbarPolicy = ScrollBarPolicy.AS_NEEDED
        this.vbarPolicy = ScrollBarPolicy.AS_NEEDED
        this.content = group

        this.setOnMousePressed {
            if (model.selectedImageView != null) {
                model.selectedImageView?.effect = null
                model.selectedImageView = null
                model.selectedFileName = ""
                model.notifyViews()
            }
        }
    }

    override fun update(model: Model) {
        // Updates deleted images for both Cascade and Tile modes
        val deletedViews = ArrayList<ImageView>()
        for (imageView in this.group.children) {
            if (imageView is ImageView) {
                if (!model.imageViews.contains(imageView)) {
                    deletedViews.add(imageView)
                }
            }
        }
        for (imageView in deletedViews) {
            this.group.children.remove(imageView)
        }

        if (model.mode == "Cascade") {
            // Update added images
            for (imageView in model.imageViews) {
                if (!this.group.children.contains(imageView)) {
                    this.group.children.add(imageView)
                }
            }
        } else if (model.mode == "Tile") {
            this.group.children.clear()
            var startX = PADDING
            var startY = PADDING
            for (imageView in model.imageViews) {
                imageView.fitHeight = IMAGEVIEW_DEFAULT_HEIGHT
                imageView.rotate = 0.0
                val imageViewWidth = IMAGEVIEW_DEFAULT_HEIGHT/imageView.image.height * imageView.image.width
                if (startX + imageViewWidth + PADDING > this.width) {
                    startX = PADDING
                    startY += (IMAGEVIEW_DEFAULT_HEIGHT + PADDING)
                }
                imageView.x = startX
                imageView.y = startY
                startX += (imageViewWidth + PADDING)
                this.group.children.add(imageView)
            }
        }
    }
}