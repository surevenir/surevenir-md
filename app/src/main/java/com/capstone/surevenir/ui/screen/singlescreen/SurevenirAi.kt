package com.capstone.surevenir.ui.screen.singlescreen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import android.annotation.SuppressLint
import android.webkit.WebView
import android.webkit.WebViewClient
import android.webkit.WebSettings
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavHostController
import com.capstone.surevenir.R
import com.capstone.surevenir.ui.screen.navmenu.sfui_semibold

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun SurevenirAi(navController: NavHostController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(Color(0xFFE7E7E9), CircleShape)
                    .clickable { navController.popBackStack() },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.arrow),
                    contentDescription = "Back",
                    modifier = Modifier.size(24.dp)
                )
            }
            Text(
                text = "Surevenir GenAI",
                fontSize = 25.sp,
                fontFamily = sfui_semibold,
                color = Color(0xFFCC5B14)
            )
            Spacer(modifier = Modifier.size(48.dp))
        }

        AndroidView(
            factory = { context ->
                WebView(context).apply {
                    settings.apply {
                        javaScriptEnabled = true
                        domStorageEnabled = true
                        databaseEnabled = true
                        javaScriptCanOpenWindowsAutomatically = true
                        allowFileAccess = true
                        allowContentAccess = true
                        mediaPlaybackRequiresUserGesture = false
                        mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
                    }

                    isFocusable = true
                    isFocusableInTouchMode = true
                    requestFocus()

                    webViewClient = WebViewClient()

                    val htmlContent = """""
                        <!DOCTYPE html>
                            <html>
                            <head>
                                <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">
                                <link rel="stylesheet" href="https://www.gstatic.com/dialogflow-console/fast/df-messenger/prod/v1/themes/df-messenger-default.css">
                                <script src="https://www.gstatic.com/dialogflow-console/fast/df-messenger/prod/v1/df-messenger.js"></script>
                                <style>
                                    html, body {
                                        margin: 0;
                                        padding: 0;
                                        height: 100%;
                                        overflow: hidden;
                                    }
                                    df-messenger {
                                        --df-messenger-font-color: #000;
                                        --df-messenger-font-family: Google Sans;
                                        --df-messenger-chat-background: #f3f6fc;
                                        --df-messenger-message-user-background: #d3e3fd;
                                        --df-messenger-message-bot-background: #fff;
                                        --df-messenger-button-titlebar-color: #CC5B14;
                            
                                        /* Hilangkan penggunaan vh dan vw, gunakan 100% */
                                        width: 100%;
                                        height: 100%;
                                    }
                                </style>
                            </head>
                            <body>
                                <df-messenger
                                    project-id="submission-mgce-juniawan"
                                    agent-id="e5dc7f79-4500-4abf-a75c-ef9fcb1cecb9"
                                    language-code="en"
                                    chat-title="SureVenir Assistant">
                                </df-messenger>
                            </body>
                            </html>

                        
                        """

                    loadDataWithBaseURL(
                        "https://www.gstatic.com/",
                        htmlContent,
                        "text/html",
                        "UTF-8",
                        null
                    )
                }
            },
            modifier = Modifier.fillMaxSize()
        )
    }
}

