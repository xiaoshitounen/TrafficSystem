package swu.xl.trafficsystem.widget

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import swu.xl.trafficsystem.R

interface InputListener {
    fun onTextInput(input: String)
    fun onCanceled()
}

class InputDialog : DialogFragment(), View.OnClickListener {

    var title = "Title"
    var defaultValue = ""
    var listener: InputListener? = null
    private lateinit var input: EditText


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (listener == null) {
            dismiss()
            return
        }
        setStyle(STYLE_NO_TITLE, R.style.ThemeOverlay_AppCompat_Dialog)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.input_dialog, container, false)
        initView(view)
        return view
    }

    private fun initView(root: View) {
        val title: TextView = root.findViewById(R.id.title)
        title.text = this.title
        input = root.findViewById(R.id.input)
        input.setText(defaultValue)
        root.findViewById<View>(R.id.btn_ok).setOnClickListener(this)
        root.findViewById<View>(R.id.btn_cancel).setOnClickListener(this)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.btn_ok-> listener?.onTextInput(input.text.toString())
            R.id.btn_cancel -> listener?.onCanceled()
        }
    }

    override fun onResume() {
        super.onResume()
        input.requestFocus()
    }
}