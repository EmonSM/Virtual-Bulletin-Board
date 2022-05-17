import javafx.scene.control.*
import javafx.scene.effect.DropShadow
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.scene.paint.Color
import javafx.stage.FileChooser
import javafx.stage.Stage
import java.io.FileInputStream

// Acknowledgement: The fileImageView's event handling code is based closely on the "04.graphics/04.drag_shape" and "07.layout/07.boundaries" sample code from the cs349 public repo

class ToolBarView(model: Model, stage: Stage, scrollPane: ScrollPane) : IView, ToolBar() {
    private val BUTTON_IMAGEVIEW_DEFAULT_HEIGHT = 22.0
    private val IMAGE_IMAGEVIEW_DEFAULT_HEIGHT = 240.0
    private val PADDING = 10.0
    private val ROTATION_AMOUNT = 10.0
    private val SCALING_AMOUNT = 0.25

    private var deleteImageButton: Button? = null
    private var rotateLeftButton: Button? = null
    private var rotateRightButton: Button? = null
    private var zoomInButton: Button? = null
    private var zoomOutButton: Button? = null
    private var resetButton: Button? = null

    init {
        // Add Image Button
        val addImageImageView = ImageView(Image("AddImageIcon.png"))
        addImageImageView.isPreserveRatio = true
        addImageImageView.fitHeight = BUTTON_IMAGEVIEW_DEFAULT_HEIGHT
        val addImageButton = Button("Add Image", addImageImageView)
        addImageButton.setOnAction {
            val fileChooser = FileChooser()
            fileChooser.title = "Please select which image to add"
            val file = fileChooser.showOpenDialog(stage)
            if (file != null) {
                if (file.name.lowercase().endsWith(".png") ||
                    file.name.lowercase().endsWith(".jpg") ||
                    file.name.lowercase().endsWith(".jpeg") ||
                    file.name.lowercase().endsWith(".bmp")) {

                    val fileImageView = ImageView(Image(FileInputStream(file)))
                    fileImageView.isPreserveRatio = true
                    fileImageView.fitHeight = IMAGE_IMAGEVIEW_DEFAULT_HEIGHT
                    val imageViewWidth = IMAGE_IMAGEVIEW_DEFAULT_HEIGHT/fileImageView.image.height * fileImageView.image.width
                    fileImageView.x = (Math.random() * (scrollPane.width - imageViewWidth - PADDING))
                    fileImageView.y = (Math.random() * (scrollPane.height - fileImageView.fitHeight - PADDING))
                    model.imageViews.add(fileImageView)

                    var startX = -1.0
                    var startY = -1.0
                    var state = "NONE"

                    fileImageView.setOnMousePressed { event ->
                        event.consume()
                        startX = event.sceneX
                        startY = event.sceneY
                        state = "DRAG"
                        fileImageView.toFront()
                        if (model.selectedImageView != fileImageView) {
                            if (model.selectedImageView != null) {
                                model.selectedImageView!!.effect = null
                            }
                            model.selectedImageView = fileImageView
                            val dropShadow = DropShadow()
                            dropShadow.radius = 10.0
//                            dropShadow.offsetX = 5.0
//                            dropShadow.offsetY = 5.0
                            dropShadow.color = Color.AQUA
                            model.selectedImageView!!.effect = dropShadow
                            model.selectedFileName = file.name
                            model.notifyViews()
                        }
                    }

                    fileImageView.setOnMouseDragged { event ->
                        if (model.mode == "Cascade") {
                            if (state == "DRAG") {
                                val dx = event.sceneX - startX
                                val dy = event.sceneY - startY

                                if (fileImageView.boundsInParent.minX + dx >= 0.0 && fileImageView.boundsInParent.maxX + dx <= scrollPane.width) {
                                    fileImageView.x += dx
                                    startX = event.sceneX
                                }

                                if (fileImageView.boundsInParent.minY + dy >= 0.0 && fileImageView.boundsInParent.maxY + dy <= scrollPane.height) {
                                    fileImageView.y += dy
                                    startY = event.sceneY
                                }
                            }
                        }
                    }

                    fileImageView.setOnMouseReleased {
                        state = "NONE"
                    }

                    fileImageView.setOnMouseExited{
                        state = "NONE"
                    }

                } else {
                    val alert = Alert(Alert.AlertType.ERROR)
                    alert.title = "Error"
                    alert.headerText = "Unable to add image"
                    alert.contentText = "Please ensure that the image you are trying to add has an extension of .png, .jpg/.jpeg, or .bmp."
                    alert.showAndWait()
                }
            }
            model.notifyViews()
        }

        // Delete Image Button
        val deleteImageImageView = ImageView(Image("DeleteImageIcon.png"))
        deleteImageImageView.isPreserveRatio = true
        deleteImageImageView.fitHeight = BUTTON_IMAGEVIEW_DEFAULT_HEIGHT
        deleteImageButton = Button("Delete Image", deleteImageImageView)
        deleteImageButton!!.isDisable = true
        deleteImageButton!!.setOnAction {
            if (model.selectedImageView != null) {
                model.imageViews.remove(model.selectedImageView)
                model.selectedImageView!!.effect = null
                model.selectedImageView = null
                model.selectedFileName = ""
                model.notifyViews()
            }
        }


        // Rotate Left Button
        val rotateLeftImageView = ImageView(Image("RotateLeftIcon.png"))
        rotateLeftImageView.isPreserveRatio = true
        rotateLeftImageView.fitHeight = BUTTON_IMAGEVIEW_DEFAULT_HEIGHT
        rotateLeftButton = Button("Rotate Left", rotateLeftImageView)
        rotateLeftButton!!.isDisable = true
        rotateLeftButton!!.setOnAction {
            if (model.selectedImageView != null) {
                model.selectedImageView?.rotate = model.selectedImageView?.rotate?.minus(ROTATION_AMOUNT)!!
            }
        }

        // Rotate Right Button
        val rotateRightImageView = ImageView(Image("RotateRightIcon.png"))
        rotateRightImageView.isPreserveRatio = true
        rotateRightImageView.fitHeight = BUTTON_IMAGEVIEW_DEFAULT_HEIGHT
        rotateRightButton = Button("Rotate Right", rotateRightImageView)
        rotateRightButton!!.isDisable = true
        rotateRightButton!!.setOnAction {
            if (model.selectedImageView != null) {
                model.selectedImageView?.rotate = model.selectedImageView?.rotate?.plus(ROTATION_AMOUNT)!!
            }
        }

        // Zoom In Button
        val zoomInImageView = ImageView(Image("ZoomInIcon.png"))
        zoomInImageView.isPreserveRatio = true
        zoomInImageView.fitHeight = BUTTON_IMAGEVIEW_DEFAULT_HEIGHT
        zoomInButton = Button("Zoom In", zoomInImageView)
        zoomInButton!!.isDisable = true
        zoomInButton!!.setOnAction {
            if (model.selectedImageView != null) {
                model.selectedImageView?.fitHeight = model.selectedImageView?.fitHeight?.times(1 + SCALING_AMOUNT)!!
            }
        }

        // Zoom Out Button
        val zoomOutImageView = ImageView(Image("ZoomOutIcon.png"))
        zoomOutImageView.isPreserveRatio = true
        zoomOutImageView.fitHeight = BUTTON_IMAGEVIEW_DEFAULT_HEIGHT
        zoomOutButton = Button("Zoom Out", zoomOutImageView)
        zoomOutButton!!.isDisable = true
        zoomOutButton!!.setOnAction {
            if (model.selectedImageView != null) {
                model.selectedImageView?.fitHeight = model.selectedImageView?.fitHeight?.times(1 - SCALING_AMOUNT)!!
            }
        }

        // Reset Button
        val resetImageView = ImageView(Image("ResetIcon.png"))
        resetImageView.isPreserveRatio = true
        resetImageView.fitHeight = BUTTON_IMAGEVIEW_DEFAULT_HEIGHT
        resetButton = Button("Reset", resetImageView)
        resetButton!!.isDisable = true
        resetButton!!.setOnAction {
            if (model.selectedImageView != null) {
                model.selectedImageView?.fitHeight = IMAGE_IMAGEVIEW_DEFAULT_HEIGHT
                model.selectedImageView?.rotate = 0.0
            }
        }

        val viewingMode = ToggleGroup()

        // Cascade Button
        val cascadeImageView = ImageView(Image("CascadeIcon.png"))
        cascadeImageView.isPreserveRatio = true
        cascadeImageView.fitHeight = BUTTON_IMAGEVIEW_DEFAULT_HEIGHT
        val cascadeButton = ToggleButton("Cascade", cascadeImageView)
        cascadeButton.toggleGroup = viewingMode
        cascadeButton.isSelected = true
        cascadeButton.setOnAction {
            model.mode = "Cascade"
            model.notifyViews()
        }

        // Tile Button
        val tileImageView = ImageView(Image("TileIcon.png"))
        tileImageView.isPreserveRatio = true
        tileImageView.fitHeight = BUTTON_IMAGEVIEW_DEFAULT_HEIGHT
        val tileButton = ToggleButton("Tile", tileImageView)
        tileButton.toggleGroup = viewingMode
        tileButton.setOnAction {
            model.mode = "Tile"
            model.notifyViews()
        }

        // Add all buttons to toolbar
        this.items.addAll(addImageButton, deleteImageButton, rotateLeftButton, rotateRightButton, zoomInButton, zoomOutButton, resetButton)
        this.items.addAll(Separator(), cascadeButton, tileButton)
    }

    override fun update(model: Model) {
        if (model.selectedImageView == null) {
            deleteImageButton?.isDisable = true
            rotateLeftButton?.isDisable = true
            rotateRightButton?.isDisable = true
            zoomInButton?.isDisable = true
            zoomOutButton?.isDisable = true
            resetButton?.isDisable = true
        } else if (model.mode == "Cascade") {
            deleteImageButton?.isDisable = false
            rotateLeftButton?.isDisable = false
            rotateRightButton?.isDisable = false
            zoomInButton?.isDisable = false
            zoomOutButton?.isDisable = false
            resetButton?.isDisable = false
        } else if (model.mode == "Tile") {
            deleteImageButton?.isDisable = false
            rotateLeftButton?.isDisable = true
            rotateRightButton?.isDisable = true
            zoomInButton?.isDisable = true
            zoomOutButton?.isDisable = true
            resetButton?.isDisable = true
        }
    }
}