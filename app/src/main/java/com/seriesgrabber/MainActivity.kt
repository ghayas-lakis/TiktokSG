package com.seriesgrabber

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.seriesgrabber.databinding.ActivityMainBinding
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var engine: SeriesEngine
    private var job: Job? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        engine = SeriesEngine(this)
        handleIntent(intent)
        binding.startButton.setOnClickListener { toggle() }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        handleIntent(intent)
    }

    /** Accepts links shared from TikTok via Share > Series Grabber. */
    private fun handleIntent(i: Intent?) {
        if (i?.action == Intent.ACTION_SEND) {
            val text = i.getStringExtra(Intent.EXTRA_TEXT) ?: return
            Regex("""https?://\S+""").find(text)?.let { binding.urlInput.setText(it.value) }
        }
    }

    private fun log(msg: String) = runOnUiThread {
        binding.logText.append(msg + "\n")
        binding.logScroll.post { binding.logScroll.fullScroll(View.FOCUS_DOWN) }
    }

    private fun toggle() {
        if (job?.isActive == true) {
            job?.cancel()
            return
        }
        val url = binding.urlInput.text.toString().trim()
        if (url.isEmpty()) {
            binding.urlInput.error = "Paste a link first"
            return
        }
        binding.logText.text = ""
        binding.startButton.setText(R.string.cancel)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        job = lifecycleScope.launch {
            try {
                val saved = engine.run(
                    source = url,
                    name = binding.nameInput.text.toString().trim(),
                    requireMarker = binding.markerSwitch.isChecked,
                    reverse = binding.reverseSwitch.isChecked,
                    log = ::log,
                    progress = { p -> runOnUiThread { binding.progress.progress = p } },
                )
                log("Done! Saved to $saved")
            } catch (e: CancellationException) {
                log("Cancelled.")
                throw e
            } catch (e: Exception) {
                log("Error: ${e.message}")
            } finally {
                binding.startButton.setText(R.string.start)
                window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
            }
        }
    }
}
