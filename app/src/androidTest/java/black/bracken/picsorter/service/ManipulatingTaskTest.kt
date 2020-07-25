package black.bracken.picsorter.service

import android.content.Context
import androidx.test.filters.SmallTest
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.platform.app.InstrumentationRegistry
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.File

@RunWith(AndroidJUnit4ClassRunner::class)
@SmallTest
class ManipulatingTaskTest {
    private lateinit var context: Context
    private lateinit var dummyFile: File

    @Before
    fun setup() {
        context = InstrumentationRegistry.getInstrumentation().context

        dummyFile = File(context.getExternalFilesDir("/"), "dummy.png").apply {
            createNewFile()
        }
    }

    @Test
    fun shouldMoveFileAnother() {
        val destination = File(context.getExternalFilesDir("/"), "dummy_dest.png")
        val request = ManipulatingTask.TaskRequest(destination.absolutePath, null, null)

        runBlocking {
            ManipulatingTask(dummyFile, context, request).execute()
        }

        assertThat(dummyFile.exists()).isFalse()
        assertThat(destination.exists()).isTrue()
    }

    @Test
    fun shouldRename() {
        val newName = "new_dummy"
        val destination = File(context.getExternalFilesDir("/"), "$newName.png")
        val request = ManipulatingTask.TaskRequest(null, newName, null)

        runBlocking {
            ManipulatingTask(dummyFile, context, request).execute()
        }

        assertThat(destination.nameWithoutExtension).isEqualTo(newName)
        assertThat(dummyFile.exists()).isFalse()
        assertThat(destination.exists()).isTrue()
    }

    @Test
    fun shouldDeleteLater() {
        val delaySeconds = 3
        val request = ManipulatingTask.TaskRequest(null, null, delaySeconds)

        GlobalScope.launch {
            ManipulatingTask(dummyFile, context, request).execute()
        }

        assertThat(dummyFile.exists()).isTrue()

        runBlocking {
            delay(delaySeconds * 1000L + 500L)
        }

        assertThat(dummyFile.exists()).isFalse()
    }

}