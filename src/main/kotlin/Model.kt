import javafx.scene.image.ImageView

class Model {
    val imageViews = ArrayList<ImageView>()
    private val views = ArrayList<IView>()

    // Status variables
    var mode = "Cascade"
    var selectedImageView: ImageView? = null
    var selectedFileName = ""

    // View Management Methods:
    fun addView(view: IView) {
        views.add(view)
    }

//    fun removeView(view: IView) {
//        views.remove(view)
//    }

    fun notifyViews() {
        for (view in views) {
            view.update(this)
        }
    }
}