import javafx.application.Application
import javafx.application.Platform
import javafx.scene.Group
import javafx.scene.Scene
import javafx.scene.control.ScrollPane
import javafx.scene.layout.Priority
import javafx.scene.layout.VBox
import javafx.stage.Stage
import kotlin.system.exitProcess

class Main : Application() {
    private val MAX_SCREEN_WIDTH = 1600.0
    private val MAX_SCREEN_HEIGHT= 1200.0
    private val MIN_SCREEN_WIDTH = 800.0
    private val MIN_SCREEN_HEIGHT= 600.0

    override fun start(stage: Stage) {
        val model = Model()

        val pane = CanvasView(model)
//        val group = Group()
//        group.isAutoSizeChildren = false
//        group.children.add(pane)
        val toolBar = ToolBarView(model, stage, pane)
        val statusBar = StatusBarView(model)

        // Register views with the model
        model.addView(toolBar)
        model.addView(pane)
        model.addView(statusBar)

        // Root of the scene graph
        val root = VBox()
        VBox.setVgrow(pane, Priority.ALWAYS)
        root.children.addAll(toolBar, pane, statusBar)

        // Stage setup and display
        stage.scene = Scene(root)
        stage.isResizable = true
        stage.maxWidth = MAX_SCREEN_WIDTH
        stage.maxHeight = MAX_SCREEN_HEIGHT
        stage.minWidth = MIN_SCREEN_WIDTH
        stage.minHeight = MIN_SCREEN_HEIGHT
        stage.title = "Virtual Bulletin Board"
        stage.show()
        stage.setOnCloseRequest {
            Platform.exit()
            exitProcess(0)
        }
    }
}