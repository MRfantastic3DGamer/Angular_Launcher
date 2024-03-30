package com.dhruv.angular_launcher.settings_screen.presentation.components.shader

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.dhruv.angular_launcher.settings_screen.presentation.components.H3


@Composable
fun CodeEditor(
    onSave: () -> Unit,
    code: TextFieldValue,
    onChange: (TextFieldValue) -> Unit
) {
    val context = LocalContext.current

    Column {
        TextField(
            value = code,
            onValueChange = onChange,
            Modifier
        )

        Spacer(modifier = Modifier.height(10.dp))

        Button(onClick = onSave) {
            H3(text = "save code")
        }
    }

//    val touch = EditorTouch()

    // Handle back press to navigate back in the web view history
//    BackHandler(onBack = onBackPressed)
//    val webView = remember {
//        WebView(context)
//    }

//    AndroidView(
//        modifier = Modifier.fillMaxSize(),
//        factory = { _ ->
//            webView.apply {
//                layoutParams = ViewGroup.LayoutParams(
//                    ViewGroup.LayoutParams.MATCH_PARENT,
//                    ViewGroup.LayoutParams.MATCH_PARENT,
//                )
//                webViewClient = WebViewClient()
//                this.settings.javaScriptEnabled = true
//                webChromeClient = WebChromeClient()
//
//                loadUrl("file:///android_asset/ace_editor.html")
////                touch.getCode(this){}
////                touch.setCode(this, "new code")
//
////                this.evaluateJavascript(
////                    """
////                        var editor = ace.edit("editor")
////                        editor.setValue("new code")
////                    """.trimIndent()
////                ) {
////                    println(it)
////                }
//                this.evaluateJavascript("ace.edit(\"editor\").getValue()") {
//                    println(it)
//                }
//            }
//        }
//    )
}