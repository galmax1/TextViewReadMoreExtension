package com.galmax.readmore

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.galmax.readmore.extension.setReadMoreText

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setupTestTextFields()
        findViewById<Button>(R.id.btnReload).setOnClickListener {
            setupTestTextFields()
        }
    }

    private fun setupTestTextFields() {
        /**
        Case #1:
        - Content 5 lines long.
        - No read more button.
        - Display all text
        showLines >= Content lines count
         */
        findViewById<TextView>(R.id.case1_content)
            .setReadMoreText(
                text = getString(R.string.content_5_lines),
                showLines = 10,
            )

        /**
        Case #2:
        - Content 5 lines long.
        - No read more button.
        - Display all text
        showLines + showLinesDefer >= Content lines count
         */
        findViewById<TextView>(R.id.case2_content)
            .setReadMoreText(
                text = getString(R.string.content_5_lines),
                showLines = 3,
                showLinesDefer = 2
            )

        /**
        Case #3:
        - Content 5 lines long.
        - Show read more button.
        - Display 3 lines only
        showLines + showLinesDefer < Content lines count
         */
        findViewById<TextView>(R.id.case3_content)
            .setReadMoreText(
                text = getString(R.string.content_5_lines),
                showLines = 3,
                showLinesDefer = 1
            )

        /**
        Case #4:
        - Content 5 lines long.
        - Show read more button.
        - Display 4 lines only
        showLines + showLinesDefer < Content lines count
         */
        findViewById<TextView>(R.id.case4_content)
            .setReadMoreText(
                text = getString(R.string.content_5_lines),
                showLines = 4,
                showLinesDefer = 0
            )
    }
}