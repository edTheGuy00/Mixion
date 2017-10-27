package com.taskail.mixion.helpers

import android.graphics.Color
import android.view.View
import at.grabner.circleprogress.CircleProgressView

/**Created by ed on 10/26/17.
 */
class CircleProgressViewHelper {

    companion object {
        @JvmStatic fun showLoading(circleProgressView: CircleProgressView) {
            circleProgressView.visibility = View.VISIBLE
            circleProgressView.setValue(50f)
            circleProgressView.setText("Loading...")
            circleProgressView.spin()
        }

        @JvmStatic fun stopLoading(circleProgressView: CircleProgressView) {
            circleProgressView.stopSpinning()
            circleProgressView.visibility = View.GONE
        }

        @JvmStatic fun unableToLoadError ( circleProgressView: CircleProgressView ){
            circleProgressView.stopSpinning()
            circleProgressView.setBarColor(Color.RED)
            circleProgressView.setText("ERROR")
            circleProgressView.setOnClickListener {  }
        }

        @JvmStatic fun setRetryError ( circleProgressView: CircleProgressView ){
            circleProgressView.stopSpinning()
            circleProgressView.setBarColor(Color.RED)
            circleProgressView.setText("Retry")
        }
    }
}